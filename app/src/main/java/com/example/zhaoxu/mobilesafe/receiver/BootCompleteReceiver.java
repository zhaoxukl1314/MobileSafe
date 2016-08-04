package com.example.zhaoxu.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/7.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean protecting = sharedPreferences.getBoolean("protecting", false);
        if (protecting) {
            String safeSimSerial = sharedPreferences.getString("sim_id","");
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            if (simSerialNumber.equals(safeSimSerial)) {
                Toast.makeText(context,"同一个SIM卡",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context,"不同的SIM卡",Toast.LENGTH_LONG).show();
                SmsManager.getDefault().sendTextMessage(sharedPreferences.getString("safe_phone",""), null, "SIM changing", null, null);
            }
        }
    }
}
