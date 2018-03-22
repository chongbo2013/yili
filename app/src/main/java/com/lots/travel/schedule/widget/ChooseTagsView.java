package com.lots.travel.schedule.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lots.travel.R;
import com.lots.travel.network.model.result.TravelTag;
import com.lots.travel.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2018/1/19.
 */

public class ChooseTagsView extends RecyclerView {
    private int mMaxCheckedCount;

    private List<TravelTag> mCheckedTags;

    private List<TravelTag> mTags;
    private TagsAdapter mTagsAdapter;

    private RequestOptions mOptions;
    private int mDp40;

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag();
            int position = holder.getAdapterPosition();
            TravelTag tag = mTags.get(position);
            boolean oldValue = mCheckedTags.contains(tag);
            boolean newValue;
            int checkedCount = mCheckedTags.size();
            if(oldValue || checkedCount<mMaxCheckedCount) {
                newValue = !oldValue;
                if(newValue)
                    mCheckedTags.add(tag);
                else
                    mCheckedTags.remove(tag);
                mTagsAdapter.notifyItemChanged(position);
            }
        }
    };

    public ChooseTagsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mMaxCheckedCount = 2;

        mCheckedTags = new ArrayList<>();
        mTags = new ArrayList<>();

        mOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
        mDp40 = DensityUtil.dp2px(context,40);

        GridLayoutManager layoutManager = new GridLayoutManager(context,3);
        setLayoutManager(layoutManager);

        mTagsAdapter = new TagsAdapter();
        setAdapter(mTagsAdapter);
    }

    public void setTags(List<TravelTag> src){
        mTags.clear();
        if(src!=null && src.size()>0){
            mTags.addAll(src);
        }
        mTagsAdapter.notifyDataSetChanged();
    }

    public List<TravelTag> getCheckedTags(){
        return mCheckedTags;
    }

    public String getCheckedTagsString(){
        StringBuilder strBuilder = new StringBuilder();
        for (int i=0;i<mCheckedTags.size();i++){
            TravelTag tag = mCheckedTags.get(i);
            if(i!=0)
                strBuilder.append(',');
            strBuilder.append(tag.getValue());
        }
        return strBuilder.toString();
    }

    private class TagsAdapter extends RecyclerView.Adapter<Holder>{

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_tag,parent,false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            TravelTag tag = mTags.get(position);
            final TextView tvTag = (TextView) holder.itemView;
            tvTag.setText(tag.getValue());

            boolean checked = mCheckedTags.contains(tag);

            Glide.with(getContext())
                    .load(checked ? tag.getLogoOn():tag.getLogo())
                    .apply(mOptions)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                            if(drawable==null)
                                return;

                            drawable.setBounds(0,0,mDp40,mDp40);
                            tvTag.setCompoundDrawables(null,drawable,null,null);
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return mTags.size();
        }
    }

    class Holder extends ViewHolder {

        Holder(View v) {
            super(v);
            v.setTag(this);
            v.setOnClickListener(mOnClickListener);
        }

    }

}
