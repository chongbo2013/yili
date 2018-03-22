package com.lots.travel.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by nalanzi on 2017/12/17.
 */
//宽度取RecyclerView宽，高度取测量总高度 - 后面换成ListView
public class WholeGridLayoutManager extends GridLayoutManager {

    public WholeGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        //测量每一行的高度，取其最高
        int spanCount = getSpanCount();
        int itemCount = Math.min(state.getItemCount(),getItemCount());
        int childHeight;
        int totalHeight = 0;

        for(int i=0;i<itemCount;i+=spanCount){
            int maxSpan = Math.min(i+spanCount,itemCount);
            childHeight = 0;
            for (int j=i;j<maxSpan;j++){
                View child = null;
                try{
                    child = recycler.getViewForPosition(j);
                }catch (Exception e){
                    Log.d(WholeGridLayoutManager.class.getName(),"RecyclerView Bug");
                }
                if(child==null)
                    continue;
                measureChildWithMargins(child,0,0);
                childHeight = Math.max(childHeight,getDecoratedMeasuredHeight(child));
            }
            totalHeight += childHeight;
        }

        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec),totalHeight);
    }

}
