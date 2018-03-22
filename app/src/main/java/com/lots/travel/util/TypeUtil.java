package com.lots.travel.util;

import android.text.TextUtils;
import android.util.Log;

import com.lots.travel.widget.images.Image;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nalanzi on 2017/11/4.
 */

public class TypeUtil {

    public static float str2float(String s){
        if(TextUtils.isEmpty(s))
            return 0f;

        float v = 0f;
        try{
            v = Float.parseFloat(s);
        }catch (NumberFormatException e){
            Log.e(TypeUtil.class.getName()+"str2float",e.toString());
        }

        return v;
    }

    public static int str2int(String s){
        if(TextUtils.isEmpty(s))
            return 0;

        int v = 0;
        try{
            v = Integer.parseInt(s);
        }catch (NumberFormatException e){
            Log.e(TypeUtil.class.getName()+"str2Int",e.toString());
        }

        return v;
    }

    public static String mergeStrings(String...strings){
        if(strings==null||strings.length==0)
            return null;

        StringBuilder strBuilder = new StringBuilder();
        for(int i=0;i<strings.length;i++){
            if(strings[i]!=null)
                strBuilder.append(strings[i]);
        }

        return strBuilder.toString();
    }

    public static String array2str(String[] strings){
        if(strings==null||strings.length==0)
            return null;

        StringBuilder strBuilder = new StringBuilder();
        int count = 0;
        for(int i=0;i<strings.length;i++){
            if(!TextUtils.isEmpty(strings[i])) {
                if(count!=0)
                    strBuilder.append(",");
                strBuilder.append(strings[i]);
                ++count;
            }
        }

        return strBuilder.toString();
    }

    public static String mergeString(String str1,String union,String str2){
        StringBuilder strBuilder = new StringBuilder();

        if(TextUtils.isEmpty(str1))
            return str2;
        strBuilder.append(str1);

        if(TextUtils.isEmpty(str2))
            return strBuilder.toString();

        if(!TextUtils.isEmpty(union))
            strBuilder.append(union);

        strBuilder.append(str2);

        return strBuilder.toString();
    }

    //uri
    public static String images2str(String str,List<Image> images,int maxCount){
        StringBuilder strBuilder = new StringBuilder();
        if(!TextUtils.isEmpty(str))
            strBuilder.append(str).append(",");

        maxCount = Math.max(0,maxCount);
        maxCount = Math.min(maxCount,images.size());

        for (int i=0;i<maxCount;i++){
            if(i!=0)
                strBuilder.append(",");
            Image image = images.get(i);
            strBuilder.append("file://").append(image.getPath());
        }
        return strBuilder.toString();
    }

    public static String images2str(List<String> images){
        StringBuilder strBuilder = new StringBuilder();
        for (int i=0;i<images.size();i++){
            if(i!=0)
                strBuilder.append(",");
            strBuilder.append(images.get(i));
        }
        return strBuilder.toString();
    }

    public static String[] str2arrays(String text){
        String[] frags = new String[0];
        if(TextUtils.isEmpty(text))
            return frags;

        frags = text.split(",");
        return frags;
    }

    public static String[] list2array(List<String> src){
        String[] strs = new String[src.size()];
        for (int i=0;i<src.size();i++){
            strs[i] = src.get(i);
        }
        return strs;
    }

}
