package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.lots.travel.R;
import com.lots.travel.widget.foreground.ForegroundImageView;

/**
 * Created by nalanzi on 2017/9/16.
 */
//带tag的ImageView
public class TaggedImageView extends ForegroundImageView {
    private int tagWidth;
    private int tagHeight;
    private int tagGravity;
    private Drawable tagDrawable;
    private boolean tagVisible = true;
    private boolean tagBoundsChanged = true;

    public TaggedImageView(Context context) {
        this(context,null);
    }

    public TaggedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TaggedImageView);
        tagWidth = a.getLayoutDimension(R.styleable.TaggedImageView_tagWidth,60);
        tagHeight = a.getLayoutDimension(R.styleable.TaggedImageView_tagHeight,60);
        tagGravity = a.getInt(R.styleable.TaggedImageView_tagGravity,0x33);
        tagDrawable = a.getDrawable(R.styleable.TaggedImageView_tagDrawable);
        a.recycle();
    }

    public void setTagDrawable(Drawable drawable){
        tagDrawable = drawable;
        tagBoundsChanged = true;
        invalidate();
    }

    public void setTagVisible(boolean visible){
        if(tagVisible!=visible) {
            tagVisible = visible;
            invalidate();
        }
    }

    public void setTagGravity(int gravity){
        tagGravity = gravity;
        tagBoundsChanged = true;
        invalidate();
    }

    public void setTagSize(int w,int h){
        tagWidth = w;
        tagHeight = h;
        tagBoundsChanged = true;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        tagBoundsChanged = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        tagBoundsChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(tagBoundsChanged){
            tagBoundsChanged = false;

            if(tagDrawable==null)
                return;

            int tagW = getTagSize(tagWidth,tagDrawable.getIntrinsicWidth());
            int tagH = getTagSize(tagHeight,tagDrawable.getIntrinsicHeight());

            int l = getPaddingLeft();
            int t = getPaddingTop();

            if((tagGravity & 0x30)==0x30)
                t = getPaddingTop();
            else if((tagGravity & 0x50)==0x50)
                t = getHeight()-getPaddingBottom()-tagH;
            else if((tagGravity & 0x10)==0x10)
                t = (getPaddingTop()+getHeight()-getPaddingBottom()-tagH)/2;

            if((tagGravity & 0x03)==0x03)
                l = getPaddingLeft();
            else if((tagGravity & 0x05)==0x05)
                l = getWidth()-getPaddingRight()-tagW;
            else if((tagGravity & 0x10)==0x10)
                l = (getPaddingLeft()+getWidth()-getPaddingRight()-tagW)/2;

            tagDrawable.setBounds(l,t,l+tagW,t+tagH);
        }

        if(tagDrawable!=null && tagVisible)
            tagDrawable.draw(canvas);

    }

    private int getTagSize(int attrValue,int realSize){
        switch (attrValue){
            case -1:
                return getWidth()-getPaddingLeft()-getPaddingRight();
            case -2:
                return realSize;
            default:
                return attrValue;
        }
    }

}
