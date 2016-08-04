package com.example.zhaoxu.mobilesafe.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/6/30.
 */
public class MD5Utils {
    public static String md5Password(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(password.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b: digest) {
                int a = b & 0xff;
                String s = Integer.toHexString(a);
                if (s.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(s);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
