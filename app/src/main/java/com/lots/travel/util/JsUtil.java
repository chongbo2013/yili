package com.lots.travel.util;

import android.content.Context;
import android.util.Log;

import com.lots.travel.AccountManager;
import com.lots.travel.network.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nalanzi on 2017/11/30.
 */

public class JsUtil {

    //POST请求，将signature放在json中
    public static void addSignature(JSONObject json) throws JSONException {
        long timestamp = System.currentTimeMillis();
        int nonce = (int)((Math.random()*9+1)*100000);
        int fixIndex = (int) (Math.random()* Util.FIXS.length);

        json.put("from", "android");
        json.put("userId", Long.toString(AccountManager.getInstance().getUserId()));
        json.put("timestamp", Long.toString(timestamp));
        json.put("nonceStr", Integer.toString(nonce));
        json.put("fix", Integer.toString(fixIndex));
        json.put("signature", Util.generateSignature(timestamp,nonce,fixIndex));
    }

    public static String addSignature(String segment){
        long timestamp = System.currentTimeMillis();
        int nonce = (int)((Math.random()*9+1)*100000);
        int fixIndex = (int) (Math.random()* Util.FIXS.length);
        return segment + "&from=" + "android" +
                "&userId=" + AccountManager.getInstance().getUserId() +
                "&timestamp=" + timestamp +
                "&nonceStr=" + nonce +
                "&fix=" + fixIndex +
                "&signature=" + Util.generateSignature(timestamp, nonce, fixIndex);
    }

    //从assets加载js文件
    public static String loadJs(Context context,String name){
        String jsBrideCodeStr = "";

        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            in = context.getAssets().open(name);
            if(in==null)
                return jsBrideCodeStr;
            StringBuilder content = new StringBuilder(); // 文件内容字符串
            reader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(reader);
            char[] buffer = new char[2048];
            int length = -1;
            // 分行读取
            while ((length = bufferedReader.read(buffer)) > 0) {
                content.append(buffer, 0, length);
            }
            jsBrideCodeStr = content.toString();
        } catch (java.io.FileNotFoundException e) {
            Log.e(JsUtil.class.getName(), "The File doesn't not exist.");
        } catch (IOException e) {
            Log.e(JsUtil.class.getName(), e.getMessage());
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in!=null)
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(JsUtil.class.getName(), e.getMessage());
                }
        }

        // 多行注释
        String multiComment = "/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/";
        // 单行注释
        String singleComment = "//[^\r\n]*+";
        Pattern p = Pattern.compile(multiComment + "|" + singleComment + "|" + "\t|\r|\n");
        Matcher m = p.matcher(jsBrideCodeStr);
        jsBrideCodeStr = m.replaceAll("");
        jsBrideCodeStr = "javascript:function onCoreBridgeJS(){"+ jsBrideCodeStr +"}; onCoreBridgeJS();";

        return jsBrideCodeStr;
    }

}
