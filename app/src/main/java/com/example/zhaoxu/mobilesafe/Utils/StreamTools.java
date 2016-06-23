package com.example.zhaoxu.mobilesafe.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/6/23.
 */
public class StreamTools {
    public static String getStringFromStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer,0,len);
        }
        String updateInfo = new String(byteArrayOutputStream.toByteArray(),"gbk");
        return updateInfo;
    }
}
