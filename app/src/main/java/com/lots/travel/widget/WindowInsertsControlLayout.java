package com.lots.travel.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * Created by nalanzi on 2018/1/8.
 */
//重写dispatch方法，主页部分四个fragment都需要对status bar进行处理
//CollapsingToolbarLayout回将window inserts直接消耗掉，导致其它fragment的status bar出现异常
public class WindowInsertsControlLayout extends FrameLayout{

    public WindowInsertsControlLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsets ret = insets;
        if (!insets.isConsumed()) {
            final int count = getChildCount();
            WindowInsets copy = new WindowInsets(insets);
            for (int i = 0; i < count; i++) {
                copy = getChildAt(i).dispatchApplyWindowInsets(copy);
                if (copy.isConsumed()) {
                    ret = copy;
                    copy = new WindowInsets(insets);
                }
            }
        }
        return ret;
    }

}
