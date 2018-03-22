package com.lots.travel.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lots.travel.R;

/**
 * Created by lWX479187 on 2017/10/31.
 */
public class PopupDialog {
    private Dialog mDialog;

    public PopupDialog(Context context, View root){
        mDialog = new Dialog(context);
        mDialog.setContentView(root);

        onViewCreated();

        Window window = mDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.4f;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public PopupDialog(Context context, int layoutId){
        mDialog = new Dialog(context);
        mDialog.setContentView(layoutId);

        onViewCreated();

        Window window = mDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.4f;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onViewCreated(){}

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
