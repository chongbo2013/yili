package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/9/3.
 */
//变量没有save、restore，后续添加
public class CheckableImageView extends android.support.v7.widget.AppCompatImageView implements Checkable{
    private boolean mChecked;
    private Drawable mForeground;

    public CheckableImageView(Context context) {
        super(context,null);
    }

    public CheckableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CheckableImageView);
        mForeground = a.getDrawable(R.styleable.CheckableImageView_checkedForeground);
        mChecked = a.getBoolean(R.styleable.CheckableImageView_checked,false);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mChecked && mForeground!=null) {
            mForeground.setBounds(0,0,mForeground.getIntrinsicWidth(),mForeground.getIntrinsicHeight());
            mForeground.draw(canvas);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        invalidate();
    }
}
