package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhaoxu.mobilesafe.R;

/**
 * Created by Administrator on 2016/6/23.
 */
public class HomeActivity extends Activity {

    private myAdapter mAdapter;

    private static String[] functions = {
            "手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };

    private static int[] ids = {
            R.mipmap.comma_face_01, R.mipmap.comma_face_02, R.mipmap.comma_face_03,
            R.mipmap.comma_face_04, R.mipmap.comma_face_05, R.mipmap.comma_face_06,
            R.mipmap.comma_face_07, R.mipmap.comma_face_08, R.mipmap.comma_face_09
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        GridView gridView = (GridView) findViewById(R.id.grid_home);
        mAdapter = new myAdapter();
        gridView.setAdapter(mAdapter);
    }

    private class myAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return functions.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemView;
            if (view == null) {
                itemView = View.inflate(HomeActivity.this, R.layout.home_gridview_item, null);
            } else {
                itemView = view;
            }
            TextView textView = (TextView) itemView.findViewById(R.id.grid_item_text);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.grid_item_image);
            textView.setText(functions[i]);
            imageView.setImageResource(ids[i]);
            return itemView;
        }
    }

}
