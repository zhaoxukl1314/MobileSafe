package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.Database.BlackNumberDatabaseUtil;
import com.example.zhaoxu.mobilesafe.Info.BlackNumberInfo;
import com.example.zhaoxu.mobilesafe.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class CallSmsSafeActivity extends Activity {

    private BlackNumberDatabaseUtil blackNumberDatabaseUtil;
    private List<BlackNumberInfo> infos;
    private EditText etBlackDialog;
    private CheckBox cbBlackDialogNumber;
    private CheckBox cbBlackDialogSms;
    private Button btnBlackDialogOk;
    private Button btnBlackDialogCancel;
    private myListAdapter adapter;
    private ListView listView;
    private int offset = 0;
    private int limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_sms_safe_activity);
        listView = (ListView) findViewById(R.id.call_sms_list);
        blackNumberDatabaseUtil = new BlackNumberDatabaseUtil(this);

        fillData();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        int lastPosition = listView.getLastVisiblePosition();
                        if (lastPosition == infos.size() - 1) {
                            offset += limit;
                            fillData();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    private void fillData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (infos ==null) {
                    infos = blackNumberDatabaseUtil.querypart(limit,offset);
                } else {
                    infos.addAll(blackNumberDatabaseUtil.querypart(limit,offset));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter == null) {
                            adapter = new myListAdapter();
                            listView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }

    private class myListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v;
            ViewHolder holder;
            if (view == null) {
                v = View.inflate(getApplicationContext(),R.layout.call_sms_adapter,null);
                holder = new ViewHolder();
                holder.tv_number = (TextView) v.findViewById(R.id.call_sms_number);
                holder.tv_mode = (TextView) v.findViewById(R.id.call_sms_mode);
                holder.iv_delete = (ImageView) v.findViewById(R.id.call_sms_delete);
                v.setTag(holder);
            } else {
                v = view;
                holder = (ViewHolder) v.getTag();
            }
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("确定要删除吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            blackNumberDatabaseUtil.delete(infos.get(i).getNumber());
                            infos.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
            holder.tv_number.setText(infos.get(i).getNumber());
            String mode = infos.get(i).getMode();
            if ("0".equals(mode)) {
                holder.tv_mode.setText("电话拦截");
            } else if ("1".equals(mode)) {
                holder.tv_mode.setText("短信拦截");
            } else {
                holder.tv_mode.setText("全部拦截");
            }
            return v;
        }
    }

    private class ViewHolder {
        public TextView tv_number;
        public TextView tv_mode;
        public ImageView iv_delete;
    }

    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View contentView = View.inflate(this,R.layout.dialog_add_black_number,null);
        dialog.setView(contentView);
        dialog.show();
        etBlackDialog = (EditText) contentView.findViewById(R.id.black_number_dialog_edit);
        cbBlackDialogNumber = (CheckBox) contentView.findViewById(R.id.black_number_dialog_number);
        cbBlackDialogSms = (CheckBox) contentView.findViewById(R.id.black_number_dialog_sms);
        btnBlackDialogOk = (Button) contentView.findViewById(R.id.black_number_dialog_ok);
        btnBlackDialogCancel = (Button) contentView.findViewById(R.id.black_number_dialog_cancel);
        btnBlackDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnBlackDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = etBlackDialog.getText().toString().trim();
                String mode = "2";
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(CallSmsSafeActivity.this,"号码为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cbBlackDialogNumber.isChecked() && cbBlackDialogSms.isChecked()) {
                    mode = "2";
                } else if (cbBlackDialogNumber.isChecked()) {
                    mode = "0";
                } else if (cbBlackDialogSms.isChecked()) {
                    mode = "1";
                } else {
                    Toast.makeText(CallSmsSafeActivity.this,"未选择拦截模式", Toast.LENGTH_SHORT).show();
                    return;
                }
                blackNumberDatabaseUtil.insert(number,mode);
                BlackNumberInfo info = new BlackNumberInfo();
                info.setMode(mode);
                info.setNumber(number);
                infos.add(0,info);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

    }
}
