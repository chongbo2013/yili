package com.lots.travel.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nalanzi on 2017/10/22.
 */

public class FitHeightViewPager extends ViewPager {

    public FitHeightViewPager(Context context) {
        this(context,null);
    }

    public FitHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(getChildCount()!=0){
            View childView = getChildAt(0);
            int heightSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
            childView.measure(widthMeasureSpec,heightSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    childView.getMeasuredHeight()+getPaddingTop()+getPaddingBottom(),
                    MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
