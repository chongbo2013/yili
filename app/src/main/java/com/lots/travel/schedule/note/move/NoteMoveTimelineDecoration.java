package com.lots.travel.schedule.note.move;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.move.MoveScheduleAdapter;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class NoteMoveTimelineDecoration extends RecyclerView.ItemDecoration {
    private int mMarginVertical;
    private Paint mLinePaint;

    private int mTimelineMarginLeft;

    public NoteMoveTimelineDecoration(Context context, float lineWidth){
        final float scale = context.getResources().getDisplayMetrics().density;

        mMarginVertical = (int) (2 * scale + 0.5f);
        mTimelineMarginLeft = (int) (22 * scale + 0.5f);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(ContextCompat.getColor(context, R.color.color_divider));
        mLinePaint.setStrokeWidth(lineWidth * scale);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = mMarginVertical;
        outRect.top = mMarginVertical;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView rv, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();

        for (int i=0;i<rv.getChildCount();i++){
            View child = rv.getChildAt(i);
            RecyclerView.ViewHolder holder = rv.getChildViewHolder(child);

            int type = holder.getItemViewType();
            if(type== Topology.TYPE_DAY){
                canvas.drawLine(
                        mTimelineMarginLeft,
                        child.getTop(),
                        mTimelineMarginLeft,
                        layoutManager.getDecoratedBottom(child),
                        mLinePaint);
            }else {
                int pos = holder.getAdapterPosition();
                if(pos==-1)
                    return;

                MoveScheduleAdapter adapter = (MoveScheduleAdapter) rv.getAdapter();
                int top,bottom;
                top = layoutManager.getDecoratedTop(child);
                pos = adapter.getItemPosition(pos);
                if(adapter.isGroupLastComponent(pos)){
                    bottom = child.getTop()+child.getHeight()/2;
                }else{
                    bottom = layoutManager.getDecoratedBottom(child);
                }
                canvas.drawLine(
                        mTimelineMarginLeft,
                        top,
                        mTimelineMarginLeft,
                        bottom,
                        mLinePaint);
            }
        }
    }

}
