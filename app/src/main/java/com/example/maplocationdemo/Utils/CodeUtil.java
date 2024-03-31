package com.example.maplocationdemo.Utils;

import java.text.SimpleDateFormat;

public class CodeUtil {
    public static String getTimeCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String number = sdf.format(System.currentTimeMillis());//2024 03 14 15 52 16

        int x = (int) (Math.random() * 900) + 100;
        return number + x;
    }

}
