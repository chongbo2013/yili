package com.lots.travel.widget.picker.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lots.travel.R;
import com.lots.travel.widget.picker.core.PickerView;

/**
 * Created by nalanzi on 2017/12/14.
 */

public class PickerGroupLayout extends LinearLayout {
    private Paint linePaint;

    public PickerGroupLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        setWillNotDraw(false);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerGroupLayout);
        linePaint.setStrokeWidth(a.getDimension(R.styleable.PickerGroupLayout_pglDividerHeight,2));
        linePaint.setColor(a.getColor(R.styleable.PickerGroupLayout_pglDividerColor, Color.GRAY));
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getChildCount()==0)
            return;

        PickerView picker = (PickerView) getChildAt(0);
        float centerHeight = picker.getCenterRangeHeight();
        int cy = getPaddingTop()+(getHeight()-getPaddingTop()-getPaddingBottom())/2;
        int cl = getPaddingLeft();
        int ct = (int) (cy-centerHeight/2);
        int cr = getWidth()-getPaddingRight();
        int cb = (int) (cy+centerHeight/2);

        canvas.drawLine(cl,ct,cr,ct,linePaint);
        canvas.drawLine(cl,cb,cr,cb,linePaint);
    }
}
