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

public class ChooseFollowPopup extends BasePopup implements View.OnClickListener{
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_CANCEL = 1;

    private OnChooseListener mOnChooseListener;
    private int mWidth,mHeight;

    public ChooseFollowPopup(Activity context,OnChooseListener listener) {
        super(context);
        mOnChooseListener = listener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.popup_choose_follow;
    }

    @Override
    public void onCreate(Context context, View root) {
        root.findViewById(R.id.tv_cancel).setOnClickListener(this);
        root.findViewById(R.id.tv_group).setOnClickListener(this);
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
            case R.id.tv_cancel:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_CANCEL);
                break;

            case R.id.tv_group:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_GROUP);
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
