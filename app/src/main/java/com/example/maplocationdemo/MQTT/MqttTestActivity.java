package com.example.maplocationdemo.MQTT;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.maplocationdemo.Notification.NotificationData;
import com.example.maplocationdemo.Notification.notification;
import com.example.maplocationdemo.R;
import com.example.maplocationdemo.Utils.JsonUtil;


public class MqttTestActivity extends AppCompatActivity {

    private TextView messageText;
    private Button connectButton;
    private Button disconnectButton;

    // 内部类实现了 MqttListener 接口
    private class MqttMessageListener implements MqttListener {
        @Override
        public void onConnected() {
            runOnUiThread(() -> {
                Toast.makeText(MqttTestActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onDisconnected() {
            runOnUiThread(() -> {
                Toast.makeText(MqttTestActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onSubscribed(String... topics) {

        }

        @Override
        public void onReceiveMessage(String topic, byte[] message) {
            runOnUiThread(() -> {
                // 显示接收到的消息
                messageText.setText(new String(message));
                notification.showBasicNotification(MqttTestActivity.this,JsonUtil.processReceivedMessage(message));
            });
        }

        @Override
        public void onSendMessage(String topic, byte[] data) {

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        messageText = findViewById(R.id.messageText);
        connectButton = findViewById(R.id.connectButton);
        disconnectButton = findViewById(R.id.disconnectButton);

        MqttListener listener = new MqttMessageListener();

        connectButton.setOnClickListener(v -> {
            MqttManager.getInstance().addMqttListener(listener); // 添加监听器到 MQTT 管理器中
            MqttManager.getInstance().connect();
        });

        disconnectButton.setOnClickListener(v -> {
            MqttManager.getInstance().disconnect(); // 断开 MQTT 连接
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MqttManager.getInstance().disconnect(); // 确保在 Activity 销毁时断开连接
    }



}