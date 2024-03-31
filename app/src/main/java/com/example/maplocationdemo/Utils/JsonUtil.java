package com.example.maplocationdemo.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.maplocationdemo.Notification.NotificationData;

import java.nio.charset.StandardCharsets;


public class JsonUtil {

    public static NotificationData processReceivedMessage(byte[] data) {
        NotificationData NofData = new NotificationData();
        try {
            String json = new String(data, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(json);

            // 获取 JSON 中的字段值
            String title = jsonObject.getString("Title");
            NofData.setTitle(title);
            String message = jsonObject.getString("Message");
            NofData.setMessage(message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return NofData;
    }

}
