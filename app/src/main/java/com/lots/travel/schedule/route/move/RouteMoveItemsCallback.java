package com.lots.travel.schedule.route.move;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.lots.travel.schedule.base.move.MoveScheduleAdapter;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteMoveItemsCallback extends ItemTouchHelper.Callback {
    private MoveScheduleAdapter mScheduleAdapter;

    public RouteMoveItemsCallback(MoveScheduleAdapter adapter){
        mScheduleAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags;
        //标题不移动
        if (mScheduleAdapter.canMove(viewHolder.getAdapterPosition())) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        } else {
            dragFlags = 0;
        }
        final int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int sourcePosition = source.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();
        if(mScheduleAdapter!=null
                && mScheduleAdapter.canSwap(sourcePosition, targetPosition)){
            mScheduleAdapter.swap(sourcePosition, targetPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setSelected(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setSelected(false);
    }

}
