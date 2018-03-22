package com.lots.travel.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lots.travel.R;
import com.lots.travel.recruit.widget.ExpenseItemsView;
import com.lots.travel.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用CheckBox作为item，正确的做法应该是点击修改checked数据，然后notify。后续单独对其封装
 * 需要的是点击以后不自行改变背景的CheckBox
 * 这里只是改变了背景而不是布局，所以不会对rv显示有影响.
 */
public class ExpandableGridView extends RecyclerView {
    private boolean expanded;
    private int spanCount;
    private GridAdapter gridAdapter;

    public ExpandableGridView(Context context) {
        this(context,null);
    }

    public ExpandableGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        spanCount = 4;
        GridLayoutManager layoutManager = new GridLayoutManager(context,spanCount);
        setLayoutManager(layoutManager);

        expanded = false;
    }

    public void setAdapter(GridAdapter adapter) {
        super.setAdapter(adapter);
        gridAdapter = adapter;
        gridAdapter.setGridView(this);
    }

    public GridAdapter getAdapter() {
        return gridAdapter;
    }

    public void setExpanded(boolean newState){
        if(expanded==newState)
            return;

        Adapter adapter = getAdapter();
        int oldCount = adapter.getItemCount();
        expanded = newState;

        int newCount = adapter.getItemCount();
        if((expanded && newCount>spanCount) ||
                (!expanded && oldCount>spanCount)){
            adapter.notifyDataSetChanged();
        }
    }

    public void turnExpandState(){
        setExpanded(!expanded);
    }

    public static abstract class GridAdapter extends Adapter<GridAdapter.Holder>{
        private Context context;
        private int dp40;
        private RequestOptions options;
        private ExpandableGridView gridView;
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                int position = holder.getAdapterPosition();
                boolean oldValue = isItemChecked(position);
                setItemChecked(position,!oldValue);
                notifyItemChanged(position);
            }
        };

        public GridAdapter(Context ctx){
            context = ctx;
            dp40 = DensityUtil.dp2px(context,40);
            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH);
        }

        void setGridView(ExpandableGridView v){
            gridView = v;
        }

        public abstract boolean isItemChecked(int position);

        public abstract void setItemChecked(int position,boolean checked);

        public abstract String getItemLabel(int position);

        public abstract String getItemIcon(int position,boolean checked);

        public abstract int getDataCount();

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_service,parent,false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            boolean checked = isItemChecked(position);
            final CheckBox checkBox = (CheckBox)holder.itemView;
            checkBox.setChecked(checked);
            checkBox.setText(getItemLabel(position));

            Glide.with(context)
                    .load(getItemIcon(position,checked))
                    .apply(options)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                            if(drawable==null)
                                return;
                            drawable.setBounds(0,0,dp40,dp40);
                            checkBox.setCompoundDrawables(null,drawable,null,null);
                        }
                    });
        }

        @Override
        public int getItemCount() {
            int gridSize = getDataCount();
            return gridView.expanded ? gridSize:Math.min(gridSize,gridView.spanCount);
        }

        public class Holder extends ViewHolder{

            Holder(View v) {
                super(v);
                v.setTag(this);
                v.setOnClickListener(onClickListener);
            }

        }
    }

}
