package com.lots.travel.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by nalanzi on 2017/9/25.
 */

public class LinearItemsLayout extends LinearLayout implements View.OnClickListener{
    private OnItemClickListener mOnItemClickListener;

    public LinearItemsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(Adapter adapter){
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for(int i=0;i<adapter.getCount();i++){
            View view = adapter.getView(inflater,this,i);
            addView(view);
            if(adapter.isClickable(i)) {
                view.setTag(i);
                view.setOnClickListener(this);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(position);
        }
    }

    public interface Adapter{
        boolean isClickable(int position);

        int getCount();

        View getView(LayoutInflater inflater,LinearLayout container,int position);

    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}
