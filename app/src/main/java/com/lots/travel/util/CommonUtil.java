package com.lots.travel.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lots.travel.R;
import com.lots.travel.TravelApplication;

/**
 * Created by nalanzi on 2018/1/15.
 */

public class CommonUtil {

    public static void showSoftInput(View view){
        Context context = view.getContext();
        InputMethodManager im = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view.getWindowToken()!=null)
            im.showSoftInput(view, 0);
    }

    public static void hideSoftInput(View view){
        Context context = view.getContext();
        InputMethodManager im = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view.getWindowToken()!=null)
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static int EXT_PADDING_TOP;
    public static int getStatusBarHeight(Context context){
        if (EXT_PADDING_TOP > 0)
            return EXT_PADDING_TOP;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            EXT_PADDING_TOP = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
            return DensityUtil.dp2px(context,25);
        }
        return EXT_PADDING_TOP;
    }

    public static int getSexDrawable(String sex){
        if(!TextUtils.isEmpty(sex)){
            if (sex.equals("1")){
                return R.drawable.ico_male;
            } else if(sex.equals("2")) {
                return R.drawable.ico_female;
            }
        }
        return -1;
    }

}
