package com.example.maplocationdemo.baidu.background;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
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
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.example.maplocationdemo.DataBase.Address;
import com.example.maplocationdemo.DataBase.Connection.ConnectionTaskService;
import com.example.maplocationdemo.DataBase.GPSInfo;
import com.example.maplocationdemo.DataBase.PoiInfo;
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
import com.example.maplocationdemo.Utils.CodeUtil;
import com.example.maplocationdemo.baidu.location.LocationService;

import java.util.ArrayList;
import java.util.List;

public class BackgroundLocationActivity extends Activity{
    //    定位服务的声明
    private LocationService locationService;
    private LocationClient mClient;
    private MyLocationListener myLocationListener = new MyLocationListener();
    LocationClientOption mOption;
    //  存储点的声明
    List<LatLng> points = new ArrayList<>();

    //    activity中组件的声明
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mForegroundBtn;
    //  后台任务栏的声明
    private BackgroundService mNotificationUtils;
    private Notification notification;
    //  参数的声明
    Polyline lastPolyline = null;
    private boolean isFirstLoc = true;
    private boolean isEnableLocInForeground = false;
    private static final int paddingLeft = 0;
    private static final int paddingTop = 0;
    private static final int paddingRight = 0;
    private static final int paddingBottom = 500;
    private TextView mTextView;

    private final int SDK_PERMISSION_REQUEST = 127;

    private ConnectionTaskService.MyBinder binder;
    private boolean isBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (ConnectionTaskService.MyBinder) service;
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
            isBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_location);
        initViews();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e("GlobalException", "未捕获的异常：" + t + "\n" + Log.getStackTraceString(e));
            }
        });

        //  添加后台线程服务管理后台线程
        Intent intent = new Intent(this, ConnectionTaskService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

//        getPersimmions();


        // 定位初始化
        try {
            mClient = new LocationClient(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("baidu_location", "onCreate: ForegroundActivity mClient = " + mClient);

//         mOption = new BackgroundOption().getDefaultLocationClientOption();
        mOption = new LocationClientOption();
        mOption.setScanSpan(0);

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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // -----------location config ------------
//        locationService = ((MyApplication) getApplication()).locationService;
//        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
//        locationService.registerListener(mListener);
//        //注册监听
//        int type = getIntent().getIntExtra("from", 0);
//        if (type == 0) {
//            LocationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.start();
//        }
//
//        mForegroundBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mForegroundBtn.getText().toString().equals("开始后台定位")) {
//                    locationService.start();// 定位SDK
//                    // start之后会默认发起一次定位请求，开发者无须判断 isStart 并主动调用request
//
//                } else {
//                    locationService.stop();
//                    binder.getService().stopTask();
//
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onStop() {
//        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
//        unbindService(connection);  //停止后台线程连接
//        super.onStop();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        unbindService(connection);  //停止后台线程连接
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
                        binder.getService().stopTask();
                        mClient.stop();
                    } else {
                        //开启后台定位
                        mClient.enableLocInForeground(1, notification);
                        isEnableLocInForeground = true;
                        mForegroundBtn.setText("停止后台定位");
                        mOption = new BackgroundOption().getDefaultLocationClientOption();
                        mClient.setLocOption(mOption);
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

            if (bdLocation.getLocType() != BDLocation.TypeServerError) {
                String code = CodeUtil.getTimeCode();

                @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                Address address = new Address();
                address.setCode(code);
                address.setPhone(androidId);
                address.setTime(bdLocation.getTime());
                address.setLocType(bdLocation.getLocType());
                address.setLocTypeDescription(bdLocation.getLocTypeDescription());
                address.setLatitude(bdLocation.getLatitude());
                address.setLongitude(bdLocation.getLongitude());
                address.setRadius((double) bdLocation.getRadius());
                address.setProvince(bdLocation.getProvince());
                address.setCity(bdLocation.getCity());
                address.setCityCode(bdLocation.getCityCode());
                address.setCity(bdLocation.getCity());
                address.setDistrict(bdLocation.getDistrict());
                address.setTown(bdLocation.getTown());
                address.setStreet(bdLocation.getStreet());
                address.setAddr(bdLocation.getAddrStr());
                address.setStreetNumber(bdLocation.getStreetNumber());
                address.setUserIndoorState(bdLocation.getUserIndoorState());
                address.setDirection(String.valueOf(bdLocation.getDirection()));
                address.setLocationDescribe(bdLocation.getLocationDescribe());
                if(bdLocation.hasAltitude()){
                    address.setHeight(Math.round(bdLocation.getAltitude() * 100.0) / 100.0);
                }

                if (binder != null) {
                    binder.getService().startAddressTask(BackgroundLocationActivity.this, address);
                }

                if (bdLocation.getPoiList() != null && !bdLocation.getPoiList().isEmpty()) {
                    for (int i = 0; i < bdLocation.getPoiList().size(); i++) {
                        Poi poi = (Poi) bdLocation.getPoiList().get(i);

                        PoiInfo poiInfo = new PoiInfo();
                        poiInfo.setCode(code);
                        poiInfo.setPoiName(poi.getName());
                        poiInfo.setPoiATag(poi.getTags());
                        if (binder != null) {
                            binder.getService().startPoiInfoTask(poiInfo);
                        }

                    }
                }
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                    GPSInfo gpsInfo = new GPSInfo();
                    gpsInfo.setCode(code);
                    gpsInfo.setSpeed(bdLocation.getSpeed());
                    gpsInfo.setSatellite(bdLocation.getSatelliteNumber());
                    gpsInfo.setHeight(bdLocation.getAltitude());
                    gpsInfo.setStatus(bdLocation.getGpsAccuracyStatus());
                    binder.getService().startGPSInfoTask(gpsInfo);

                }
            }
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
            mBaiduMap.addOverlay(dotOption);

            //  加入新的点
            LatLng newPoint = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                points.add(newPoint);
                refreshPolyline();
            }



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


            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }

        }
    }
    // 更新地图上的折线
    private void refreshPolyline() {
        // 如果地图不为null且点的数量大于1
        if (mBaiduMap != null && points.size() > 1) {
            // 如果之前的折线存在，移除它
            if (lastPolyline != null) {
                lastPolyline.remove();
            }

            // 创建新的折线构建者对象
            PolylineOptions polylineOptions = new PolylineOptions().points(points).color(Color.BLUE).width(5);

            // 添加新折线到地图并保存其引用
            lastPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
        }
    }

}
