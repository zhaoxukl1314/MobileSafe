package com.example.zhaoxu.mobilesafe.Engine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.zhaoxu.mobilesafe.Info.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class AppInfoProvider {

    public static List<AppInfo> getAppInfos(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (PackageInfo packageInfo : installedPackages) {
            AppInfo appInfo = new AppInfo();
            String packageName = packageInfo.packageName;
            String name = packageInfo.applicationInfo.loadLabel(pm).toString();
            Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
            appInfo.setPackageName(packageName);
            appInfo.setName(name);
            appInfo.setDrawable(drawable);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

}
