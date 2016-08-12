package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoxu.mobilesafe.R;
import com.example.zhaoxu.mobilesafe.Utils.MD5Utils;

/**
 * Created by Administrator on 2016/6/23.
 */
public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";
    private myAdapter mAdapter;

    private SharedPreferences sharedPreferences;

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
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        GridView gridView = (GridView) findViewById(R.id.grid_home);
        mAdapter = new myAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"触发点击事件：" + i);
                Intent intent;
                switch (i) {
                    case 0:
                        showPasswordDialog();
                        break;

                    case 1:
                        intent = new Intent(HomeActivity.this,CallSmsSafeActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(HomeActivity.this, AppManagerActivity.class);
                        startActivity(intent);
                        break;

                    case 3:
                        intent = new Intent(HomeActivity.this, AppTaskActivity.class);
                        startActivity(intent);
                        break;

                    case 7:
                        intent = new Intent(HomeActivity.this, AToolsActivity.class);
                        startActivity(intent);
                        break;

                    case 8:
                        intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void showPasswordDialog() {
        boolean isPasswordAlreadySet = isPasswordSet();
        if (isPasswordAlreadySet) {
            showEnterDialog();
        } else {
            showSetPasswordDialog();
        }
    }

    private void showEnterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogEnterView = View.inflate(this, R.layout.dialog_enter_password, null);
        builder.setView(dialogEnterView);
        final AlertDialog enterDialog = builder.show();
        final EditText enterPasswordEdit = (EditText) dialogEnterView.findViewById(R.id.et_enter_password);
        Button okButton = (Button) dialogEnterView.findViewById(R.id.btn_ok);
        Button cancelButton = (Button) dialogEnterView.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterDialog.dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterString = enterPasswordEdit.getText().toString().trim();
                String password = sharedPreferences.getString("password",null);
                if (TextUtils.isEmpty(enterString)) {
                    Toast.makeText(HomeActivity.this,"请输入密码", Toast.LENGTH_LONG).show();
                }
                if (MD5Utils.md5Password(enterString).equals(password)) {
                    enterDialog.dismiss();
                    Log.e(TAG,"进入手机防盗界面");
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                    enterPasswordEdit.setText("");
                }
            }
        });
    }

    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_set_password, null);
        final EditText setPasswordEditText = (EditText) dialogView.findViewById(R.id.et_password);
        final EditText confirmPasswordEditText = (EditText) dialogView.findViewById(R.id.et_password_confirm);
        Button okButton = (Button) dialogView.findViewById(R.id.btn_ok);
        Button cancelButton = (Button) dialogView.findViewById(R.id.btn_cancel);
        builder.setView(dialogView);
        final AlertDialog setPasswordDialog = builder.show();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordDialog.dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordSet = setPasswordEditText.getText().toString().trim();
                String passwordConfirm = confirmPasswordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(passwordSet) || TextUtils.isEmpty(passwordConfirm)) {
                    Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
                    setPasswordDialog.dismiss();
                    return;
                }

                if (passwordConfirm.equals(passwordSet)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password",MD5Utils.md5Password(passwordConfirm));
                    editor.commit();
                    setPasswordDialog.dismiss();
                    Log.e(TAG,"进入手机防盗界面");
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this,"密码不相等，请重新输入",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isPasswordSet() {
        return !TextUtils.isEmpty(sharedPreferences.getString("password",null));
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
