package com.lots.travel.schedule.note.detail;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.BasePopup;
import com.lots.travel.widget.OnChooseListener;

/**
 * Created by nalanzi on 2018/1/29.
 */

public class ChooseDetailGzPopup extends BasePopup implements View.OnClickListener{
    public static final int TYPE_CANCEL = 1;
    public static final int TYPE_GROUP = 2;

    private OnChooseListener mOnChooseListener;

    public ChooseDetailGzPopup(Activity context, OnChooseListener listener) {
        super(context);
        mOnChooseListener = listener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.popup_choose_detail_gz;
    }

    @Override
    public void onCreate(Context context, View root) {
        root.findViewById(R.id.tv_group).setOnClickListener(this);
        root.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_group:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_GROUP);
                break;

            case R.id.tv_cancel:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_CANCEL);
                break;
        }
        dismiss();
    }

}
