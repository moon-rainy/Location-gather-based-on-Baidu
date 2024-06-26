package com.example.maplocationdemo.baidu.background;

import com.baidu.location.LocationClientOption;

public class BackgroundOption {
    private static LocationClientOption mOption;

    public BackgroundOption() {
    }

    public LocationClientOption getDefaultLocationClientOption() {
        mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType( "bd09ll" ); // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(10000); // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true); // 可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true); // 可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(true); // 可选，设置是否需要设备方向结果
        mOption.setLocationNotify(false); // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true); // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop
        mOption.setIsNeedLocationDescribe(true); // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        mOption.setIsNeedLocationPoiList(true); // 可选，默认false，设置是否需要POI结果，可以在BDLocation
        mOption.SetIgnoreCacheException(false); // 可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setOpenGps(true); // 可选，默认false，设置是否开启Gps定位
        mOption.setIsNeedAltitude(true); // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        return mOption;
    }


}
