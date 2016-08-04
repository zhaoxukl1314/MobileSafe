package com.example.zhaoxu.mobilesafe.Info;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/8/3.
 */
public class AppInfo {
    private String packageName;
    private String name;
    private Drawable drawable;
    private boolean inRom;
    private boolean userApp;

    @Override
    public String toString() {
        return "AppInfo{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", drawable=" + drawable +
                ", inRom=" + inRom +
                ", userApp=" + userApp +
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

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }
}
