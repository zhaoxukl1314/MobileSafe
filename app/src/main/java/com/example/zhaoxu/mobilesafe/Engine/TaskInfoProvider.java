package com.example.zhaoxu.mobilesafe.Engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.zhaoxu.mobilesafe.Info.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class TaskInfoProvider {
    public static List<TaskInfo> getTaskInfos(Context context) throws PackageManager.NameNotFoundException {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            String processName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(processName);
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(processName, 0);
            Drawable drawable = applicationInfo.loadIcon(packageManager);
            String name = applicationInfo.loadLabel(packageManager).toString();
            taskInfo.setName(name);
            taskInfo.setIcon(drawable);
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                taskInfo.setUserApp(true);
            } else {
                taskInfo.setUserApp(false);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
