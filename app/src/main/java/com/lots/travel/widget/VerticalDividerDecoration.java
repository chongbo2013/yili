package com.lots.travel.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author： liyi
 * Date：    2016/11/28.
 */
public class VerticalDividerDecoration extends RecyclerView.ItemDecoration {
    private int lineWidth;

    private Rect tempRect;
    private Paint paint;

    public VerticalDividerDecoration(float width,int color){
        lineWidth = (int) (width+.5f);

        tempRect = new Rect();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int index = parent.getChildAdapterPosition(view);
        boolean isTopEdge = index==0;
        //boolean isBottomEdge = index==state.getItemCount();

        outRect.left = 0;
        outRect.top = isTopEdge ? 0:lineWidth;
        outRect.right = 0;
        outRect.bottom = 0;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i=0; i<childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            //int bottom = top + spaceVertical;
            //tempRect.set(left,top,right,bottom);
            //c.drawRect(tempRect,paint);
            c.drawLine(left,top,right,top,paint);
        }
    }

}
