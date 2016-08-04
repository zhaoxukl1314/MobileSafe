package com.example.zhaoxu.mobilesafe.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/6/28.
 */
public class SettingClickView extends RelativeLayout {

    private static final String TAG = "SettingClickView";
    private TextView tv_desc;
    private ImageView imageView;
    private String title;
    private String desc_on;
    private String desc_off;
    private TextView tv_title;

    public SettingClickView(Context context) {
        super(context);
        Log.e(TAG,"zhaoxu SettingClickView 0: " );
        initView(context);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.e(TAG,"zhaoxu SettingClickView 1: " );
        title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","text_title");
        desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","desc_on");
        desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","desc_off");
        initView(context);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG,"zhaoxu SettingClickView 2: " );
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.e(TAG,"zhaoxu SettingClickView 3: " );
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.setting_click_view,this);
        tv_desc = (TextView) this.findViewById(R.id.setting_click_tv_desc);
        imageView = (ImageView) this.findViewById(R.id.setting_click_image);
        tv_title = (TextView) this.findViewById(R.id.setting_click_tv_title);
    }

    public void setChecked(boolean checked) {
        if (checked) {
            tv_desc.setText(desc_on);
        } else {
            tv_desc.setText(desc_off);
        }
    }

    public void setDescText(String desc) {
        tv_desc.setText(desc);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

}
