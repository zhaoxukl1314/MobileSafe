package com.example.zhaoxu.mobilesafe.Service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.zhaoxu.mobilesafe.Activity.CallSmsSafeActivity;
import com.example.zhaoxu.mobilesafe.Database.BlackNumberDatabaseUtil;
import com.example.zhaoxu.mobilesafe.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallSmsSafeService extends Service {

    private CallSmsReceiver callSmsReceiver;
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener listener;
    private BlackNumberDatabaseUtil databaseUtil;
    private MyObserver observer;

    public CallSmsSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class CallSmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String address = smsMessage.getOriginatingAddress();
                String mode = databaseUtil.queryMode(address);
                if (TextUtils.isEmpty(mode)) {
                    if ("1".equals(mode) || "2".equals(mode)) {
                        abortBroadcast();
                    }
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseUtil = new BlackNumberDatabaseUtil(CallSmsSafeService.this);
        callSmsReceiver = new CallSmsReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(callSmsReceiver, intentFilter);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callSmsReceiver);
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
        callSmsReceiver = null;
        listener = null;
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode = databaseUtil.queryMode(incomingNumber);
                    if ("0".equals(mode) || "2".equals(mode)) {
                        Uri uri = CallLog.Calls.CONTENT_URI;
                        observer = new MyObserver(new Handler(), incomingNumber);
                        getContentResolver().registerContentObserver(uri,true, observer);
                        endCall();
                    }
                    break;

                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private class MyObserver extends ContentObserver {
        private String mIncomingNumber;

        public MyObserver(Handler handler, String incomingNumber) {
            super(handler);
            mIncomingNumber = incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            deleteCallLog(mIncomingNumber);
            getContentResolver().unregisterContentObserver(this);
            super.onChange(selfChange);
        }
    }

    private void deleteCallLog(String incomingNumber) {
        ContentResolver resolver = getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        resolver.delete(uri, "number=?", new String[]{incomingNumber});
    }

    private void endCall() {
        try {
            Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, "TELEPHONY_SERVICE");
            ITelephony.Stub.asInterface(iBinder).endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
