package com.example.zhaoxu.mobilesafe.Info;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/8/12.
 */
public class TaskInfo {
    private String packageName;
    private String name;
    private Drawable icon;
    private long meminfo;
    private boolean isUserApp;

    @Override
    public String toString() {
        return "TaskInfo{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", meminfo=" + meminfo +
                ", isUserApp=" + isUserApp +
                '}';
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMeminfo() {
        return meminfo;
    }

    public void setMeminfo(long meminfo) {
        this.meminfo = meminfo;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setUserApp(boolean userApp) {
        isUserApp = userApp;
    }
}
