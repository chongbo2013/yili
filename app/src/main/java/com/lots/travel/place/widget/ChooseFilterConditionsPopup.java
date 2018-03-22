package com.lots.travel.place.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.lots.travel.R;
import com.lots.travel.place.adapter.ChooseFilterConditionsAdapter;
import com.lots.travel.util.DensityUtil;

/**
 * Created by nalanzi on 2017/9/3.
 */

public class ChooseFilterConditionsPopup {
    private final int X_OFFSET;
    private final int Y_OFFSET;
    private PopupWindow mPopup;
    private ChooseFilterConditionsAdapter mChooseFilerConditionsAdapter;

    public ChooseFilterConditionsPopup(Context context) {
        X_OFFSET = DensityUtil.dp2px(context,80);
        Y_OFFSET = (int) ((context.getResources().getDimension(R.dimen.actionBarSize)-DensityUtil.dp2px(context,32))/2);

        mPopup = new PopupWindow(context);
        RecyclerView root = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.popup_choose_fiter_conditions,null);
        mPopup.setContentView(root);

        mChooseFilerConditionsAdapter = new ChooseFilterConditionsAdapter();
        root.setLayoutManager(new LinearLayoutManager(context));
        root.setAdapter(mChooseFilerConditionsAdapter);

        mPopup.setWidth(DensityUtil.dp2px(context,100));
        mPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        mPopup.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.popup_choose_filer_conditions));
        mPopup.setOutsideTouchable(true);
        mPopup.setTouchable(true);
        mPopup.setFocusable(true);
    }

    public void setOnItemClickListener(ChooseFilterConditionsAdapter.OnItemClickListener listener){
        mChooseFilerConditionsAdapter.setOnItemClickListener(listener);
    }

    public void show(View anchor){
        mPopup.showAsDropDown(anchor,anchor.getWidth()/2-X_OFFSET,Y_OFFSET+4);
    }

    public void dismiss(){
        if(mPopup.isShowing())
            mPopup.dismiss();
    }

}
