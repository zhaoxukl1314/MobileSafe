package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Setup1Activity extends BaseSetupActivity {
    private static final String TAG = "Setup1Activity";

    @Override
    public void showNext() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_next_trans_in, R.anim.anim_next_trans_out);
    }

    @Override
    public void showPre() {
        //NOP
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup1_activity);
    }
}
