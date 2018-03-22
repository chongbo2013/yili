/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.lots.travel.video.edit.effects.caption;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.aliyun.quview.CircularImageView;
import com.aliyun.struct.form.ResourceForm;
import com.bumptech.glide.request.transition.Transition;
import com.lots.travel.R;
import com.lots.travel.video.edit.effects.control.EffectInfo;
import com.lots.travel.video.edit.effects.control.OnItemClickListener;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private OnItemClickListener mItemClick;
    private ArrayList<ResourceForm> data = new ArrayList<>();
    private OnMoreClickListener mMoreClickListener;
    private int mSelectedPosition = 0;
    private static final int VIEW_TYPE_SELECTED = 1;
    private static final int VIEW_TYPE_UNSELECTED = 2;

    public CategoryAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(ArrayList<ResourceForm> data) {
        if (data == null) {
            return;
        }
        this.data = data;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item_view, parent, false);
        CategoryViewHolder filterViewHolder = new CategoryViewHolder(view);
        filterViewHolder.frameLayout = (FrameLayout) view.findViewById(R.id.category_image);
        return filterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        ResourceForm form = data.get(position);
        int viewType = getItemViewType(position);
        if (form.isMore()) {
            categoryViewHolder.mImage.setImageResource(R.mipmap.more);
        } else {
            Glide.with(mContext).load(form.getIcon()).into(new ViewTarget<CircularImageView,Drawable>(categoryViewHolder.mImage) {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    categoryViewHolder.mImage.setImageBitmap(((BitmapDrawable) resource).getBitmap());
                }
            });
        }
        categoryViewHolder.itemView.setTag(holder);
        categoryViewHolder.itemView.setOnClickListener(this);
        switch (viewType) {
            case VIEW_TYPE_SELECTED:
                categoryViewHolder.itemView.setSelected(true);
                break;
            case VIEW_TYPE_UNSELECTED:
                categoryViewHolder.itemView.setSelected(false);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void selectPosition(int categoryIndex) {
        int lasPos = mSelectedPosition;
        mSelectedPosition = categoryIndex;
        notifyItemChanged(mSelectedPosition);
        notifyItemChanged(lasPos);
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder{

        FrameLayout frameLayout;
        CircularImageView mImage;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            mImage = (CircularImageView) itemView.findViewById(R.id.category_image_source);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClick = listener;
    }

    @Override
    public void onClick(View view) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        ResourceForm form = data.get(position);
        if (form.isMore()) {
            if (mMoreClickListener != null) {
                mMoreClickListener.onMoreClick();
            }
        } else {
            if (mItemClick != null) {
                EffectInfo effectInfo = new EffectInfo();
                effectInfo.isCategory = true;
                int lastPos = mSelectedPosition;
                mSelectedPosition = viewHolder.getAdapterPosition();
                mItemClick.onItemClick(effectInfo, viewHolder.getAdapterPosition());
                notifyItemChanged(lastPos);
                notifyItemChanged(mSelectedPosition);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mSelectedPosition) {
            return VIEW_TYPE_SELECTED;
        }else {
            return VIEW_TYPE_UNSELECTED;
        }
    }

    public void setMoreClickListener(OnMoreClickListener moreClickListener) {
        mMoreClickListener = moreClickListener;
    }

    public interface OnMoreClickListener {
        void onMoreClick();
    }
}
