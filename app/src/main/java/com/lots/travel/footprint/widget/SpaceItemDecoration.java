package com.lots.travel.footprint.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nalanzi on 2017/11/19.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        outRect.left = pos!=0 ? space:0;
    }

}
