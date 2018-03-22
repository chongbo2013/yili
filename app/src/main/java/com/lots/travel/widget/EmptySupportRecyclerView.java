package com.lots.travel.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author： liyi
 * Date：    2017/3/8.
 */
public class EmptySupportRecyclerView extends RecyclerView {
    private View emptyView;
    private int extraCount;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            changed();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            changed();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            changed();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            changed();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            changed();
        }

        @Override
        public void onChanged() {
            changed();
        }

    };

    private void changed(){
        Adapter adapter =  getAdapter();

        if(adapter != null && emptyView != null){
            emptyView.setVisibility(adapter.getItemCount()==extraCount ? View.VISIBLE:View.INVISIBLE);
        }
    }

    public EmptySupportRecyclerView(Context context) {
        super(context);
    }

    public EmptySupportRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySupportRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null)
            adapter.registerAdapterDataObserver(emptyObserver);

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        setEmptyView(emptyView,0);
    }

    public void setEmptyView(View emptyView,int extraCount) {
        this.emptyView = emptyView;
        this.extraCount = extraCount;
    }

}
