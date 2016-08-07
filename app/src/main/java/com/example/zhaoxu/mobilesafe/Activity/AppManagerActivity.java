package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.Engine.AppInfoProvider;
import com.example.zhaoxu.mobilesafe.Info.AppInfo;
import com.example.zhaoxu.mobilesafe.R;

import org.w3c.dom.Text;

import java.util.Formatter;
import java.util.List;

public class AppManagerActivity extends Activity {

    private ListView app_list;
    private List<AppInfo> appinfos;
    private LinearLayout ll_loading;
    private MyAppListAdapter appListAdapter;

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
        app_list = (ListView) findViewById(R.id.list_app_info);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_loading.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                appinfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.GONE);
                        appListAdapter = new MyAppListAdapter();
                        app_list.setAdapter(appListAdapter);
                    }
                });
            }
        }).start();
    }

    private long getAvailableSize(String path) {
        StatFs statFs = new StatFs(path);
        long blockNumber = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        return blockNumber * blockSize;
    }

    private class MyAppListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return appinfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(AppManagerActivity.this);
            textView.setText(appinfos.get(position).toString());
            return textView;
        }
    }
}
