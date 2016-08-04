package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Utils.SmsUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AToolsActivity extends Activity {

    ProgressDialog pb;
    SmsCallback smsCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atools_activity);
    }

    public void numberQuery(View view) {
        Intent intent = new Intent(this, NumberAddressActivity.class);
        startActivity(intent);
    }

    public void smsBackup(View view) {
        pb = new ProgressDialog(this);
        pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pb.setMessage("正在备份短信");
        pb.show();
        smsCallback = new SmsCallback();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SmsUtils.smsBackup(AToolsActivity.this,smsCallback);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AToolsActivity.this,"备份成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AToolsActivity.this,"备份失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                pb.dismiss();
            }
        }).start();
    }

    private class SmsCallback implements SmsUtils.SmsProgressCallback {

        @Override
        public void beforeBackup(int max) {
            pb.setMax(max);
        }

        @Override
        public void onBackup(int progress) {
            pb.setProgress(progress);
        }
    }
}
