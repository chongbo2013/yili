package com.lots.travel.recruit.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
import com.lots.travel.network.model.result.ExpenseTag;
import com.lots.travel.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2018/1/4.
 */
//旅费涉及的项目
public class ExpenseItemsView extends RecyclerView{
    private ExpenseItemsAdapter mItemsAdapter;
    private OnItemCheckChangedListener mOnItemCheckChangedListener;

    public ExpenseItemsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mItemsAdapter = new ExpenseItemsAdapter(context);
        setLayoutManager(new GridLayoutManager(context,4));
        setAdapter(mItemsAdapter);
    }

    public void setItemList(List<ExpenseItem> src){
        mItemsAdapter.setItemList(src);
    }

    public void setOnItemCheckChangedListener(OnItemCheckChangedListener listener){
        mOnItemCheckChangedListener = listener;
    }

    public void setItemEnabled(int position,boolean enabled){
        mItemsAdapter.setItemEnabled(position,enabled);
    }

    private class ExpenseItemsAdapter extends RecyclerView.Adapter<ExpenseItemsAdapter.Holder> {
        private Context context;
        private RequestOptions options;
        private List<ExpenseItem> itemList;
        private int dp40;
        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if(!v.isPressed())
                    return;
                Holder holder = (Holder) v.getTag();
                int position = holder.getAdapterPosition();
                setItemChecked(position,isChecked);
                if(mOnItemCheckChangedListener!=null)
                    mOnItemCheckChangedListener.onItemCheckChanged(position,isChecked);
            }
        };

        ExpenseItemsAdapter(Context ctx){
            context = ctx;
            itemList = new ArrayList<>();
            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH);
            dp40 = DensityUtil.dp2px(ctx,40);
        }

        void setItemList(List<ExpenseItem> src){
            itemList.clear();
            if(src!=null && src.size()!=0)
                itemList.addAll(src);
            notifyDataSetChanged();
        }

        public void setItemEnabled(int position,boolean enabled){
            ExpenseItem item = itemList.get(position);
            item.setEnabled(enabled);
            notifyItemChanged(position);
        }

        public void setItemChecked(int position,boolean checked){
            ExpenseItem item = itemList.get(position);
            item.setChecked(checked);
            notifyItemChanged(position);
        }

        public String getItemIcon(int position){
            ExpenseItem item = itemList.get(position);
            if(!item.isEnabled())
                return item.getLogoDisabled();
            return item.isChecked() ? item.getLogoOn():item.getLogo();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expense_item,parent,false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final ExpenseItem item = itemList.get(position);
            final CheckBox checkBox = (CheckBox) holder.itemView;
            checkBox.setEnabled(item.isEnabled());
            checkBox.setChecked(item.isChecked());
            checkBox.setText(item.getValue());

            Glide.with(context)
                    .load(getItemIcon(position))
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
            return itemList.size();
        }

        class Holder extends RecyclerView.ViewHolder{

            public Holder(View v) {
                super(v);
                ((CheckBox)v).setOnCheckedChangeListener(onCheckedChangeListener);
                v.setTag(this);
            }
        }

    }

    public static class ExpenseItem extends ExpenseTag implements Parcelable {
        private boolean checked;
        private boolean enabled;

        public ExpenseItem(ExpenseTag src){
            super(src);
            enabled = true;
            checked = false;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        protected ExpenseItem(Parcel in) {
            super(in);
            checked = in.readByte() != 0;
            enabled = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte((byte) (checked ? 1 : 0));
            dest.writeByte((byte) (enabled ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ExpenseItem> CREATOR = new Creator<ExpenseItem>() {
            @Override
            public ExpenseItem createFromParcel(Parcel in) {
                return new ExpenseItem(in);
            }

            @Override
            public ExpenseItem[] newArray(int size) {
                return new ExpenseItem[size];
            }
        };
    }

    public interface OnItemCheckChangedListener{
        void onItemCheckChanged(int position,boolean checked);
    }

}
