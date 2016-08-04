package com.example.zhaoxu.mobilesafe.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/14.
 */
public class NumberAddressDatabaseUtil {
    private static final String TAG = "NumberAddressDatabase";

    public static String queryAddress(Context context, String number) {
        String path = "/data/data/com.example.zhaoxu.mobilesafe/files/address.db";
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        String address = null;
        if (number.matches("^1[34568]\\d{9}$")) {
            Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)", new String[]{number.substring(0, 7)});
            Log.e(TAG,"zhaoxu cursor : " + cursor);
            while(cursor.moveToNext()){
                address = cursor.getString(0);
            }
            cursor.close();
        } else {
            Toast.makeText(context,"数据库中无此号码",Toast.LENGTH_LONG).show();
        }
        return address;
    }
}
