package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.lots.travel.R;

/**
 * Created by lWX479187 on 2017/11/9.
 */
public class RoundRelativeLayout extends RelativeLayout {
    private static final int[] ATTRS = new int[]{R.attr.radius};

    private Path clipPath;
    private RectF clipRect;

    private float radius;

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,ATTRS);
        radius = a.getDimension(0,0);
        a.recycle();

        clipPath = new Path();
        clipRect = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        clipRect.set(0,0,getWidth(),getHeight());
        clipPath.reset();
        clipPath.addRoundRect(clipRect,radius,radius, Path.Direction.CCW);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

}
