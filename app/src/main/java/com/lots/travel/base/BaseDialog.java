package com.lots.travel.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by lWX479187 on 2017/10/31.
 */
public class BaseDialog {
    private Dialog mDialog;

    public BaseDialog(Context context, int layout){
        this(context, layout,Gravity.CENTER);
    }

    public BaseDialog(Context context, int layout, int gravity){
        mDialog = new Dialog(context);
        mDialog.setContentView(layout);

        onViewCreated();

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.4f;

        onConfigAttributes(lp);
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onViewCreated(){}

    public void onConfigAttributes(WindowManager.LayoutParams lp){}

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }

    public Dialog getDialog(){
        return mDialog;
    }

}
