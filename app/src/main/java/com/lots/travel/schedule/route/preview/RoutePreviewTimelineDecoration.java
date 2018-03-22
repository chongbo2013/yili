package com.lots.travel.schedule.route.preview;

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
import com.lots.travel.schedule.base.preview.PreviewScheduleAdapter;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RoutePreviewTimelineDecoration extends RecyclerView.ItemDecoration {
    private int dp4,dp10,dp50,dp25;
    private int timeline;
    private Paint linePaint;

    public RoutePreviewTimelineDecoration(Context context, float lineWidth){
        final float scale = context.getResources().getDisplayMetrics().density;
        dp4 = (int) (4 * scale + 0.5f);
        dp10 = (int) (10 * scale + 0.5f);
        dp25 = (int) (25 * scale + 0.5f);
        dp50 = (int) (50 * scale + 0.5f);

        timeline = (int) (25 * scale + 0.5f);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(ContextCompat.getColor(context, R.color.color_divider));
        linePaint.setStrokeWidth(lineWidth * scale);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView rv, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, rv, state);

        PreviewScheduleAdapter adapter = (PreviewScheduleAdapter) rv.getAdapter();

        int flatPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int itemPosition = adapter.getItemPosition(flatPosition);
        if(itemPosition<0)
            return;
        ScheduleHolder holder = (ScheduleHolder) rv.getChildViewHolder(view);

        switch (holder.getItemViewType()){
            case Topology.TYPE_DATE:
                outRect.top = dp4;
                outRect.bottom = dp4;
                outRect.left = dp10;
                outRect.right = dp10;
                break;

            case Topology.TYPE_DESC:
            case Topology.TYPE_SPOT:
            case Topology.TYPE_HOTEL:
            case Topology.TYPE_FOOD:
            case Topology.TYPE_STAY:
                int relPos = adapter.getChildRelativePosition(holder);
                //不是date后面的后继，且前驱为component，top存在为10dp
                outRect.top = relPos!=1 && adapter.isComponent(holder) ? dp10:0;
                outRect.left = dp50;
                outRect.right = dp10;
                break;

            case Topology.TYPE_TRAFFIC:
                outRect.left = dp50;
                outRect.right = dp10;
                break;
        }

        outRect.bottom = adapter.isBottom(holder) ? dp10:0;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView rv, RecyclerView.State state) {
        super.onDraw(canvas, rv, state);

        PreviewScheduleAdapter adapter = (PreviewScheduleAdapter) rv.getAdapter();

        for (int i=0;i<rv.getChildCount();i++){
            View child = rv.getChildAt(i);
            ScheduleHolder holder = (ScheduleHolder) rv.getChildViewHolder(child);
            RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            int itemPosition = holder.getItemPosition();
            Drawable leftIcon;

            if(itemPosition<0)
                continue;

            switch (holder.getItemViewType()){
                case Topology.TYPE_DATE:
                    canvas.drawLine(
                            timeline,
                            itemPosition!=0 ? layoutManager.getDecoratedTop(child):child.getTop(),
                            timeline,
                            itemPosition==adapter.getItemCount()-1 ? child.getBottom():layoutManager.getDecoratedBottom(child),
                            linePaint);
                    break;

                case Topology.TYPE_DESC:
                case Topology.TYPE_SPOT:
                case Topology.TYPE_HOTEL:
                case Topology.TYPE_FOOD:
                case Topology.TYPE_STAY:
                    boolean isBottom = adapter.isBottom(holder);
                    canvas.drawLine(
                            timeline,
                            layoutManager.getDecoratedTop(child),
                            timeline,
                            isBottom ? (child.getTop()+child.getBottom())/2:child.getBottom(),
                            linePaint);
                    leftIcon = holder.getLeftIcon();
                    if(leftIcon!=null){
                        int cx = timeline;
                        int cy = (child.getTop()+child.getBottom())/2;
                        leftIcon.setBounds(cx-dp25/2,cy-dp25/2,cx+dp25/2,cy+dp25/2);
                        leftIcon.draw(canvas);
                    }
                    break;

                case Topology.TYPE_TRAFFIC:
                    canvas.drawLine(timeline,child.getTop(),timeline,child.getBottom(),linePaint);
                    leftIcon = holder.getLeftIcon();
                    if(leftIcon!=null){
                        int cx = timeline;
                        int cy = (child.getTop()+child.getBottom())/2;
                        leftIcon.setBounds(cx-dp25/2,cy-dp25/2,cx+dp25/2,cy+dp25/2);
                        leftIcon.draw(canvas);
                    }
                    break;
            }
        }
    }

}
