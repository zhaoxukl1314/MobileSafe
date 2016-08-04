package com.example.zhaoxu.mobilesafe.Utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ServiceUtil {
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServicesInfoes = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : runningServicesInfoes) {
            String name = info.service.getClassName();
            if (serviceName.equals(name)) {
                return true;
            }
        }
        return false;
    }

}
