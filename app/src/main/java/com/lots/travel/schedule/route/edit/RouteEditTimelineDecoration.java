package com.lots.travel.schedule.route.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class RouteEditTimelineDecoration extends RecyclerView.ItemDecoration {
    //4dp
    private int mDateMarginTop;
    //50dp
    private int mOtherMarginVertical;
    //25dp
    private int mTimelineMarginLeft;
    //25dp
    private int mIconSize;

    private Paint mLinePaint;

    /**
     ** @param lineWidth dp
     */
    public RouteEditTimelineDecoration(Context context, float lineWidth){
        final float scale = context.getResources().getDisplayMetrics().density;

        mDateMarginTop = (int) (4 * scale + 0.5f);
        mOtherMarginVertical = (int) (5 * scale + 0.5f);
        mTimelineMarginLeft = (int) (25 * scale + 0.5f);

        mIconSize = (int) (25 * scale + 0.5f);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(ContextCompat.getColor(context, R.color.color_divider));
        mLinePaint.setStrokeWidth(lineWidth * scale);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView rv, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();

        int radius = mIconSize/2;
        for (int i=0;i<rv.getChildCount();i++){
            View child = rv.getChildAt(i);
            ScheduleHolder holder = (ScheduleHolder) rv.getChildViewHolder(child);

            switch (holder.getItemViewType()){
                case Topology.TYPE_DATE:
                    canvas.drawLine(
                            mTimelineMarginLeft,
                            child.getTop(),
                            mTimelineMarginLeft,
                            layoutManager.getDecoratedBottom(child),
                            mLinePaint);
                    break;

                case Topology.TYPE_STAY:
                    canvas.drawLine(
                            mTimelineMarginLeft,
                            layoutManager.getDecoratedTop(child),
                            mTimelineMarginLeft,
                            child.getTop()+child.getHeight()/2,
                            mLinePaint);
                    break;

                default:
                    canvas.drawLine(
                            mTimelineMarginLeft,
                            layoutManager.getDecoratedTop(child),
                            mTimelineMarginLeft,
                            layoutManager.getDecoratedBottom(child),
                            mLinePaint);
                    break;
            }

            Drawable leftIcon = holder.getLeftIcon();
            if(leftIcon!=null){
                int cx = mTimelineMarginLeft;
                int cy = (child.getTop()+child.getBottom())/2;
                leftIcon.setBounds(cx-radius,cy-radius,cx+radius,cy+radius);
                leftIcon.draw(canvas);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView rv, RecyclerView.State state) {
        ScheduleHolder holder = (ScheduleHolder) rv.getChildViewHolder(view);

        if(holder==null)
            return;

        int type = holder.getItemViewType();
        switch (type){
            case Topology.TYPE_DATE:
                outRect.top = mOtherMarginVertical;
                outRect.bottom = mOtherMarginVertical;
                break;

            case Topology.TYPE_ADD:
            case Topology.TYPE_DESC:
            case Topology.TYPE_TRAFFIC:
                outRect.top = mOtherMarginVertical;
                outRect.bottom = mOtherMarginVertical;
                break;
        }

        if(holder.getAdapterPosition()==rv.getAdapter().getItemCount()-1)
            outRect.bottom = mOtherMarginVertical;
    }

}
