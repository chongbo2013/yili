package com.lots.travel.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nalanzi on 2017/12/15.
 */
//对grid而言，如果不水平spacing填充不均匀，会出现宽度不一致的问题
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpanCount;
    private int mSpacingVertical;
    private int mSpacingHorizontal;

    public GridSpaceItemDecoration(int spanCount,int spacingVertical,int spacingHorizontal){
        mSpanCount = spanCount;
        mSpacingVertical = spacingVertical;
        mSpacingHorizontal = spacingHorizontal;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpacingHorizontal;
        outRect.right = mSpacingHorizontal;
        outRect.top = mSpacingVertical;
        outRect.bottom = mSpacingVertical;

//        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
//        int row = pos/mSpanCount;
//        int column = pos%mSpanCount;
//
//        outRect.left = column==0 ? 0:mSpacingHorizontal;
//        outRect.right = 0;
//        outRect.top = row==0 ? 0:mSpacingVertical;
//        outRect.bottom = 0;
    }
}
