package com.example.zhaoxu.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Service.LocationService;

import java.util.Objects;

/**
 * Created by Administrator on 2016/7/12.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    private static final String LOCATION = "#*location*#";

    private static final String ALARM = "#*alarm*#";

    private static final String WIPEDATA = "#*wipedata*#";

    private static final String LOCKSCREEN = "#*lockscreen*#";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object object : objs) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = message.getOriginatingAddress();
            String messageBody = message.getMessageBody();
            switch (messageBody) {
                case LOCATION:
                    Log.d(TAG, "onReceive: #*location*#");
                    Intent i = new Intent(context, LocationService.class);
                    context.startService(i);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
                    String location = sharedPreferences.getString("location","");
                    if (TextUtils.isEmpty(location)) {
                        SmsManager.getDefault().sendTextMessage(originatingAddress,null,"getting location",null,null);
                    } else {
                        SmsManager.getDefault().sendTextMessage(originatingAddress,null,location,null,null);
                    }
                    abortBroadcast();
                    break;

                case ALARM:
                    Log.d(TAG, "onReceive: #*alarm*#");
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setLooping(false);
                    player.setVolume(1.0f, 1.0f);
                    player.start();
                    abortBroadcast();
                    break;

                case WIPEDATA:
                    Log.d(TAG, "onReceive: #*wipedata*#");
                    abortBroadcast();
                    break;

                case LOCKSCREEN:
                    Log.d(TAG, "onReceive: #*lockscreen*#");
                    abortBroadcast();
                    break;

                default:
                    break;
            }
        }
    }
}
