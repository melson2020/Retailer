package com.melson.base.utils;

import java.security.MessageDigest;

/**
 * Created by Jacky on 2015/3/20.
 */
public class MD5Util {
    public static String string2MD5(String origin) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(origin.getBytes("utf8"));
            byte[] result = md.digest();
            for (int i = 0; i < result.length; i++) {
                //int val = result[i] & 0xff;
                //sb.append(Integer.toHexString(val));
                int val = (result[i] & 0x000000ff) | 0xffffff00;
                sb.append(Integer.toHexString(val).substring(6));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
