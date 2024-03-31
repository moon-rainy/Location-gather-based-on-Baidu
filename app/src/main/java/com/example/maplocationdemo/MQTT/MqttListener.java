package com.example.maplocationdemo.MQTT;

public interface MqttListener {
    void onConnected();
    void onDisconnected();
    void onSubscribed(String... topics);
    void onReceiveMessage(String topic, byte[] data);
    void onSendMessage(String topic, byte[] data);
}
