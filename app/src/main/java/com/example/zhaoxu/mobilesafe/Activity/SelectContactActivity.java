package com.example.zhaoxu.mobilesafe.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.zhaoxu.mobilesafe.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/11.
 */
public class SelectContactActivity extends Activity {
    private static final String TAG = "SelectContactActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contact_activity);
        ListView contactList = (ListView) findViewById(R.id.select_contact_adapter);
        final List<Map<String, String>> data = getContactInfo();
        contactList.setAdapter(new SimpleAdapter(this,data,R.layout.contact_item_view,new String[]{"name", "phone"}, new int[]{R.id.list_item_name, R.id.list_item_phone}));
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String phone = data.get(i).get("phone");
                Intent data = new Intent();
                data.putExtra("phone",phone);
                setResult(0,data);
                finish();
            }
        });
    }

    private List<Map<String, String>> getContactInfo() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        ContentResolver resolver = getContentResolver();
        Uri contactUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri contactData = Uri.parse("content://com.android.contacts/data");
        Cursor contactCursor = resolver.query(contactUri, new String[]{"contact_id"}, null, null, null);
        while(contactCursor.moveToNext()) {
            String contact_id = contactCursor.getString(0);
            if (contact_id != null) {
                Map<String, String> map = new HashMap<>();
                Cursor dataCursor = resolver.query(contactData,new String[]{"data1", "mimetype"}, "contact_id = ?", new String[]{contact_id}, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    Log.e(TAG,"zhaoxu data1: " + data1);
                    Log.e(TAG,"zhaoxu mimetype: " + mimetype);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        map.put("name", data1);
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                        map.put("phone", data1);
                    }
                }
                dataCursor.close();
                list.add(map);
            }
        }
        contactCursor.close();
        return list;
    }
}
