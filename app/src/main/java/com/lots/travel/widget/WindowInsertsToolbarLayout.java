package com.lots.travel.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by nalanzi on 2018/1/8.
 */
public class WindowInsertsToolbarLayout extends FrameLayout {

    public WindowInsertsToolbarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ViewCompat.setOnApplyWindowInsetsListener(this,
                new android.support.v4.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        int statusBarHeight = insets.getSystemWindowInsetTop();
                        setPadding(0,statusBarHeight,0,0);
                        return insets;
                    }
                });
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewCompat.requestApplyInsets(this);
    }

}
