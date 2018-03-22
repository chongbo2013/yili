package com.lots.travel.schedule.note.detail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.detail.route.RouteDetailAdapter;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailTimelineDecoration extends RecyclerView.ItemDecoration {
    private int dp4,dp10,dp50,dp25;
    private int timeline;
    private Paint linePaint;

    public RouteDetailTimelineDecoration(Context context, float lineWidth){
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

        RouteDetailAdapter adapter = (RouteDetailAdapter) rv.getAdapter();
        int flatPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int itemPosition = adapter.getItemPosition(flatPosition);
        if(itemPosition<0 || itemPosition>=adapter.getChildCount())
            return;

        Group group = adapter.getGroup(itemPosition);
        int childPosition = itemPosition-group.getFlatPosition();

        if (childPosition==0) {
            outRect.left = dp10;
            outRect.right = dp10;
            outRect.top = dp4;
            outRect.bottom = dp4;
        }else{
            outRect.left = dp50;
            outRect.right = dp10;
            outRect.top = childPosition==1 ? 0:dp4;
            outRect.bottom = dp4;
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView rv, RecyclerView.State state) {
        super.onDraw(canvas, rv, state);

        RouteDetailAdapter adapter = (RouteDetailAdapter) rv.getAdapter();

        for (int i=0;i<rv.getChildCount();i++) {
            View child = rv.getChildAt(i);
            ScheduleHolder holder = (ScheduleHolder) rv.getChildViewHolder(child);
            RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            int itemPosition = holder.getItemPosition();

            if (itemPosition<0 || itemPosition>=adapter.getChildCount())
                continue;

            Group group = adapter.getGroup(itemPosition);
            int childPosition = itemPosition - group.getFlatPosition();
            int groupSize = group.getChildSize();

            Drawable leftIcon;

            if (childPosition == 0) {
                canvas.drawLine(
                        timeline,
                        child.getTop(),
                        timeline,
                        childPosition == groupSize - 1 ? child.getBottom() : layoutManager.getDecoratedBottom(child),
                        linePaint);
            } else {
                canvas.drawLine(
                        timeline,
                        layoutManager.getDecoratedTop(child),
                        timeline,
                        childPosition==groupSize-1 ? (child.getTop() + child.getBottom()) / 2 : layoutManager.getDecoratedBottom(child),
                        linePaint);
                leftIcon = holder.getLeftIcon();
                if (leftIcon != null) {
                    int cx = timeline;
                    int cy = (child.getTop() + child.getBottom()) / 2;
                    leftIcon.setBounds(cx - dp25 / 2, cy - dp25 / 2, cx + dp25 / 2, cy + dp25 / 2);
                    leftIcon.draw(canvas);
                }
            }
        }
    }

}
