package com.lots.travel.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.util.DensityUtil;

/**
 * 左边带竖线，如果使用drawableLeft会导致居中
 */

@SuppressLint("AppCompatCustomView")
public class LeftLineTextView extends TextView {
    private int linePadding;
    private int lineWidth;
    private int lineHeight;
    private float lineHeightRatio;

    private Paint linePaint;

    private RectF lineRect;

    public LeftLineTextView(Context context) {
        this(context,null);
    }

    public LeftLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeftLineTextView);
        lineWidth = a.getDimensionPixelSize(R.styleable.LeftLineTextView_leftLineWidth,DensityUtil.dp2px(context,3));
        lineHeight = a.getDimensionPixelOffset(R.styleable.LeftLineTextView_leftLineHeight,-1);
        linePadding = a.getDimensionPixelOffset(R.styleable.LeftLineTextView_leftLinePadding,DensityUtil.dp2px(context,3));
        lineHeightRatio = a.getFloat(R.styleable.LeftLineTextView_leftLineHeightRatio,.75f);

        lineRect = new RectF();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(a.getColor(R.styleable.LeftLineTextView_leftLineColor,
                ContextCompat.getColor(context,R.color.color_main)));
        a.recycle();

        //调用setPadding
        setTextPadding(getPaddingLeft(),getPaddingTop(),getPaddingRight(),getPaddingBottom());
    }

    public void setTextPadding(int l,int t,int r,int b){
        setPadding(l+lineWidth+linePadding,t,r,b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lineRect.width()!=0 && lineRect.height()!=0){
            float r = lineRect.width()/2;
            canvas.drawRoundRect(lineRect,r,r,linePaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        TextPaint textPaint = getPaint();
        if(textPaint!=null) {
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float paintHeight = metrics.bottom-metrics.top;

            float realHeight;
            float realTop;
            if(lineHeight<=0){
                realHeight = paintHeight*lineHeightRatio;
                realTop = getPaddingTop()+paintHeight/2-realHeight/2;
            }else{
                realTop = getPaddingTop()+paintHeight/2-lineHeight/2;
                realHeight = lineHeight;
            }

            float realLeft = getPaddingLeft()-linePadding-lineWidth;
            lineRect.set(realLeft,realTop,realLeft+lineWidth,realTop+realHeight);
        }
    }

}
