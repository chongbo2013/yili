package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/9/5.
 */
//宽高按比率的ImageView
public class RatioedImageView extends android.support.v7.widget.AppCompatImageView {
    private float mRatio = 0;

    public RatioedImageView(Context context) {
        this(context,null);
    }

    public RatioedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{R.attr.ratio});
        float ratio = a.getFloat(0,1f);
        setRatio(ratio);
        a.recycle();
    }

    public void setRatio(float ratio){
        if(ratio<0 || Float.compare(mRatio,ratio)==0)
            return;

        mRatio = ratio;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0,height = 0;
        float ratio;

        if(widthMode==MeasureSpec.EXACTLY){
            ratio = mRatio==0 ? 0:1f /mRatio;
            width = widthSize;
            height = width==0 ? 0: (int) (width * ratio + .5f);
        }else if(heightMode==MeasureSpec.EXACTLY){
            ratio = mRatio;
            height = heightSize;
            width = height==0 ? 0: (int) (height * ratio + .5f);
        }

        int widthSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);

        super.onMeasure(widthSpec, heightSpec);
    }

}
