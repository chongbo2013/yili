package com.lots.travel.schedule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.util.DensityUtil;

/**
 * 计时部分放在了View内部，录音或者播放时不断更新ui，避免ui更新出现掉帧
 * 可以计时放在View外部，但是View内部也需要不断invalidate
 */
public class VoiceCircleView extends View {
    private long mStartMills;
    private long mCurrentLength;
    //默认最大值为60秒
    private long mPlayLength;
    private long mRecordLength = 60000;

    //处于录制阶段还是播放阶段
    private boolean mInPlayPhase;
    //正在录音、正在播放
    private boolean mRunning;

    private OnVoiceCallback mCallback;

    private Paint mCirclePaint;
    private Paint mRingPaint;

    private Drawable mRecordDrawable;
    private Drawable mPlayDrawable;
    private Drawable mPauseDrawable;

    private int mSpace;
    private int mRingWidth;

    private RectF mArcRect;
    private Rect mDrawRect;
    private boolean mBoundsChanged;

    //达到时间以后，后续的event就不处理了
    private boolean invalidateDown;

    public VoiceCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mInPlayPhase = false;
        mRunning = false;

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(DensityUtil.dp2px(context,0.5f));
        mCirclePaint.setColor(ContextCompat.getColor(context, R.color.color_main));

        mRingWidth = DensityUtil.dp2px(context,3f);
        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mRingWidth);
        mRingPaint.setColor(ContextCompat.getColor(context, R.color.color_main));
        mRingPaint.setStrokeJoin(Paint.Join.ROUND);

        mRecordDrawable = ContextCompat.getDrawable(context,R.drawable.img_audio_recording);
        mPlayDrawable = ContextCompat.getDrawable(context,R.drawable.img_audio_play);
        mPauseDrawable = ContextCompat.getDrawable(context,R.drawable.img_audio_pause);

        mSpace = DensityUtil.dp2px(context,8);

        mBoundsChanged = true;
        mDrawRect = new Rect();
        mArcRect = new RectF();
    }

    //在play阶段时，可以设置在某个地方播放，totalLength在play阶段使用，maxLength在录制阶段使用
    public void setPhase(boolean inPlayPhase,long currentLength,long playLength,int recordLength){
        mRunning = false;
        mInPlayPhase = inPlayPhase;
        mCurrentLength = currentLength;
        mPlayLength = playLength;
        mRecordLength = recordLength;
        invalidate();
    }

    public void setCallback(OnVoiceCallback callback){
        mCallback = callback;
    }

    public boolean isRunning(){
        return mRunning;
    }

    public int getFilepathLength(){
        return (int) mPlayLength;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(mCallback==null)
            return;
        if(!mInPlayPhase && mRunning)
            mCallback.onRecordComplete(mPlayLength);
        else if(mInPlayPhase && mRunning)
            mCallback.onPlayStop(mCurrentLength,mPlayLength);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = mRecordDrawable.getIntrinsicWidth()+mSpace*2+mRingWidth;
        setMeasuredDimension(size,size);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int rw = (getWidth()-getPaddingLeft()-getPaddingRight());
        int rh = (getHeight()-getPaddingTop()-getPaddingBottom());
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        mDrawRect.set(pl,pt,pl+rw,pt+rh);
        mBoundsChanged = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBoundsChanged = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                invalidateDown = false;
                //在录制阶段，开始录制
                if(!mInPlayPhase){
                    mRunning = true;
                    mCurrentLength = 0;
                    mStartMills = SystemClock.uptimeMillis();
                    if(mCallback!=null)
                        mCallback.onRecordStart(mRecordLength);
                    invalidate();
                    if(getParent()!=null)
                        getParent().requestDisallowInterceptTouchEvent(true);
                }
                return true;

            case MotionEvent.ACTION_CANCEL:
                if(mInPlayPhase)
                    return false;

            case MotionEvent.ACTION_UP:
                if(invalidateDown)
                    return false;

                //处于哪个阶段、哪个过程
                if(!mInPlayPhase && mRunning){
                    mInPlayPhase = true;
                    mRunning = false;
                    mPlayLength = mCurrentLength;
                    mCurrentLength = 0;
                    if(mCallback!=null){
                        mCallback.onRecordComplete(mPlayLength);
                    }
                    invalidate();
                }else if(mInPlayPhase && mRunning){
                    mRunning = false;
                    mCurrentLength = 0;
                    if(mCallback!=null){
                        mCallback.onPlayStop(mCurrentLength,mPlayLength);
                    }
                    invalidate();
                }else if(mInPlayPhase/* && !mRunning*/){
                    mRunning = true;
                    mStartMills = SystemClock.uptimeMillis();
                    if(mCallback!=null){
                        mCallback.onPlayStart(mCurrentLength,mPlayLength);
                    }
                    invalidate();
                }
                break;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = mDrawRect.centerX();
        int cy = mDrawRect.centerY();
        int radius = mRecordDrawable.getIntrinsicWidth()/2;

        if(mBoundsChanged){
            float cs = radius+mSpace;
            mArcRect.set(cx-cs,cy-cs,cx+cs,cy+cs);
            mRecordDrawable.setBounds(cx-radius,cy-radius,cx+radius,cy+radius);
            mPlayDrawable.setBounds(cx-radius,cy-radius,cx+radius,cy+radius);
            mPauseDrawable.setBounds(cx-radius,cy-radius,cx+radius,cy+radius);
        }

        if(!mInPlayPhase && !mRunning){
            //绘制按钮
            mRecordDrawable.draw(canvas);
            //绘制圆圈
            canvas.drawCircle(cx,cy,radius+mSpace,mCirclePaint);
        }else if(!mInPlayPhase/* && mRunning*/){
            //绘制按钮
            mRecordDrawable.draw(canvas);
            //绘制圆圈
            canvas.drawArc(mArcRect,-90,mCurrentLength*360f/mRecordLength,false,mRingPaint);
            //更新进度
            long currentMills = SystemClock.uptimeMillis();
            mCurrentLength += currentMills-mStartMills;
            mStartMills = currentMills;
            //是否结束
            if(mCurrentLength>=mRecordLength){
                invalidateDown = true;
                mRunning = false;
                mInPlayPhase = true;
                mCurrentLength = 0;
                mPlayLength = mRecordLength;
                if(mCallback!=null)
                    mCallback.onRecordComplete(mPlayLength);
            }else if (mCallback!=null){
                mCallback.onRunning(mCurrentLength,mRecordLength);
            }
            invalidate();
        }else if(/*mInPlayPhase && */!mRunning){
            //绘制按钮
            mPlayDrawable.draw(canvas);
            //绘制圆圈
            canvas.drawCircle(cx,cy,radius+mSpace,mCirclePaint);
        }else/* if(mInPlayPhase && mRunning)*/{
            //绘制按钮
            mPauseDrawable.draw(canvas);
            //绘制圆圈
            canvas.drawArc(mArcRect,-90,mCurrentLength*360f/mPlayLength,false,mRingPaint);
            //更新进度
            long currentMills = SystemClock.uptimeMillis();
            mCurrentLength += currentMills-mStartMills;
            mStartMills = currentMills;
            //是否结束
            if(mCurrentLength>=mPlayLength){
                mRunning = false;
                mCurrentLength = 0;
                if(mCallback!=null)
                    mCallback.onPlayStop(mPlayLength,mPlayLength);
            }else if (mCallback!=null){
                mCallback.onRunning(mCurrentLength,mPlayLength);
            }
            invalidate();
        }
    }

}
