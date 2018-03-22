/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.lots.travel.video.edit.effects.filter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aliyun.quview.CircularImageView;
import com.aliyun.struct.effect.EffectFilter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lots.travel.R;
import com.lots.travel.video.edit.effects.control.EffectInfo;
import com.lots.travel.video.edit.effects.control.OnItemClickListener;
import com.lots.travel.video.edit.effects.control.UIEditorPage;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private OnItemClickListener mItemClick;
    private int mSelectedPos = 0;
    private FilterViewHolder mSelectedHolder;
    private List<String> mFilterList = new ArrayList<>();

    public FilterAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.resources_item_view, parent, false);
        FilterViewHolder filterViewHolder = new FilterViewHolder(view);
        filterViewHolder.frameLayout = (FrameLayout) view.findViewById(R.id.resource_image);
        return filterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FilterViewHolder filterViewHolder = (FilterViewHolder) holder;
        String name = mContext.getString(R.string.none_effect);
        String path = mFilterList.get(position);
        if(path == null || "".equals(path)) {
            Glide.with(mContext).load(R.mipmap.none).into(new ViewTarget<CircularImageView, Drawable>(filterViewHolder.mImage) {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> glideAnimation) {
                    filterViewHolder.mImage.setImageBitmap(((BitmapDrawable) resource).getBitmap());
                }
            });
        } else {
            EffectFilter effectFilter = new EffectFilter(path);
            if(effectFilter != null) {
                name = effectFilter.getName();
                if(filterViewHolder != null) {
                    Glide.with(mContext).load(effectFilter.getPath() + "/icon.png").into(new ViewTarget<CircularImageView, Drawable>(filterViewHolder.mImage) {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            filterViewHolder.mImage.setImageBitmap(((BitmapDrawable) resource).getBitmap());
                        }
                    });
                }
            }
        }

        if(mSelectedPos > mFilterList.size()) {
            mSelectedPos = 0;
        }

        if(mSelectedPos == position) {
            filterViewHolder.mImage.setSelected(true);
            mSelectedHolder = filterViewHolder;
        } else {
            filterViewHolder.mImage.setSelected(false);
        }
        filterViewHolder.mName.setText(name);
        filterViewHolder.itemView.setTag(holder);
        filterViewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    private static class FilterViewHolder extends RecyclerView.ViewHolder{

        FrameLayout frameLayout;

        CircularImageView mImage;
        TextView mName;
        public FilterViewHolder(View itemView) {
            super(itemView);
            mImage = (CircularImageView) itemView.findViewById(R.id.resource_image_view);
            mName = (TextView) itemView.findViewById(R.id.resource_name);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClick = listener;
    }

    @Override
    public void onClick(View view) {
        if(mItemClick != null) {
            FilterViewHolder viewHolder = (FilterViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            if(mSelectedPos != position && mSelectedHolder != null) {
                mSelectedHolder.mImage.setSelected(false);
                viewHolder.mImage.setSelected(true);
                mSelectedPos = position;
                mSelectedHolder = viewHolder;

                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.FILTER_EFFECT;
                effectInfo.setPath(mFilterList.get(position));
                effectInfo.id = position;
                mItemClick.onItemClick(effectInfo, position);
            }
        }
    }

    public void setDataList(List<String> list) {
        mFilterList.clear();
        mFilterList.add(null);
        mFilterList.addAll(list);
    }

    public void setSelectedPos(int position) {
        mSelectedPos = position;
    }
}
