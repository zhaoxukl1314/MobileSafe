package com.example.zhaoxu.mobilesafe.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zhaoxu.mobilesafe.Info.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class BlackNumberDatabaseUtil {

    private static final String TAG = "BlackNumberDatabaseUtil";
    private final BlackNumberDatabase blackNumberDatabase;

    public BlackNumberDatabaseUtil(Context context) {
        blackNumberDatabase = new BlackNumberDatabase(context);
    }

    public boolean query(String number) {
        boolean result = false;
        SQLiteDatabase sqLiteDatabase = blackNumberDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("blacknumber", null, "number = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    public String queryMode(String number) {
        String result = null;
        SQLiteDatabase sqLiteDatabase = blackNumberDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("blacknumber", new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    public List<BlackNumberInfo> queryAll() {
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase sqLiteDatabase = blackNumberDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("blacknumber", new String[]{"number","mode"}, null, null, null, null, "_id desc");
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            info.setNumber(number);
            info.setMode(mode);
            result.add(info);
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    public List<BlackNumberInfo> querypart(int limit, int offset) {
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase sqLiteDatabase = blackNumberDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select number, mode from blacknumber order by _id desc limit ? offset ?", new String[]{String.valueOf(limit), String.valueOf(offset)});
        Log.e(TAG,"zhaoxu cursor count : " + cursor.getCount());
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            info.setNumber(number);
            info.setMode(mode);
            result.add(info);
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    public void insert(String number, String mode) {
        SQLiteDatabase writableDatabase = blackNumberDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        writableDatabase.insert("blacknumber", null, values);
        writableDatabase.close();
    }

    public void update(String number, String mode) {
        SQLiteDatabase sqLiteDatabase = blackNumberDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        sqLiteDatabase.update("blacknumber",values,"number = ?", new String[]{number});
        sqLiteDatabase.close();
    }

    public void delete(String number) {
        SQLiteDatabase sqLiteDatabase = blackNumberDatabase.getWritableDatabase();
        sqLiteDatabase.delete("blacknumber","number = ?", new String[]{number});
        sqLiteDatabase.close();
    }
}
