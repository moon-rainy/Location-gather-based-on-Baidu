package com.example.maplocationdemo;


import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.example.maplocationdemo.baidu.location.LocationService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 * <p>
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 * <p>
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
 */
public class MyApplication extends Application {
    public LocationService locationService;
    // 震动
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 百度地图定位sdk
         */
        // 默认本地个性化地图初始化方法
        SDKInitializer.setAgreePrivacy(this, true);
        try {
            SDKInitializer.initialize(this);
        } catch (BaiduMapSDKException e) {
            e.printStackTrace();
        }
        SDKInitializer.setCoordType(CoordType.BD09LL);

        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

    }
}
