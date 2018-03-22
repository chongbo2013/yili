package com.lots.travel.network;

import com.lots.travel.AccountManager;
import com.lots.travel.TravelApplication;
import com.lots.travel.store.SpStore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nalanzi on 2017/9/11.
 */

public class Util {
    public static final char[] FIXS = new char[] {'<','>','(',')','|','_','{','}','*','!'};

    public static String generateSignature(long timestamp, int nonceStr, int fixIndex){
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = formatter.format(date);

        String str = Long.toString(timestamp)+FIXS[fixIndex]+Integer.toString(nonceStr)+FIXS[fixIndex]+ AccountManager.getInstance().getSecret()+FIXS[fixIndex]+dateStr;
        return encodeMD5(str);
    }

    private static String encodeMD5(String src){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = src.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static String trimStartSlash(String srcStr){
        int i;
        for(i=0;i<srcStr.length();i++){
            if ((srcStr.charAt(i)!='/'))
                break;
        }
        return srcStr.substring(i);
    }

}
