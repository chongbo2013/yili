package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Path;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lots.travel.R;

/**
 * 点击开始播放，设置时间
 * detach时停止
 * 两次间隔时间
 */
public class VoiceBar extends View {
    private int mBackgroundColor;

    private int mArcColor;
    private int mPlayArcColor;
    private int mArcPadding;
    private float mArcDegree;
    private int mArcWidth;
    private int mArcRadius;
    private int mArcSpace;

    private RectF mArcRect;
    private Paint mArcPaint;

    private int mBackgroundRadius;
    private int mTriangleEdge;

    private RectF mBackgroundRect;
    private Paint mBackgroundPaint;
    private Path mTrianglePath;

    private boolean mBoundsChanged;

    private long mDuration;
    private long mStartMills;
    private long mCurrentMills;
    private int mArcCount;
    private boolean mRunning;

    private OnPlayListener mOnPlayListener;

    private Runnable mPlayRunnable = new Runnable() {
        @Override
        public void run() {
            final long nowMills = SystemClock.uptimeMillis();
            if(nowMills-mStartMills<mDuration) {
                if(nowMills-mCurrentMills>=300){
                    mCurrentMills = nowMills;
                    mArcCount = mArcCount%3+1;
                    invalidate();
                }
                post(this);
            }else {
                mRunning = false;
                mArcCount = 3;
                invalidate();
            }
        }
    };

    public VoiceBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.VoiceBar);
        mArcColor = a.getColor(R.styleable.VoiceBar_arcColor,Color.GRAY);
        mPlayArcColor = a.getColor(R.styleable.VoiceBar_playArcColor,Color.WHITE);
        mArcPadding = a.getDimensionPixelSize(R.styleable.VoiceBar_arcPadding,90);
        mArcDegree = a.getFloat(R.styleable.VoiceBar_arcDegree,40f);
        mArcWidth = a.getDimensionPixelSize(R.styleable.VoiceBar_arcWidth,6);
        mArcRadius = a.getDimensionPixelSize(R.styleable.VoiceBar_arcRadius,10);
        mArcSpace = a.getDimensionPixelSize(R.styleable.VoiceBar_arcSpace,3);
        mBackgroundColor = a.getColor(R.styleable.VoiceBar_backgroundColor,0xFF23B6B7);
        mBackgroundRadius = a.getDimensionPixelSize(R.styleable.VoiceBar_backgroundRadius,12);
        mTriangleEdge = a.getDimensionPixelSize(R.styleable.VoiceBar_triangleEdge,12);
        a.recycle();

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeWidth(mArcWidth);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);

        mBackgroundRect = new RectF();
        mTrianglePath = new Path();
        mArcRect = new RectF();

        mDuration = 10000;
        mArcCount = 3;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRunning){
                    removeCallbacks(mPlayRunnable);
                    mRunning = false;
                    mArcCount = 3;
                    if(mOnPlayListener!=null)
                        mOnPlayListener.onPlayStop();
                }else {
                    mStartMills = SystemClock.uptimeMillis();
                    mCurrentMills = mStartMills;
                    mRunning = true;
                    mArcCount = 1;
                    post(mPlayRunnable);
                    if (mOnPlayListener != null)
                        mOnPlayListener.onPlayStart();
                }
                invalidate();
            }
        });
    }

    public void setDuration(long duration){
        mDuration = duration;
    }

    public void setOnPlayListener(OnPlayListener listener){
        mOnPlayListener = listener;
    }

    public void reset(){
        if(mRunning){
            mRunning = false;
            removeCallbacks(mPlayRunnable);
            if(mOnPlayListener!=null)
                mOnPlayListener.onPlayStop();
        }
        mArcCount = 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBoundsChanged = true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return !mRunning && super.onTouchEvent(event);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBoundsChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mBoundsChanged){
            mBackgroundRect.set(
                    mTriangleEdge+getPaddingLeft(),
                    getPaddingTop(),
                    getWidth()-getPaddingRight(),
                    getHeight()-getPaddingBottom());
            mTrianglePath.reset();
            mTrianglePath.moveTo(mBackgroundRect.left+1,mBackgroundRect.centerY()-mTriangleEdge/2);
            mTrianglePath.lineTo((float) (mBackgroundRect.left-mTriangleEdge*Math.sin(Math.PI*1.5f/9f)+.5f),mBackgroundRect.centerY());
            mTrianglePath.lineTo(mBackgroundRect.left+1,mBackgroundRect.centerY()+mTriangleEdge/2);
            mTrianglePath.close();
        }
        //绘制背景
        canvas.drawRoundRect(mBackgroundRect,mBackgroundRadius,mBackgroundRadius,mBackgroundPaint);
        //绘制三角形
        canvas.drawPath(mTrianglePath,mBackgroundPaint);
        //绘制arc
        mArcPaint.setColor(mRunning ? mPlayArcColor:mArcColor);
        int cx = (int) (mBackgroundRect.left+mArcPadding+.5f);
        int cy = (int) (mBackgroundRect.centerY()+.5f);
        for (int i=0;i<mArcCount;i++) {
            int radius = mArcRadius+(mArcWidth+mArcSpace)*i;
            mArcRect.set(cx-radius,cy-radius,cx+radius,cy+radius);
            canvas.drawArc(mArcRect,-mArcDegree/2,mArcDegree,false,mArcPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    //开始时进行回调
    public interface OnPlayListener{
        void onPlayStart();
        void onPlayStop();
    }

}
