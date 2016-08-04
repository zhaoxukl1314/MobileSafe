package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Utils.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                    showUpdateDialog();
                    break;

                case INTERNET_ERROR:
                    Toast.makeText(SplashActivity.this,"网络错误",Toast.LENGTH_LONG);
                    enterHome();
                    break;
            }
        }
    };
    private String description;
    private String apkurl;
    private String path;
    private TextView downLoadTextView;

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("升级窗口");
        builder.setMessage(description);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                enterHome();
            }
        });
        builder.setPositiveButton("确定升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG,"点击确定升级");
                FinalHttp finalHttp = new FinalHttp();
                path = "storage/emulated/0/mobilesafe3.0";
                finalHttp.download(apkurl, path,new ajaxCallBack());
            }
        });
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

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
        SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        boolean update = sharedPreferences.getBoolean("update",false);
        if (update) {
            checkVersion();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();
                }
            },2000);
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f,1.0f);
        alphaAnimation.setDuration(1500);
        findViewById(R.id.rl_splash).startAnimation(alphaAnimation);
        downLoadTextView = (TextView) findViewById(R.id.down_load_progress_tv);
        copyDB();
    }

    private void copyDB() {
        try {
            File file = new File(getFilesDir(),"address.db");
            Log.e(TAG,"zhaoxu DB file : " + file);
            if (file.exists() && file.length() > 0) {
                Log.e(TAG,"号码查询地址数据库已存在");
                return;
            }
            InputStream open = getAssets().open("address.db");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = open.read(buffer)) != -1) {
                fileOutputStream.write(buffer,0,len);
            }
            fileOutputStream.close();
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        description = (String) jsonObject.get("description");
                        apkurl = (String) jsonObject.get("apkurl");
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

    private class ajaxCallBack extends AjaxCallBack<File> {
        @Override
        public void onStart() {
            super.onStart();
            Log.e(TAG,"下载开始");
        }

        @Override
        public void onLoading(long count, long current) {
            super.onLoading(count, current);
            Log.e(TAG,"下载中");
            int progress = (int) (current * 100 / count);
            downLoadTextView.setVisibility(View.VISIBLE);
            downLoadTextView.setText("下载进度：" + progress + "%");
        }

        @Override
        public void onSuccess(File file) {
            super.onSuccess(file);
            Log.e(TAG,"下载成功");
            installAPK(file);
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            Log.e(TAG,"下载失败 : " + errorNo + strMsg);
            Toast.makeText(SplashActivity.this,"下载失败",Toast.LENGTH_LONG);
        }
    }

    private void installAPK(File file) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
    }
}
