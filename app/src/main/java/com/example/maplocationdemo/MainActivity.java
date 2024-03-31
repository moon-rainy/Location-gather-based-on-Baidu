package com.example.maplocationdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.example.maplocationdemo.About.About;
import com.example.maplocationdemo.MQTT.MqttTestActivity;
import com.example.maplocationdemo.baidu.background.BackgroundLocationActivity;
import com.example.maplocationdemo.baidu.location.BaiDuLocationActivity;
import com.example.maplocationdemo.baidu.location.LocationService;
import com.example.maplocationdemo.baidu.map.BaiDuMapActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    private ListView FunctionList;

    private final int DIALOG_KEY_BACK = 1;
    private final int DIALOG_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPersimmions();

        //创建弹窗显示隐私政策
        if (!Util.contains(this, Util.SP_PRIVACY_DIALOG)) {
            createPrivacyDialog();
        } else {
            boolean status = Util.getString(MainActivity.this, Util.SP_PRIVACY_STATUS).equals("1");
            try {
                initSDK(status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        Button btBaiduLocation = findViewById(R.id.bt_baidu_location);
        btBaiduLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BaiDuLocationActivity.class);
                startActivity(intent);
            }
        });

        Button btBaiduMap = findViewById(R.id.bt_baidu_map);
        btBaiduMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BaiDuMapActivity.class);
                startActivity(intent);
            }
        });

        Button btBackground = findViewById(R.id.bt_baidu_background);
        btBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BackgroundLocationActivity.class);
                startActivity(intent);
            }
        });

        Button Mqtt = findViewById(R.id.mqtt);
        Mqtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MqttTestActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createPrivacyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String notifyString = "为进一步加强对最终用户个人信息的安全保护措施, 请仔细阅读如下隐私政策并确认是否同意：\n《服务隐私政策》";
        SpannableStringBuilder spannableString = new SpannableStringBuilder(notifyString);
        Pattern pattern = Pattern.compile("《服务隐私政策》");
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            setClickableSpan(spannableString, matcher);
        }

        View view = View.inflate(this, R.layout.notify_privacy_text, null);
        TextView notifyText = (TextView) view.findViewById(R.id.notify_text);
        notifyText.setText(spannableString);
        notifyText.setMovementMethod(LinkMovementMethod.getInstance());

        builder.setView(view);
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    initSDK(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Util.putString(MainActivity.this, Util.SP_PRIVACY_DIALOG, Util.SP_PRIVACY_DIALOG);
                Util.putString(MainActivity.this, Util.SP_PRIVACY_STATUS, "1");
            }
        });

        builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    initSDK(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Util.putString(MainActivity.this, Util.SP_PRIVACY_STATUS, "0");
            }
        });


        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        layoutParams.weight = 10;
        positiveButton.setLayoutParams(layoutParams);
        negativeButton.setLayoutParams(layoutParams);
    }

    private void setClickableSpan(SpannableStringBuilder span, Matcher matcher) {
        int start = matcher.start();
        int end = matcher.end();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                final Uri uri = Uri.parse("https://lbsyun.baidu.com/index.php?title=openprivacy");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }
        };

        span.setSpan(new ForegroundColorSpan(Color.CYAN), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void initSDK(boolean status) throws Exception {
        LocationClient.setAgreePrivacy(status);
        ((MyApplication)getApplication()).locationService = new LocationService(getApplicationContext());

        onUsePermission();
        getPersimmions();
    }

    private void onUsePermission() {
        if (!Util.contains(this, Util.SP_PERMISSION_DIALOG)) {
            Util.putString(this, Util.SP_PERMISSION_DIALOG, Util.SP_PERMISSION_DIALOG);
            showMissingPermissionDialog("位置信息采集器在使用时需要申请以下权限：", "1、定位权限，用于定位功能测试。\n2、读写权限，用于写入离线定位数据。\n", DIALOG_PERMISSION);
        }
    }

    private void showMissingPermissionDialog(String title, String message, final int type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == DIALOG_KEY_BACK) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                        }

                    }
                });

        builder.setCancelable(false);

        builder.show();
    }



    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }

            // 添加访问网络状态的权限
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }

        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}