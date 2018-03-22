package com.lots.travel.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nalanzi on 2017/10/21.
 */
//LinearLayout horizontal等分，RecyclerView必须为match_parent(宽)、wrap_content(高)
public class PartsLayoutManager extends LinearLayoutManager{
    private int partsCount;
    private int itemWidth;
    private int extra;

    public PartsLayoutManager(Context context,RecyclerView rv,int count,int ex){
        super(context);
        setOrientation(LinearLayoutManager.HORIZONTAL);
        partsCount = count;
        extra = ex;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if(partsCount==0) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }

        int widthSize = View.MeasureSpec.getSize(widthSpec);
        itemWidth = (widthSize-getPaddingLeft()-getPaddingRight()-extra)/partsCount;

        int height = View.MeasureSpec.getSize(heightSpec);
        setMeasuredDimension(widthSize, height);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        lp.width = itemWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        super.measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        lp.width = itemWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        super.measureChildWithMargins(child, widthUsed, heightUsed);
    }

}
