package com.lots.travel.footprint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/11/18.
 */

public class FootprintTimelineDecoration extends RecyclerView.ItemDecoration {
    private int dp10,dp22_5;
    private Paint linePaint;

    public FootprintTimelineDecoration(Context context,float lineWidth){
        final float scale = context.getResources().getDisplayMetrics().density;
        dp10 = (int) (10 * scale + 0.5f);
        dp22_5 = (int) (22.5f * scale + 0.5f);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(ContextCompat.getColor(context, R.color.color_divider));
        linePaint.setStrokeWidth(lineWidth * scale);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView rv, RecyclerView.State state) {
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        outRect.top = pos==0 ? dp10:0;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView rv, RecyclerView.State state) {
        AbstractLoadAdapter adapter = (AbstractLoadAdapter) rv.getAdapter();
        int itemCount = adapter.getDataItemCount();

        if(itemCount==1)
            return;

        RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
        for (int i=0;i<rv.getChildCount();i++){
            View child = rv.getChildAt(i);
            RecyclerView.ViewHolder holder = rv.getChildViewHolder(child);
            if(holder!=null && holder instanceof AbstractLoadAdapter.HeaderFooterHolder) {
                continue;
            }
            int pos = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
            pos = adapter.getItemPosition(pos);

            if(pos==0){
                canvas.drawLine(dp22_5,child.getTop(),dp22_5,layoutManager.getDecoratedBottom(child),linePaint);
            }else if(pos!=itemCount-1){
                canvas.drawLine(dp22_5,child.getTop(),dp22_5,layoutManager.getDecoratedBottom(child),linePaint);
            }
//            else{
//                canvas.drawLine(dp22_5,child.getTop(),dp22_5,layoutManager.getDecoratedBottom(child),linePaint);
//            }

        }



    }

}
