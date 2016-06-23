package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Utils.StreamTools;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/6/22.
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private static final int ENTER_HOME = 0;
    private static final int NEW_VERSION = 1;
    private static final int INTERNET_ERROR = 2;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ENTER_HOME:
                    enterHome();
                    break;

                case NEW_VERSION:
                    Log.e(TAG,"弹出升级对话框");
                    break;

                case INTERNET_ERROR:
                    Toast.makeText(SplashActivity.this,"网络错误",Toast.LENGTH_LONG);
                    enterHome();
                    break;
            }
        }
    };

    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        TextView textView_version = (TextView) findViewById(R.id.tv_splash_version);
        textView_version.setText("版本号：" + getVersionCode());
        checkVersion();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f,1.0f);
        alphaAnimation.setDuration(1500);
        findViewById(R.id.rl_splash).startAnimation(alphaAnimation);
    }

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "http://192.168.0.102:8080/updateinfo.html";
                Message message = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(4000);
                    connection.setReadTimeout(4000);
                    int responseCode = connection.getResponseCode();
                    if (200 == responseCode) {
                        InputStream inputStream = connection.getInputStream();
                        String updataInfo = StreamTools.getStringFromStream(inputStream);
                        Log.e(TAG,"zhaoxu updateInfo : " + updataInfo);
                        JSONObject jsonObject = new JSONObject(updataInfo);
                        String version = (String) jsonObject.get("version");
                        String description = (String) jsonObject.get("description");
                        String apkurl = (String) jsonObject.get("apkurl");
                        if (getVersionCode().equals(version)) {
                            message.what = ENTER_HOME;
                        } else {
                            Log.e(TAG,"zhaoxu 有新版本");
                            message.what = NEW_VERSION;
                        }
                    }
                } catch (Exception e) {
                    message.what = INTERNET_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long dTime = endTime - startTime;
                    if (dTime < 3000) {
                        try {
                            Thread.sleep(3000 - dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private String getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
