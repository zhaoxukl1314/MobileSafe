package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText editText;

    @Override
    public void showNext() {
        String phone = editText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this,"安全号码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("safe_phone", phone);
        editor.commit();

        Intent intent = new Intent(this,Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_next_trans_in, R.anim.anim_next_trans_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_pre_trans_in, R.anim.anim_pre_trans_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup3_activity);
        editText = (EditText) findViewById(R.id.setup3_edit_phone);
        String phone = sharedPreferences.getString("safe_phone", "");
        editText.setText(phone);
    }

    public void selectContact(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String phone = data.getStringExtra("phone");
        editText.setText(phone);
    }
}
