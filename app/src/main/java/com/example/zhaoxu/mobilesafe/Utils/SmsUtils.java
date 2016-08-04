package com.example.zhaoxu.mobilesafe.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/1.
 */
public class SmsUtils {

    public interface SmsProgressCallback {
        public void beforeBackup(int max);

        public void onBackup(int progress);
    }

    public static void smsBackup(Context context, SmsProgressCallback callback) throws IOException {
        File file = new File(context.getFilesDir(),"backup.xml");
        FileOutputStream fos = new FileOutputStream(file);
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[]{"body", "address", "type", "date"}, null, null, null);
        XmlSerializer xmlSerializer = Xml.newSerializer();
        xmlSerializer.setOutput(fos, "utf-8");
        xmlSerializer.startDocument("utf-8",true);
        xmlSerializer.startTag(null,"smss");
        int max = cursor.getCount();
        callback.beforeBackup(max);
        int progress = 0;

        while (cursor.moveToNext()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String body = cursor.getString(0);
            String address = cursor.getString(1);
            String type = cursor.getString(2);
            String date = cursor.getString(3);
            xmlSerializer.startTag(null, "sms");

            xmlSerializer.startTag(null, "body");
            xmlSerializer.text(body);
            xmlSerializer.endTag(null, "body");

            xmlSerializer.startTag(null, "address");
            xmlSerializer.text(address);
            xmlSerializer.endTag(null, "address");

            xmlSerializer.startTag(null, "type");
            xmlSerializer.text(type);
            xmlSerializer.endTag(null, "type");

            xmlSerializer.startTag(null, "date");
            xmlSerializer.text(date);
            xmlSerializer.endTag(null, "date");

            xmlSerializer.endTag(null, "sms");
            progress++;
            callback.onBackup(progress);
        }
        xmlSerializer.endTag(null,"smss");
        xmlSerializer.endDocument();
        cursor.close();
    }
}
