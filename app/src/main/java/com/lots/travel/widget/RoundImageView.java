/*
 * Copyright 2014 - 2017 Henning Dodenhof
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.lots.travel.R;

public class RoundImageView extends android.support.v7.widget.AppCompatImageView {
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    private static final int DEFAULT_BORDER_RADIUS = 0;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mBorderRadius = DEFAULT_BORDER_RADIUS;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private ColorFilter mColorFilter;

    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;
    private boolean mDisableCircularTransformation;

    private int mTagWidth;
    private int mTagHeight;
    private int mTagGravity;
    private Drawable mTagDrawable;
    private boolean mTagVisible = true;
    private boolean mTagBoundsChanged = true;

    private float mRatio = 0;

    public RoundImageView(Context context) {
        super(context);

        init();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundImageView_riv_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.RoundImageView_riv_border_color, DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.RoundImageView_riv_border_overlay, DEFAULT_BORDER_OVERLAY);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_riv_border_radius,DEFAULT_BORDER_RADIUS);

        mTagWidth = a.getLayoutDimension(R.styleable.RoundImageView_riv_tag_width,60);
        mTagHeight = a.getLayoutDimension(R.styleable.RoundImageView_riv_tag_height,60);
        mTagGravity = a.getInt(R.styleable.RoundImageView_riv_tag_gravity,0x33);
        mTagDrawable = a.getDrawable(R.styleable.RoundImageView_riv_tag_drawable);
        mTagVisible = a.getBoolean(R.styleable.RoundImageView_riv_tag_visible,true);

        mRatio = a.getFloat(R.styleable.RoundImageView_riv_ratio,0f);
        a.recycle();

        init();
    }

    private void init() {
        mReady = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new OutlineProvider());
        }

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDisableCircularTransformation) {
            super.onDraw(canvas);
            return;
        }

        if (mBitmap==null) {
            return;
        }

        canvas.drawRoundRect(mDrawableRect,mBorderRadius,mBorderRadius, mBitmapPaint);

        if(mTagBoundsChanged && mTagDrawable!=null){
            mTagBoundsChanged = false;

            int tagW = getTagSize(mTagWidth, mTagDrawable.getIntrinsicWidth());
            int tagH = getTagSize(mTagHeight, mTagDrawable.getIntrinsicHeight());

            int l = getPaddingLeft();
            int t = getPaddingTop();

            if((mTagGravity & 0x30)==0x30)
                t = getPaddingTop();
            else if((mTagGravity & 0x50)==0x50)
                t = getHeight()-getPaddingBottom()-tagH;
            else if((mTagGravity & 0x10)==0x10)
                t = (getPaddingTop()+getHeight()-getPaddingBottom()-tagH)/2;

            if((mTagGravity & 0x03)==0x03)
                l = getPaddingLeft();
            else if((mTagGravity & 0x05)==0x05)
                l = getWidth()-getPaddingRight()-tagW;
            else if((mTagGravity & 0x10)==0x10)
                l = (getPaddingLeft()+getWidth()-getPaddingRight()-tagW)/2;

            mTagDrawable.setBounds(l,t,l+tagW,t+tagH);
        }

        if(mTagDrawable!=null && mTagVisible)
            mTagDrawable.draw(canvas);

        if (mBorderWidth > 0) {
            canvas.drawRoundRect(mDrawableRect,mBorderRadius, mBorderRadius, mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
        mTagBoundsChanged = true;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    /**
     * @deprecated Use {@link #setBorderColor(int)} instead
     */
    @Deprecated
    public void setBorderColorResource(@ColorRes int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }

    public boolean isDisableCircularTransformation() {
        return mDisableCircularTransformation;
    }

    public void setDisableCircularTransformation(boolean disableCircularTransformation) {
        if (mDisableCircularTransformation == disableCircularTransformation) {
            return;
        }

        mDisableCircularTransformation = disableCircularTransformation;
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    private void applyColorFilter() {
        if (mBitmapPaint != null) {
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeBitmap() {
        if (mDisableCircularTransformation) {
            mBitmap = null;
        } else {
            mBitmap = getBitmapFromDrawable(getDrawable());
        }
        setup();
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(getPaddingLeft(),getPaddingTop(),
                getWidth()-getPaddingRight(),getHeight()-getPaddingBottom());

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay && mBorderWidth > 0) {
            mDrawableRect.inset(mBorderWidth/2f, mBorderWidth/2f);
        }

        applyColorFilter();
        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class OutlineProvider extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            Rect bounds = new Rect();
            mBorderRect.roundOut(bounds);
            outline.setRoundRect(bounds, bounds.width() / 2.0f);
        }

    }

    public void setTagDrawable(Drawable drawable){
        mTagDrawable = drawable;
        mTagBoundsChanged = true;
        invalidate();
    }

    public void setTagVisible(boolean visible){
        if(mTagVisible!=visible) {
            mTagVisible = visible;
            invalidate();
        }
    }

    public void setTagGravity(int gravity){
        mTagGravity = gravity;
        mTagBoundsChanged = true;
        invalidate();
    }

    public void setTagSize(int w,int h){
        mTagWidth = w;
        mTagHeight = h;
        mTagBoundsChanged = true;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTagBoundsChanged = true;
    }

    private int getTagSize(int attrValue,int realSize){
        switch (attrValue){
            case -1:
                return getWidth()-getPaddingLeft()-getPaddingRight();
            case -2:
                return realSize;
            default:
                return attrValue;
        }
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
        if(Float.compare(mRatio,0)==0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

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
