package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Setup4Activity extends BaseSetupActivity {

    @Override
    public void showNext() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("alreadySetup", true);
        edit.commit();
        Intent intent = new Intent(this,LostFindActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_next_trans_in, R.anim.anim_next_trans_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_pre_trans_in, R.anim.anim_pre_trans_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup4_activity);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.setup4_checkbox);
        boolean protecting = sharedPreferences.getBoolean("protecting", false);
        if (protecting) {
            checkBox.setText("你已经开启防盗保护");
        } else {
            checkBox.setText("你没有开启防盗保护");
        }
        checkBox.setChecked(protecting);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBox.setText("你已经开启防盗保护");
                } else {
                    checkBox.setText("你没有开启防盗保护");
                }
                checkBox.setChecked(b);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("protecting", b);
                editor.commit();
            }
        });
    }
}
