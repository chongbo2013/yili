package com.lots.travel.place;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.refresh.PagedItemFragment;
import com.lots.travel.widget.refresh.SelectItemsAdapter;

import java.util.List;

/**
 * Created by nalanzi on 2017/11/3.
 */

public abstract class ChooseComponentsFragment<T> extends PagedItemFragment<T> {
    private CharSequence searchKey;

    public abstract void setOnSelectChangedListener(SelectItemsAdapter.OnSelectChangedListener listener);

    public abstract boolean hasSelectedItems();

    public abstract List<T> getSelectedItems();

    @Override
    public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
        super.onConfigureDisplay(configureAdapter);
        configureAdapter.addItemDecoration(new SpaceItemDecoration(getContext()));
    }

    public void setSearchKey(CharSequence key){
        searchKey = key;
    }

    public CharSequence getSearchKey(){
        return searchKey==null ? "":searchKey;
    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int DP10;

        SpaceItemDecoration(Context context){
            DP10 = DensityUtil.dp2px(context,10);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            outRect.left = DP10;
            outRect.right = DP10;
            outRect.top = pos==0 ? DP10:0;
            outRect.bottom = DP10;
        }
    }

}
