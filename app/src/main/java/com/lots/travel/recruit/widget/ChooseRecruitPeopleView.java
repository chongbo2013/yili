package com.lots.travel.recruit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2018/1/3.
 */

/**
 * 设置最小值和最大值
 * 为简便，只支持宽度为exactly、高度为wrap_content
 */
public class ChooseRecruitPeopleView extends View {
    private int mCount;

    private int mSpacing;
    private int mThumbOffset;

    private int mTouchX;
    private int mTouchSlop;
    private boolean mCaptured;

    private int mRecruitPeople;
    private boolean mCalculated;

    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;

    public ChooseRecruitPeopleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mCount = 8;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.img_recruit_people_en);
        mUnselectedDrawable = ContextCompat.getDrawable(context, R.drawable.img_recruit_people_dis);
        mCalculated = true;
    }

    public void setRecruitPeople(int people){
        mRecruitPeople = Math.max(0,people);
        mRecruitPeople = Math.min(mRecruitPeople,mCount);
        mCalculated = false;
        invalidate();
    }

    public int getRecruitPeople(){
        return mRecruitPeople;
    }

    //宽度为exactly、高度为wrap_content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int drawableWidth = mSelectedDrawable.getIntrinsicWidth();
        int drawableHeight = mSelectedDrawable.getIntrinsicHeight();
        mSpacing = (widthSize-getPaddingLeft()-getPaddingRight()-mCount*drawableWidth)/mCount;
        setMeasuredDimension(widthSize,drawableHeight+getPaddingTop()+getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int drawableWidth = mSelectedDrawable.getIntrinsicWidth();
        int drawableHeight = mSelectedDrawable.getIntrinsicHeight();

        if(!mCalculated) {
            mThumbOffset = getPaddingLeft() + (drawableWidth + mSpacing)*mRecruitPeople;
            mCalculated = true;
        }
        mThumbOffset = Math.max(getPaddingLeft(),mThumbOffset);

        int index = (mThumbOffset-getPaddingLeft())/(drawableWidth+mSpacing);
        int l,r;
        int t = getPaddingTop();
        int b = t+drawableHeight;
        for (int i=0;i<mCount;i++){
            l = getPaddingLeft()+(drawableWidth+mSpacing)*i+mSpacing/2;
            r = l+drawableWidth;
            Drawable drawable;
            if(i<index) {
                drawable = mSelectedDrawable;
            }else if(i>index) {
                drawable = mUnselectedDrawable;
            }else if(mThumbOffset>=l+drawableWidth/2){
                drawable = mSelectedDrawable;
                mRecruitPeople = index+1;
            }else{
                drawable = mUnselectedDrawable;
                mRecruitPeople = index;
            }
            drawable.setBounds(l,t,r,b);
            drawable.draw(canvas);
        }
    }

    //只需要记录位置然后更新显示即可
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mThumbOffset = (int) (ev.getX()+.5f);
                mTouchX = mThumbOffset;
                mCaptured = false;
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if(!mCaptured){
                    int newX = (int) (ev.getX()+.5f);
                    mCaptured = Math.abs(newX-mTouchX)>mTouchSlop;
                    if(mCaptured && getParent()!=null)
                        getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(mCaptured){
                    mThumbOffset = (int) (ev.getX()+.5f);
                    invalidate();
                }
                return true;
        }

        return super.onTouchEvent(ev);
    }

}
