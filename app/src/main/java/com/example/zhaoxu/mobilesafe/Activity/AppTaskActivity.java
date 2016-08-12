package com.example.zhaoxu.mobilesafe.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Utils.ActivityTaskInfoUtils;

/**
 * Created by Administrator on 2016/8/11.
 */
public class AppTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        TextView runningProcess = (TextView) findViewById(R.id.running_activity);
        TextView memInfo = (TextView) findViewById(R.id.ram);
        int runningProcessCount = ActivityTaskInfoUtils.getRunningTask(this);
        long availableMemory = ActivityTaskInfoUtils.getAvailableMemory(this);
        long totalMemory = ActivityTaskInfoUtils.getTotalMemory(this);
        runningProcess.setText("正在运行中的程序：" + runningProcessCount);
        memInfo.setText("剩余" + Formatter.formatFileSize(this,availableMemory) + "/" + "总内存：" + Formatter.formatFileSize(this, totalMemory));
    }
}
