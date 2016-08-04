package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Service.AddressService;
import com.example.zhaoxu.mobilesafe.Service.CallSmsSafeService;
import com.example.zhaoxu.mobilesafe.Utils.ServiceUtil;
import com.example.zhaoxu.mobilesafe.View.SettingClickView;
import com.example.zhaoxu.mobilesafe.View.SettingItemView;

/**
 * Created by Administrator on 2016/6/28.
 */
public class SettingActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private Intent addressIntent;
    private SettingItemView settingItemView_address;
    private TextView textView;
    private WindowManager windowManager;
    private View view;
    private SettingClickView settingClickView;
    private String[] toast_background = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
    private WindowManager.LayoutParams params;
    private long[] mHits = new long[2];;
    private SettingItemView setting_siv_callsms;
    private Intent callSmsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        final SettingItemView settingItemView = (SettingItemView) findViewById(R.id.setting_siv);
        settingItemView_address = (SettingItemView) findViewById(R.id.setting_siv_address);
        settingClickView = (SettingClickView) findViewById(R.id.setting_click_toast);
        boolean update = sharedPreferences.getBoolean("update",false);
        addressIntent = new Intent(this, AddressService.class);
        settingItemView.setChecked(update);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        boolean isServiceRunning = ServiceUtil.isServiceRunning(this,"com.example.zhaoxu.mobilesafe.Service.AddressService");

        if (isServiceRunning) {
            settingItemView_address.setChecked(true);
        } else {
            settingItemView_address.setChecked(false);
        }

        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                if (settingItemView.isChecked()) {
                    settingItemView.setChecked(false);
                    edit.putBoolean("update",false);
                } else {
                    settingItemView.setChecked(true);
                    edit.putBoolean("update",true);
                }
                edit.commit();
            }
        });

        settingItemView_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingItemView_address.isChecked()) {
                    settingItemView_address.setChecked(false);
                    stopService(addressIntent);
//                    Toast.makeText(SettingActivity.this,"归属地查询关闭", Toast.LENGTH_LONG).show();
                    showMyToast("归属地查询关闭");
                } else {
                    settingItemView_address.setChecked(true);
                    startService(addressIntent);
                    Toast.makeText(SettingActivity.this,"归属地查询开启", Toast.LENGTH_LONG).show();
                    hideMyToast();
                }
            }
        });

        settingClickView.setTitle("归属地弹出框背景设置");
        int toast_choice = sharedPreferences.getInt("toast_background", 0);
        settingClickView.setDescText(toast_background[toast_choice]);
        settingClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int choice = sharedPreferences.getInt("toast_background", 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("归属地弹出框背景设置");
                builder.setSingleChoiceItems(toast_background, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putInt("toast_background", i);
                        edit.commit();
                        settingClickView.setDescText(toast_background[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        setting_siv_callsms = (SettingItemView) findViewById(R.id.setting_siv_callsms);
        callSmsIntent = new Intent(this, CallSmsSafeService.class);
        boolean isCallSmsServiceRunning = ServiceUtil.isServiceRunning(this,"com.example.zhaoxu.mobilesafe.Service.CallSmsSafeService");
        if (isCallSmsServiceRunning) {
            setting_siv_callsms.setChecked(true);
        } else {
            setting_siv_callsms.setChecked(false);
        }
        setting_siv_callsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setting_siv_callsms.isChecked()) {
                    setting_siv_callsms.setChecked(false);
                    stopService(callSmsIntent);
                } else {
                    setting_siv_callsms.setChecked(true);
                    startService(callSmsIntent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isServiceRunning = ServiceUtil.isServiceRunning(this,"com.example.zhaoxu.mobilesafe.Service.AddressService");
        if (isServiceRunning) {
            settingItemView_address.setChecked(true);
        } else {
            settingItemView_address.setChecked(false);
        }
        boolean isCallSmsServiceRunning = ServiceUtil.isServiceRunning(this,"com.example.zhaoxu.mobilesafe.Service.CallSmsSafeService");
        if (isCallSmsServiceRunning) {
            setting_siv_callsms.setChecked(true);
        } else {
            setting_siv_callsms.setChecked(false);
        }
    }

    private void showMyToast(String text) {
        view = View.inflate(this, R.layout.address_toast,null);
        int[] ids = {R.mipmap.call_locate_white, R.mipmap.call_locate_orange, R.mipmap.call_locate_blue,
                R.mipmap.call_locate_gray,R.mipmap.call_locate_green};
        view.setBackgroundResource(ids[sharedPreferences.getInt("toast_background",0)]);
        textView = (TextView) view.findViewById(R.id.address_toast_textView);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    params.x = (windowManager.getDefaultDisplay().getWidth() - view.getWidth()) / 2;
                    windowManager.updateViewLayout(view,params);
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) motionEvent.getRawX();
                        int moveY = (int) motionEvent.getRawY();
                        int dx = moveX - startX;
                        int dy = moveY - startY;

                        startX = moveX;
                        startY = moveY;

                        params.x += dx;
                        params.y += dy;

                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > (windowManager.getDefaultDisplay().getWidth() - view.getWidth())){
                            params.x = windowManager.getDefaultDisplay().getWidth() - view.getWidth();
                        }
                        if (params.y > (windowManager.getDefaultDisplay().getHeight() - view.getHeight())) {
                            params.y = windowManager.getDefaultDisplay().getHeight() - view.getHeight();
                        }
                        windowManager.updateViewLayout(view,params);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;

        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x = 100;
        params.y = 100;

        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        windowManager.addView(view, params);
    }

    private void hideMyToast() {
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }
}
