package com.lots.travel.widget.foreground;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by nalanzi on 2017/9/4.
 */

public class ForegroundTextView extends android.support.v7.widget.AppCompatTextView {
    private static final boolean NEED_COMPAT = Build.VERSION.SDK_INT<Build.VERSION_CODES.M;

    private Drawable mForeground;

    private boolean mForegroundBoundsChanged = false;

    public ForegroundTextView(Context context) {
        super(context,null);
    }

    public ForegroundTextView(Context context, @Nullable AttributeSet attrs) {
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
    public void setSingleLine(boolean singleLine) {
        throw new IllegalArgumentException("请使用maxLines代替");
    }

    @Override
    public void setForeground(Drawable d){
        if(NEED_COMPAT){
            if(mForeground == d)
                return;

            if (mForeground != null) {
                setWillNotDraw(false);
                mForeground.setCallback(null);
                unscheduleDrawable(mForeground);
            }else
                setWillNotDraw(true);

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (NEED_COMPAT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (mForeground != null) {
                    mForeground.setHotspot(e.getX(), e.getY());
                }
            }
        }
        return super.onTouchEvent(e);
    }

}
