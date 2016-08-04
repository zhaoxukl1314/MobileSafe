package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.Database.NumberAddressDatabaseUtil;
import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/13.
 */
public class NumberAddressActivity extends Activity {

    private static final String TAG = "NumberAddressActivity";
    private EditText editText;
    private Button button;
    private TextView textView;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_address_activity);
        editText = (EditText) findViewById(R.id.number_address_et);
        button = (Button) findViewById(R.id.number_address_btn);
        textView = (TextView) findViewById(R.id.number_address_tv);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    public void query(View view) {
        String number = editText.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this,"号码为空",Toast.LENGTH_LONG).show();
            Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
            editText.startAnimation(shake);
            vibrator.vibrate(500);
            return;
        } else {
            Log.d(TAG,"查询号码为 : " + number);
            String address = NumberAddressDatabaseUtil.queryAddress(this,number);
            textView.setText("查询结果:" + address);
        }
    }
}
