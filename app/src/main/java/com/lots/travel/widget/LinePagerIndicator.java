package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/10/22.
 */

public class LinePagerIndicator extends View implements ViewPager.OnPageChangeListener{
    private float selectWidth,unSelectWidth;

    private float lineHeight;

    private float gapHorizontal;

    private int lineCount;

    private int selectPos;
    private float selectOffset;

    private Paint selectPaint,unSelectPaint;

    private RectF tempRt;

    private ViewPager viewPager;

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            reset();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            reset();
        }
    };

    public LinePagerIndicator(Context context) {
        this(context,null);
    }

    public LinePagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinePagerIndicator);
        selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setColor(a.getColor(R.styleable.LinePagerIndicator_selectColor,Color.RED));

        unSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unSelectPaint.setStyle(Paint.Style.FILL);
        unSelectPaint.setColor(a.getColor(R.styleable.LinePagerIndicator_unSelectColor,Color.GRAY));

        selectWidth = a.getDimension(R.styleable.LinePagerIndicator_selectWidth,40);
        unSelectWidth = a.getDimension(R.styleable.LinePagerIndicator_unSelectWidth,30);;
        lineHeight = a.getDimension(R.styleable.LinePagerIndicator_lineHeight,10);;

        gapHorizontal = a.getDimension(R.styleable.LinePagerIndicator_gapHorizontal,20);;
        lineCount = a.getInteger(R.styleable.LinePagerIndicator_lineCount,6);;
        selectPos = a.getInteger(R.styleable.LinePagerIndicator_selectPos,0);
        selectOffset = a.getFloat(R.styleable.LinePagerIndicator_selectOffset,0f);
        a.recycle();

        tempRt = new RectF();
    }

    public void setPager(ViewPager pager){
        if(viewPager==pager)
            return;

        if(viewPager!=null && viewPager.getAdapter()!=null){
            viewPager.getAdapter().unregisterDataSetObserver(dataSetObserver);
            viewPager.removeOnPageChangeListener(this);
        }
        viewPager = pager;
        viewPager.addOnPageChangeListener(this);
        reset();

        if(viewPager.getAdapter()!=null)
            viewPager.getAdapter().registerDataSetObserver(dataSetObserver);
    }

    private void reset(){
        lineCount = viewPager.getAdapter().getCount();
        selectPos = viewPager.getCurrentItem();
        selectOffset = 0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(lineCount ==0)
            return;

        float cx = getPaddingLeft()+(getWidth()-getPaddingLeft()-getPaddingRight())/2;
        float cy = getPaddingTop()+(getHeight()-getPaddingTop()-getPaddingBottom())/2;

        float totalWith = lineCount *unSelectWidth+(lineCount -1)*gapHorizontal;
        cx += unSelectWidth/2-totalWith/2;

        tempRt.set(cx-unSelectWidth/2,cy-lineHeight/2,cx+unSelectWidth/2,cy+lineHeight/2);
        for (int i = 0; i< lineCount; i++){
            canvas.drawRoundRect(tempRt,lineHeight/2,lineHeight/2,unSelectPaint);
            tempRt.offset(gapHorizontal+unSelectWidth,0);
        }

        tempRt.set(cx-selectWidth/2,cy-lineHeight/2,cx+selectWidth/2,cy+lineHeight/2);
        tempRt.offset((gapHorizontal+unSelectWidth)*(selectPos+selectOffset),0);
        canvas.drawRoundRect(tempRt,lineHeight/2,lineHeight/2,selectPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        selectPos = position;
        selectOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        selectPos = position;
        selectOffset = 0f;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
