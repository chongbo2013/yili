package com.lots.travel.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by nalanzi on 2017/11/29.
 */

public class StarDrawable extends Drawable {
    private static final String STAR = "*";
    private Paint mStarPaint;
    private Rect mStarRect;

    public StarDrawable(int color,int size){
        mStarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStarPaint.setTextSize(size);
        mStarPaint.setColor(color);

        mStarRect = new Rect();
        mStarPaint.getTextBounds(STAR,0,1,mStarRect);
    }

    @Override
    public int getIntrinsicWidth() {
        return mStarRect.width();
    }

    @Override
    public int getIntrinsicHeight() {
        return mStarRect.height();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint.FontMetrics metrics = mStarPaint.getFontMetrics();
        Rect bounds = getBounds();
        canvas.drawText(STAR,0,bounds.centerY()-(metrics.bottom+metrics.top)/2+2,mStarPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {}

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {}

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
