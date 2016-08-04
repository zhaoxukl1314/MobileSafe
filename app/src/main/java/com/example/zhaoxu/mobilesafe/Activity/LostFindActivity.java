package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/4.
 */
public class LostFindActivity extends Activity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        boolean isAlreadySetup = sharedPreferences.getBoolean("alreadySetup",false);
        if (isAlreadySetup) {
            setContentView(R.layout.lost_find_activity);
            TextView phoneNumber = (TextView) findViewById(R.id.lostfind_text_phone);
            ImageView lock = (ImageView) findViewById(R.id.lostfind_image_lock);
            String phone = sharedPreferences.getString("safe_phone", "");
            boolean protecting = sharedPreferences.getBoolean("protecting", false);
            phoneNumber.setText(phone);
            if (protecting) {
                lock.setImageResource(R.drawable.lock);
            } else {
                lock.setImageResource(R.drawable.unlock);
            }
        } else {
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    public void reEnterSetup(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}
