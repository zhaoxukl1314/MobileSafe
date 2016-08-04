package com.example.zhaoxu.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.Database.NumberAddressDatabaseUtil;
import com.example.zhaoxu.mobilesafe.R;

public class AddressService extends Service {
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private OutCallingReceiver outCallingReceiver;
    private View view;
    private TextView textView;
    private SharedPreferences sharedPreferences;
    private WindowManager windowManager;

    public AddressService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        outCallingReceiver = new OutCallingReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(outCallingReceiver, filter);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        myPhoneStateListener = null;
        unregisterReceiver(outCallingReceiver);
        outCallingReceiver = null;
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = NumberAddressDatabaseUtil.queryAddress(AddressService.this, incomingNumber);
                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    hideMyToast();

                default:
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class OutCallingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resultData = getResultData();
            String address = NumberAddressDatabaseUtil.queryAddress(context, resultData);
//            Toast.makeText(context,address, Toast.LENGTH_LONG).show();
            showMyToast(address);
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

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;

        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x = 100;
        params.y = 100;

        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        windowManager.addView(view,params);
    }

    private void hideMyToast() {
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }
}
