package com.lots.travel.widget.calendar.trip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lots.travel.R;

import java.util.ArrayList;

/**
 * Created by nalanzi on 2017/9/22.
 */

public class TripWeekView extends View {
    private Paint mTextPaint;
    private float mCellHeight;
    private float mCellWidth;

    private final String[] WEEK;

    public TripWeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        WEEK = getResources().getStringArray(R.array.week);
        if(WEEK.length!=7)
            throw new IllegalArgumentException("数组错误");

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TripWeekView);
        mTextPaint.setColor(a.getColor(R.styleable.TripWeekView_weekTextColor,Color.BLACK));
        mTextPaint.setTextSize(a.getDimension(R.styleable.TripWeekView_weekTextSize,30));
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float baseline = getTextBaseline(mTextPaint,0);

        int dx = (int) (getPaddingLeft()+mCellWidth/2);
        int dy = (int) (getPaddingTop()+mCellHeight/2);
        canvas.translate(dx,dy);
        for (int i=0;i<WEEK.length;i++){
            float offsetX = mCellWidth*i;
            canvas.translate(offsetX,0);
            float x = -mTextPaint.measureText(WEEK[i])/2;
            canvas.drawText(WEEK[i],x,baseline,mTextPaint);
            canvas.translate(-offsetX,0);
        }
        canvas.translate(-dx,-dy);
    }

    private float getTextBaseline(Paint paint,float centerY){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return centerY-(fontMetrics.bottom+fontMetrics.top)/2;
    }

    //简便起见，只支持使用exactly
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mCellWidth = (getWidth()-getPaddingLeft()-getPaddingRight()) / WEEK.length;
        mCellHeight = getHeight()-getPaddingTop()-getPaddingBottom();
    }
}
