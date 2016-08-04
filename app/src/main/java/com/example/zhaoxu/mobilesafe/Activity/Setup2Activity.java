package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.View.SettingItemView;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView settingItemView;

    @Override
    public void showNext() {
        String sim_id = sharedPreferences.getString("sim_id", null);
        if (TextUtils.isEmpty(sim_id)) {
            Toast.makeText(this,"没有绑定SIM卡",Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_next_trans_in, R.anim.anim_next_trans_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_pre_trans_in, R.anim.anim_pre_trans_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup2_activity);
        settingItemView = (SettingItemView) findViewById(R.id.setup2_sim_siv);
        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        boolean isChecked = sharedPreferences.getBoolean("sim_lock",false);
        settingItemView.setChecked(isChecked);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingItemView.isChecked()) {
                    settingItemView.setChecked(false);
                    editor.putString("sim_id", null);
                    editor.putBoolean("sim_lock", false);
                } else {
                    settingItemView.setChecked(true);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();
                    editor.putString("sim_id", simSerialNumber);
                    editor.putBoolean("sim_lock", true);
                }
                editor.commit();
            }
        });
    }
}
