package com.lots.travel.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nalanzi on 2018/1/3.
 */

public class VerticalLinearSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private boolean mFillHorizontal,mFillVertical;

    public VerticalLinearSpaceItemDecoration(int space){
        this(space,true,true);
    }

    public VerticalLinearSpaceItemDecoration(int space,boolean fillHorizontal,boolean fillVertical){
        mSpace = space;
        mFillHorizontal = fillHorizontal;
        mFillVertical = fillVertical;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if(mFillHorizontal) {
            outRect.left = mSpace;
            outRect.right = mSpace;
        }
        if(mFillVertical) {
            outRect.top = pos == 0 ? mSpace : 0;
            outRect.bottom = mSpace;
        }
    }

}
