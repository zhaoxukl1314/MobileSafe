package com.example.zhaoxu.mobilesafe.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.Engine.TaskInfoProvider;
import com.example.zhaoxu.mobilesafe.Info.TaskInfo;
import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Utils.ActivityTaskInfoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/11.
 */
public class AppTaskActivity extends Activity {

    private LinearLayout loading;
    private List<TaskInfo> taskInfos;
    private List<TaskInfo> userAppInfos;
    private List<TaskInfo> sysAppInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        TextView runningProcess = (TextView) findViewById(R.id.running_activity);
        TextView memInfo = (TextView) findViewById(R.id.ram);
        loading = (LinearLayout) findViewById(R.id.ll_loading);
        int runningProcessCount = ActivityTaskInfoUtils.getRunningTask(this);
        long availableMemory = ActivityTaskInfoUtils.getAvailableMemory(this);
        long totalMemory = ActivityTaskInfoUtils.getTotalMemory(this);
        runningProcess.setText("正在运行中的程序：" + runningProcessCount);
        memInfo.setText("剩余" + Formatter.formatFileSize(this,availableMemory) + "/" + "总内存：" + Formatter.formatFileSize(this, totalMemory));
        fillData();
    }

    private void fillData() {
        loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                userAppInfos = new ArrayList<TaskInfo>();
                sysAppInfos = new ArrayList<TaskInfo>();
                try {
                    taskInfos = TaskInfoProvider.getTaskInfos(AppTaskActivity.this);
                    for (TaskInfo taskInfo : taskInfos) {
                        if (taskInfo.isUserApp()) {
                            userAppInfos.add(taskInfo);
                        } else {
                            sysAppInfos.add(taskInfo);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
