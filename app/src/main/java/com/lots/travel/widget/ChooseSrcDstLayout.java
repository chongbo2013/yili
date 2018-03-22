package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lots.travel.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 选取出发时间、出发点、到达时间、目的地
 */
public class ChooseSrcDstLayout extends RelativeLayout {
    public static final int ID_TIME_GO = R.id.tv_time_go;
    public static final int ID_TIME_BACK = R.id.tv_time_back;
    public static final int ID_PLACE_SRC = R.id.tv_src_place;
    public static final int ID_PLACE_DST = R.id.tv_dst_place;

    private TextView tvTimeGo,tvTimeBack;
    private TextView tvSrcPlace,tvDstPlace;

    private String timeGoHint,timeBackHint;
    private String placeSrcHint,placeDstHint;
    private Drawable placeSrcDrawable,placeDstDrawable;

    public ChooseSrcDstLayout(Context context) {
        this(context,null);
    }

    public ChooseSrcDstLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ChooseSrcDstLayout);
        timeGoHint = a.getString(R.styleable.ChooseSrcDstLayout_timeGoHint);
        timeBackHint = a.getString(R.styleable.ChooseSrcDstLayout_timeBackHint);
        placeSrcHint = a.getString(R.styleable.ChooseSrcDstLayout_placeSrcHint);
        placeDstHint = a.getString(R.styleable.ChooseSrcDstLayout_placeDstHint);
        placeSrcDrawable = a.getDrawable(R.styleable.ChooseSrcDstLayout_placeSrcDrawable);
        placeDstDrawable = a.getDrawable(R.styleable.ChooseSrcDstLayout_placeDstDrawable);
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.layout_choose_src_dst,this,true);

        tvTimeGo = (TextView) findViewById(R.id.tv_time_go);
        tvTimeBack = (TextView) findViewById(R.id.tv_time_back);
        tvSrcPlace = (TextView) findViewById(R.id.tv_src_place);
        tvDstPlace = (TextView) findViewById(R.id.tv_dst_place);

        setTime(null,null);
        setPlace(null,null);
    }

    public void setTime(Date go,Date back){
        tvTimeGo.setText(showTime(go,true));
        tvTimeBack.setText(showTime(back,false));
    }

    public void setPlace(String src,String dst){
        tvSrcPlace.setText(showPlace(src,true));
        tvDstPlace.setText(showPlace(dst,false));
    }

    public void setPlace(String place,boolean isSrc){
        TextView tvPlace = isSrc ? tvSrcPlace:tvDstPlace;
        Drawable drawable = isSrc ? placeSrcDrawable:placeDstDrawable;
        tvPlace.setText(showPlace(place,isSrc));
        if(drawable!=null && !TextUtils.isEmpty(place)) {
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            tvPlace.setCompoundDrawables(null, null, drawable, null);
        }else{
            tvPlace.setCompoundDrawables(null, null, null, null);
        }
    }

    public void applyClickListener(OnClickListener listener){
        tvTimeGo.setOnClickListener(listener);
        tvTimeBack.setOnClickListener(listener);
        tvSrcPlace.setOnClickListener(listener);
        tvDstPlace.setOnClickListener(listener);
    }

    private SpannableStringBuilder showTime(Date date, boolean go){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        int textSize,textColor;

        Context context = getContext();
        if(date==null){
            strBuilder.append(go ? timeGoHint : timeBackHint);
            textSize = (int) getResources().getDimension(R.dimen.fonts_tip);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textColor = ContextCompat.getColor(context,R.color.color_text);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("M月d日", Locale.getDefault());
            String monthDay = sdf.format(date);

            strBuilder.append(monthDay);
            int end = strBuilder.length();
            textSize = (int) getResources().getDimension(R.dimen.head_small);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textColor = ContextCompat.getColor(context,R.color.color_title_big);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            String[] weeks = getResources().getStringArray(R.array.trip_date_week);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int week = cal.get(Calendar.DAY_OF_WEEK);
            String weekStr = weeks[week-1];

            strBuilder.append(" ");
            end++;
            strBuilder.append(weekStr);
            textSize = (int) getResources().getDimension(R.dimen.fonts_tip);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textColor = ContextCompat.getColor(context,R.color.color_text);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strBuilder;
    }

    private SpannableStringBuilder showPlace(String place,boolean src){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        int textSize,textColor;

        Context context = getContext();
        if(TextUtils.isEmpty(place)){
            place = src ? placeSrcHint : placeDstHint;
            textSize = (int) getResources().getDimension(R.dimen.fonts_tip);
            textColor = ContextCompat.getColor(context,R.color.color_text);
        }else{
            textSize = (int) getResources().getDimension(R.dimen.head_small);
            textColor = ContextCompat.getColor(context,R.color.color_title_big);
        }

        strBuilder.append(place);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

}
