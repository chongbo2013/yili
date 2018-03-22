package com.lots.travel.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.lots.travel.TravelApplication;
import com.lots.travel.util.Constant;

/**
 * Created by nalanzi on 2017/9/7.
 */
//sp
public class SpStore {

    public static String getString(String name,String defaultValue){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA, Context.MODE_PRIVATE);
        return sp.getString(name,defaultValue);
    }

    public static boolean getBoolean(String name,boolean defaultValue){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA, Context.MODE_PRIVATE);
        return sp.getBoolean(name,defaultValue);
    }

    public static int getInt(String name,int defaultValue){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA, Context.MODE_PRIVATE);
        return sp.getInt(name,defaultValue);
    }

    public static long getLong(String name,long defaultValue){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA, Context.MODE_PRIVATE);
        return sp.getLong(name,defaultValue);
    }

    public static void set(String name, String value){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name,value);
        editor.commit();
    }

    public static void set(String name,boolean value){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name,value);
        editor.commit();
    }

    public static void set(String name,int value){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(name,value);
        editor.commit();
    }

    public static void set(String name,long value){
        SharedPreferences sp = TravelApplication.getInstance().getSharedPreferences(Constant.SOSONA,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(name,value);
        editor.commit();
    }

}
