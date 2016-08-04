package com.example.zhaoxu.mobilesafe.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/6/28.
 */
public class SettingItemView extends RelativeLayout {

    private static final String TAG = "SettingItemView";
    private TextView tv_desc;
    private CheckBox checkBox;
    private String title;
    private String desc_on;
    private String desc_off;
    private TextView tv_title;

    public SettingItemView(Context context) {
        super(context);
        Log.e(TAG,"zhaoxu SettingItemView 0: " );
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.e(TAG,"zhaoxu SettingItemView 1: " );
        title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","text_title");
        desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","desc_on");
        desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","desc_off");
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG,"zhaoxu SettingItemView 2: " );
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.e(TAG,"zhaoxu SettingItemView 3: " );
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.setting_item_view,this);
        tv_desc = (TextView) this.findViewById(R.id.setting_item_tv_desc);
        checkBox = (CheckBox) this.findViewById(R.id.setting_item_cb);
        tv_title = (TextView) this.findViewById(R.id.setting_item_tv_title);
        Log.e(TAG,"zhaoxu tv_title : " + title);
        Log.e(TAG,"zhaoxu desc_on : " + desc_on);
        Log.e(TAG,"zhaoxu desc_off : " + desc_off);
        tv_title.setText(title);
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public void setChecked(boolean checked) {
        if (checked) {
            tv_desc.setText(desc_on);
        } else {
            tv_desc.setText(desc_off);
        }
        checkBox.setChecked(checked);
    }

    public void setDescText(String desc) {
        tv_desc.setText(desc);
    }

}
