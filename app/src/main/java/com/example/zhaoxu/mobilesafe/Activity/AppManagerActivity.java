package com.example.zhaoxu.mobilesafe.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.Engine.AppInfoProvider;
import com.example.zhaoxu.mobilesafe.Info.AppInfo;
import com.example.zhaoxu.mobilesafe.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class AppManagerActivity extends Activity {

    private ListView app_list;
    private List<AppInfo> appinfos;
    private LinearLayout ll_loading;
    private MyAppListAdapter appListAdapter;
    private List<AppInfo> userInfos;
    private List<AppInfo> systemInfos;
    private PopupWindow popupWindow;

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
        userInfos = new ArrayList<>();
        systemInfos = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                appinfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
                for (AppInfo appInfo: appinfos) {
                    if (appInfo.isUserApp()) {
                        userInfos.add(appInfo);
                    } else {
                        systemInfos.add(appInfo);
                    }
                }
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.GONE);
                        appListAdapter = new MyAppListAdapter();
                        app_list.setAdapter(appListAdapter);
                        app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dismissPopupWindow();
                                View convertView = View.inflate(AppManagerActivity.this, R.layout.pop_window_item, null);
                                popupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                int[] location = new int[2];
                                view.getLocationInWindow(location);
                                popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, location[0], location[1]);
                            }
                        });
                        app_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                dismissPopupWindow();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
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
            return appinfos.size() + 2;
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
            View view;
            ViewHolder viewHolder;
            AppInfo appInfo;
            if (position == 0) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setText("用户程序: " + userInfos.size());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            } else if (position == (userInfos.size() + 1)) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setText("系统程序: " + systemInfos.size());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            } else if (position <= userInfos.size()) {
                int newPosition = position - 1;
                appInfo = userInfos.get(newPosition);
            } else {
                int newPosition = position - userInfos.size() - 2;
                appInfo = systemInfos.get(newPosition);
            }

            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                viewHolder = new ViewHolder();
                view = View.inflate(getApplicationContext(), R.layout.app_item_adapter, null);
                viewHolder.icon = (ImageView) view.findViewById(R.id.app_info_image);
                viewHolder.name = (TextView) view.findViewById(R.id.app_info_name);
                viewHolder.location = (TextView) view.findViewById(R.id.app_info_location);
                view.setTag(viewHolder);
            }
            viewHolder.icon.setImageDrawable(appInfo.getDrawable());
            viewHolder.name.setText(appInfo.getName());
            if (appInfo.isInRom()) {
                viewHolder.location.setText("手机内存");
            } else {
                viewHolder.location.setText("外部存储");
            }
            return view;
        }
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView location;
    }

    @Override
    protected void onDestroy() {
        dismissPopupWindow();
        super.onDestroy();
    }
}
