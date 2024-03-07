package com.example.maplocationdemo.baidu.background;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.maplocationdemo.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.maplocationdemo.baidu.map.BaiDuMapActivity;

import java.util.ArrayList;

public class BackgroundLocationActivity extends Activity{
    private LocationClient mClient;
    private MyLocationListener myLocationListener = new MyLocationListener();

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mForegroundBtn;

    private BackgroundService mNotificationUtils;
    private Notification notification;

    private boolean isFirstLoc = true;
    private boolean isEnableLocInForeground = false;
    private static final int paddingLeft = 0;
    private static final int paddingTop = 0;
    private static final int paddingRight = 0;
    private static final int paddingBottom = 500;
    private TextView mTextView;

    private final int SDK_PERMISSION_REQUEST = 127;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_location);
        initViews();

//        getPersimmions();


        // 定位初始化
        try {
            mClient = new LocationClient(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("baidu_location", "onCreate: ForegroundActivity mClient = " + mClient);
        LocationClientOption mOption = new LocationClientOption();
//        mOption.setScanSpan(5000);
        mOption.setCoorType("bd09ll");
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        if (mClient != null) {
            mClient.setLocOption(mOption);
            mClient.registerLocationListener(myLocationListener);
            mClient.start();
        }



        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new BackgroundService(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("位置采集器正在后台进行定位……", "正在持续采集定位数据");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(BackgroundLocationActivity.this);
            Intent nfIntent = new Intent(BackgroundLocationActivity.this, BackgroundLocationActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(BackgroundLocationActivity.this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("位置采集器正在后台进行定位……") // 设置下拉列表里的标题
                    .setSmallIcon(R.drawable.ic_launcher_background) // 设置状态栏内的小图标
                    .setContentText("正在持续采集定位数据") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
        }
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        if (mClient != null) {
            // 关闭前台定位服务
            mClient.disableLocInForeground(true);
            // 取消之前注册的 BDAbstractLocationListener 定位监听函数
            mClient.unRegisterLocationListener(myLocationListener);
            // 停止定位sdk
            mClient.stop();
        }
    }

    private void initViews(){
        mForegroundBtn = (Button) findViewById(R.id.bt_foreground);
        mForegroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClient != null) {
                    if (isEnableLocInForeground) {
                        //关闭后台定位（true：通知栏消失；false：通知栏可手动划除）
                        mClient.disableLocInForeground(true);
                        isEnableLocInForeground = false;
                        mForegroundBtn.setText("开始后台定位");
                        mClient.stop();
                    } else {
                        //开启后台定位
                        mClient.enableLocInForeground(1, notification);
                        isEnableLocInForeground = true;
                        mForegroundBtn.setText("停止后台定位");
                        LocationClientOption option = new LocationClientOption();
                        option.setScanSpan(5000);
                        option.setCoorType("bd09ll");
                        option.setIsNeedAddress(true);
                        option.setOpenGps(true);
                        mClient.setLocOption(option);
                        mClient.start();
                    }
                }
            }
        });
        mMapView = (MapView) findViewById(R.id.mv_foreground);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
    }


    class  MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            //地图SDK处理
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            LatLng point = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            OverlayOptions dotOption = new DotOptions().center(point).color(0xAAA9A9A9);
//            mBaiduMap.addOverlay(dotOption);

            Log.d("baidu_location", "onCreate: Latitude = " + bdLocation.getLatitude());
            Log.d("baidu_location", "onCreate: Longitude = " + bdLocation.getLongitude());

            StringBuilder sb = new StringBuilder(256);
            sb.append("Latitude:");
            sb.append(bdLocation.getLatitude());
            sb.append("      ");
            sb.append("Longitude");
            sb.append(bdLocation.getLongitude()).append("\n");
            if (null != mTextView){
                mTextView.append(sb.toString());
            }

        }
    }

    /**
     * 添加view展示定位结果回调
     *
     * @param mapView 地图控件
     */
    private void addView(MapView mapView) {
        mTextView = new TextView(this);
        mTextView.setTextSize(15.0f);
        mTextView.setTextColor(Color.BLACK);
        mTextView.setBackgroundColor(Color.parseColor("#AAA9A9A9"));
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
        builder.width(mapView.getWidth());
        builder.height(paddingBottom);
        builder.point(new Point(0, mapView.getHeight()));
        builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
        mBaiduMap.setViewPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        mapView.addView(mTextView, builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                addView(mMapView);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();


            if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }

        }
    }


}
