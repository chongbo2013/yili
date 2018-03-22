package com.lots.travel.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.lots.travel.util.DensityUtil;

import java.security.MessageDigest;

/**
 * Created by nalanzi on 2017/9/29.
 */

public class GlideRoundTransform extends BitmapTransformation {
    private float radius = 0f;

    private Paint shaderPaint;
    private Paint borderPaint;

    public GlideRoundTransform(Context context) {
        this(context,Color.WHITE,DensityUtil.dp2px(context,4));
    }

    public GlideRoundTransform(Context context,int color,int lineWidth) {
        super();
        this.radius = lineWidth;

        shaderPaint = new Paint();

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(DensityUtil.dp2px(context,2));
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, bitmap);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);

        shaderPaint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, shaderPaint);
        float hBorderWidth = borderPaint.getStrokeWidth()/2;
        rectF.set(hBorderWidth,hBorderWidth,source.getWidth()-hBorderWidth,source.getHeight()-hBorderWidth);
        canvas.drawRoundRect(rectF,radius,radius,borderPaint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {}
}
