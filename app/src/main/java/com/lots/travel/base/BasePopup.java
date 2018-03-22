package com.lots.travel.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by nalanzi on 2018/1/29.
 */
//弹出时修改Window的alpha
public abstract class BasePopup extends PopupWindow {
    private Window mWindow;
    private float mAlpha;

    public BasePopup(Activity context){
        this(context,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,0.4f);
    }

    public BasePopup(Activity context,int width,int height,float alpha){
        super(width, height);
        mWindow = context.getWindow();
        mAlpha = alpha;
        //加载内容
        int layoutId = getLayoutId();
        View root = LayoutInflater.from(context).inflate(layoutId,null);
        onCreate(context,root);
        setContentView(root);
        //默认设置
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);
    }

    public abstract int getLayoutId();

    public abstract void onCreate(Context context,View root);

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = mAlpha;
        mWindow.setAttributes(lp);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = mAlpha;
        mWindow.setAttributes(lp);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void dismiss() {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = 1.0f;
        mWindow.setAttributes(lp);

        super.dismiss();
    }

}
