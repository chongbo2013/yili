package com.lots.travel.main.info.person;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.BasePopup;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.OnChooseListener;

/**
 * Created by nalanzi on 2018/1/29.
 */

public class ChooseHotsPopup extends BasePopup implements View.OnClickListener{
    public static final int TYPE_ALL = 0;
    public static final int TYPE_NOTE = 1;
    public static final int TYPE_TRIP = 2;
    public static final int TYPE_ACTIVE = 3;
    public static final int TYPE_FOOTPRINT = 4;
    public static final int TYPE_LIVE = 5;
    public static final int TYPE_DESTINATION = 6;

    private OnChooseListener mOnChooseListener;
    private int mWidth,mHeight;

    public ChooseHotsPopup(Activity context, OnChooseListener listener) {
        super(context);
        mOnChooseListener = listener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.popup_choose_hots;
    }

    @Override
    public void onCreate(Context context, View root) {
        root.findViewById(R.id.tv_all).setOnClickListener(this);
        root.findViewById(R.id.tv_note).setOnClickListener(this);
        root.findViewById(R.id.tv_trip).setOnClickListener(this);
        root.findViewById(R.id.tv_active).setOnClickListener(this);
        root.findViewById(R.id.tv_footprint).setOnClickListener(this);
        root.findViewById(R.id.tv_live).setOnClickListener(this);
        root.findViewById(R.id.tv_destination).setOnClickListener(this);

        mWidth = (context.getResources().getDisplayMetrics().widthPixels-DensityUtil.dp2px(context,30))/3;
        root.measure(
                View.MeasureSpec.makeMeasureSpec(mWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        mHeight = root.getMeasuredHeight();
        setWidth(mWidth);
        setHeight(mHeight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_all:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_ALL);
                break;

            case R.id.tv_note:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_NOTE);
                break;

            case R.id.tv_trip:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_TRIP);
                break;

            case R.id.tv_active:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_ACTIVE);
                break;

            case R.id.tv_footprint:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_FOOTPRINT);
                break;

            case R.id.tv_live:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_LIVE);
                break;

            case R.id.tv_destination:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_DESTINATION);
                break;
        }
        dismiss();
    }

    public void show(View parent){
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        showAtLocation(parent,Gravity.NO_GRAVITY,location[0]+parent.getWidth()/2-mWidth/2, location[1]-mHeight);
    }

}
