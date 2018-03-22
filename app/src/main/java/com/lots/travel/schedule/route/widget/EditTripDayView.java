package com.lots.travel.schedule.route.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class EditTripDayView extends FrameLayout implements View.OnClickListener {
    private TextView tvDayCount;

    private int mDayCount,mMaxDayCount;

    public EditTripDayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_edit_trip_day,this,true);

        findViewById(R.id.iv_day_reduce).setOnClickListener(this);
        findViewById(R.id.iv_day_add).setOnClickListener(this);
        tvDayCount = (TextView) findViewById(R.id.tv_day_count);

        mDayCount = 0;
        mMaxDayCount = Integer.MAX_VALUE;

        tvDayCount.setText(
                showText(mDayCount,context.getString(R.string.edit_labels_count)));
    }

    public void setDayCount(int dayCount){
        mDayCount = dayCount<0 ? 0:dayCount;
        tvDayCount.setText(
                showText(mDayCount,getContext().getString(R.string.edit_labels_count)));
    }

    public int getDayCount(){
        return mDayCount;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_day_add:
                mDayCount = changeDayCount(mDayCount,1);
                break;

            case R.id.iv_day_reduce:
                if(mDayCount==0)
                    return;
                mDayCount = changeDayCount(mDayCount,-1);
                break;
        }

        tvDayCount.setText(
                showText(mDayCount,getContext().getString(R.string.edit_labels_count)));
    }

    private int changeDayCount(int dayCount,int delta){
        dayCount += delta;
        dayCount = Math.max(0,dayCount);
        dayCount = Math.min(dayCount,mMaxDayCount);
        return dayCount;
    }

    private SpannableStringBuilder showText(int count, String extra){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        int textColor;
        int end = 0;
        strBuilder.append(Integer.toString(count));
        end = strBuilder.length();
        textColor = ContextCompat.getColor(getContext(),R.color.color_main);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new AbsoluteSizeSpan(30,true),0,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if(!TextUtils.isEmpty(extra)){
            strBuilder.append(extra);
            textColor = ContextCompat.getColor(getContext(),R.color.color_text);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new AbsoluteSizeSpan(12,true),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strBuilder;
    }

}
