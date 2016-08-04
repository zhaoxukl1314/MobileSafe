package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.R;

import org.w3c.dom.Text;

import java.util.Formatter;

public class AppManagerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        TextView internalStorage = (TextView) findViewById(R.id.internal_storage);
        TextView sdStorage = (TextView) findViewById(R.id.sd_storage);
        long availableInternal = getAvailableSize(Environment.getDataDirectory().getPath());
        long availableSD = getAvailableSize(Environment.getExternalStorageDirectory().getPath());
        internalStorage.setText("可用内存空间：" + android.text.format.Formatter.formatFileSize(this, availableInternal));
        sdStorage.setText("可用SD卡空间：" + android.text.format.Formatter.formatFileSize(this, availableSD));
    }

    private long getAvailableSize(String path) {
        StatFs statFs = new StatFs(path);
        long blockNumber = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        return blockNumber * blockSize;
    }
}
