package com.lots.travel.schedule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;

import com.lots.travel.R;
import com.lots.travel.util.DensityUtil;

/**
 * Created by nalanzi on 2017/9/24.
 */
//带下划线，为了避免布局嵌套过深
public class DescriptionTab extends android.support.v7.widget.AppCompatRadioButton{
    private int lineColor;
    private float lineWidth;
    private Paint linePaint;

    private float extraWidth;

    public DescriptionTab(Context context) {
        this(context,null);
    }

    public DescriptionTab(Context context, AttributeSet attrs) {
        super(context, attrs);

        lineWidth = DensityUtil.dp2px(context,1);
        lineColor = ContextCompat.getColor(context, R.color.color_main);
        setButtonDrawable(android.R.color.transparent);
        setGravity(Gravity.CENTER);
        setMaxLines(1);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineWidth);

        extraWidth = DensityUtil.dp2px(context,2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isChecked()){
            int dx = getPaddingLeft()+(getWidth()-getPaddingLeft()-getPaddingRight())/2;
            canvas.translate(dx,0);

            float width = getWidth()-getPaddingLeft()-getPaddingRight();
            Paint paint = getPaint();
            if(paint!=null)
                width = Math.min(width,paint.measureText(getText().toString()));

            canvas.drawLine(-width/2-extraWidth,getHeight()-lineWidth*2,width/2+extraWidth,getHeight()-lineWidth*2,linePaint);
            canvas.translate(-dx,0);
        }
    }
}
