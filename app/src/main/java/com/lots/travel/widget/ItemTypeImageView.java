package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 只支持android:src属性
 */
public class ItemTypeImageView extends View {
    private static final int[] ATTRS =
            new int[]{android.R.attr.src};

    private Drawable mDrawable;
    private boolean mBoundsChanged;

    public ItemTypeImageView(Context context) {
        this(context,null);
    }

    public ItemTypeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,ATTRS);
        mDrawable = a.getDrawable(0);
        a.recycle();

        setPadding(0,0,0,0);
    }

    public void setImageResource(int drawable){
        mDrawable = ContextCompat.getDrawable(getContext(),drawable);
        mBoundsChanged = true;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBoundsChanged = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBoundsChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mDrawable!=null){
            if(mBoundsChanged){
                mDrawable.setBounds(0,0,
                        mDrawable.getIntrinsicWidth(),mDrawable.getIntrinsicHeight());
            }
            mDrawable.draw(canvas);
        }
    }

}
