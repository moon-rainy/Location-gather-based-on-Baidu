package com.example.maplocationdemo.MQTT;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.maplocationdemo.Utils.CodeUtil;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedListener;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedListener;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAckReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class MqttManager implements MqttClientConnectedListener, MqttClientDisconnectedListener {

    private static final String TAG = CodeUtil.getTimeCode();

    private static final String SERVER_HOST = "XXXXXXXXXX";
    private static final int SERVER_PORT = 0;
    private static final String USERNAME = "XXXXXXXXXXX";
    private static final String PASSWORD = "XXXXXXXXXXXX";




    private static final MqttManager instance = new MqttManager();
    private static volatile Mqtt5Client client;
    private final List<MqttListener> listeners = new ArrayList<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    private MqttManager() {
        //1.客户端
        client = Mqtt5Client.builder()
                .identifier(TAG) // use a unique identifier
                .serverHost(SERVER_HOST) // use the public HiveMQ broker
                .serverPort(SERVER_PORT)
                .addConnectedListener(this) // add a connected listener
                .addDisconnectedListener(this) // add a disconnected listener)
                .simpleAuth()
                .username(USERNAME)
                .password(PASSWORD.getBytes())
                .applySimpleAuth()
                .automaticReconnectWithDefaultConfig() // the client automatically reconnects
                .build();
        Log.d("MQTT","客户端部署");

//        // 匿名连接
//        client.toBlocking().connectWith()
//                .cleanStart(true)   // 清理之前的会话，视为全新的会话
//                .keepAlive(60)      // 保持连接的时间间隔，单位为秒
//                .send();
    }

    private String[] getSubTopics() {
        return new String[] {"test/topic"};
    }

    public void addMqttListener(MqttListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeMqttListener(MqttListener listener) {
        listeners.remove(listener);
    }


    public static MqttManager getInstance() {
        return instance;
    }

    @Override
    public void onConnected(@NonNull MqttClientConnectedContext context) {
        for (MqttListener listener : listeners) {
            listener.onConnected();
        }
        subscribeAll();
    }
    @Override
    public void onDisconnected(@NonNull MqttClientDisconnectedContext context) {
        System.err.println(
                TAG + ": onDisconnected() - isConnected=" + client.getState().isConnected() +
                        ", isConnectedOrReconnect=" + client.getState().isConnectedOrReconnect()
        );
        for (MqttListener l : listeners) {
            l.onDisconnected();
        }

    }
    public void connect() {
        client.toAsync().connectWith()
                .cleanStart(true)
                .keepAlive(30)
                .send()
                .thenAccept(it -> {
                    if (it.getReasonCode() == Mqtt5ConnAckReasonCode.SUCCESS) {
                        System.out.println(TAG + ": connect() - SUCCESS");
                    } else {
                        System.err.println(TAG + ": connect() - " + it.getReasonCode());
                    }
                });
    }
    public void disconnect() {
        client.toAsync().disconnect().thenAccept(aVoid -> System.out.println(TAG + ": disconnect()"));

    }
    public void subscribeAll() {
        // 5. 订阅消息
        CompletableFuture.supplyAsync(() -> {
            CompletableFuture<?>[] futures = Arrays.stream(getSubTopics())
                    .map(this::subscribe)
                    .map(future -> future.thenCompose(success -> CompletableFuture.supplyAsync(() -> {
                        if (!success.getReasonString().isPresent()) {
                            System.out.println(TAG + ": 订阅成功");
                            return true;
                        } else {
                            System.err.println(
                                    TAG + ": subscribe() - reasonCodes=[" +
                                            success.getReasonCodes().stream().map(Object::toString).collect(Collectors.joining(", ")) +
                                            "], reasonString=" + success.getReasonString()
                            );
                            return false;
                        }
                    }, executor)))
                    .toArray(CompletableFuture<?>[]::new);

            CompletableFuture.allOf(futures).join(); // Wait for all subscription results
            if (Arrays.stream(futures).allMatch(future -> {
                try {
                    return (boolean) future.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            })) {
                System.out.println(TAG + ": subscribeAll() - All subscriptions successful");
            }
            for (MqttListener l : listeners) {
                l.onSubscribed(getSubTopics());
            }
            return null;
        }, executor);
    }

    private final Consumer<Mqtt5Publish> callback = publish -> {
        String topic = publish.getTopic().toString();
        byte[] payload = publish.getPayloadAsBytes();
        processReceivedMessage(topic, payload);
    };

    private void processReceivedMessage(String topic, byte[] data) {
        for (MqttListener l : listeners) {
            l.onReceiveMessage(topic, data);
        }
    }

    public CompletableFuture<Mqtt5SubAck> subscribe(String topic) {
        return client.toAsync().subscribeWith()
                .topicFilter(topic)
                .noLocal(true) // we do not want to receive our own messages
                .qos(MqttQos.AT_MOST_ONCE)
                .callback(callback)
                .executor(executor)
                .send();
    }

    public void unsubscribe(String topic) {
        client.toAsync().unsubscribeWith()
                .topicFilter(topic)
                .send().thenAccept(unsubAck -> System.out.println(TAG + ": unsubscribe() - " + unsubAck));;
    }

    public void publish(String topic, byte[] payload) {
        // 6. 发布订阅
        client.toAsync().publishWith()
                .topic(topic)
                .qos(MqttQos.AT_MOST_ONCE)
                .payload(payload)
                .send()
                .thenAccept(mqtt5PublishResult -> {
                    Mqtt5Publish mqtt5Publish = mqtt5PublishResult.getPublish();
                    byte[] data = mqtt5Publish.getPayloadAsBytes();
                    for (MqttListener l : listeners) {
                        l.onSendMessage(topic, data);
                    }
                });
    }

}



