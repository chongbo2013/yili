package com.lots.travel.widget.foreground;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by nalanzi on 2017/9/4.
 */

public class ForegroundImageView extends android.support.v7.widget.AppCompatImageView {
    private static final boolean NEED_COMPAT = Build.VERSION.SDK_INT<Build.VERSION_CODES.M;

    private Drawable mForeground;

    private boolean mForegroundBoundsChanged = false;

    public ForegroundImageView(Context context) {
        super(context,null);
    }

    public ForegroundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if(NEED_COMPAT){
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.foreground});
            Drawable d = a.getDrawable(0);
            if (d != null) {
                setForeground(d);
            }
            a.recycle();
        }

    }

    @Override
    public void setForeground(Drawable d){
        if(NEED_COMPAT){
            if(mForeground == d)
                return;

            if (mForeground != null) {
                mForeground.setCallback(null);
                unscheduleDrawable(mForeground);
            }

            mForeground = d;

            if (d != null) {
                d.setCallback(this);
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
            }

            requestLayout();
            invalidate();
        }else{
            super.setForeground(d);
        }
    }

    @Override
    public Drawable getForeground() {
        return NEED_COMPAT ? mForeground:super.getForeground();
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        boolean ret = super.verifyDrawable(who);
        return NEED_COMPAT ? (ret||(who==mForeground)):ret;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if(NEED_COMPAT && mForeground != null)
            mForeground.jumpToCurrentState();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (NEED_COMPAT && mForeground != null && mForeground.isStateful()) {
            mForeground.setState(getDrawableState());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mForegroundBoundsChanged = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mForegroundBoundsChanged = true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (mForeground != null) {
            final Drawable foreground = mForeground;

            if (mForegroundBoundsChanged) {
                mForegroundBoundsChanged = false;

                foreground.setBounds(0,0,getWidth(),getHeight());
            }

            foreground.draw(canvas);
        }
    }

}
