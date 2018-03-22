package com.lots.travel.main.setting.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.main.setting.model.ShippingAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2018/1/10.
 */

public class ShippingAddressListAdapter extends RecyclerView.Adapter<ShippingAddressListAdapter.Holder> {
    private RecyclerView rvContent;
    private List<ShippingAddress> mAddressList;
    private int mCheckedPosition = -1;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mOnItemClickListener==null)
                return;

            Holder holder = (Holder) v.getTag();
            int position = holder.getAdapterPosition();
            ShippingAddress item = mAddressList.get(position);

            switch (v.getId()){
                case R.id.iv_as_default:
                    boolean checked;
                    if(mCheckedPosition==-1)
                        checked = true;
                    else {
                        ShippingAddress checkedItem = mAddressList.get(mCheckedPosition);
                        checked = checkedItem != item;
                    }
                    mOnItemClickListener.onMakeDefault(position,checked);
                    break;

                case R.id.tv_edit:
                    mOnItemClickListener.onEdit(position,item);
                    break;

                case R.id.tv_delete:
                    mOnItemClickListener.onDelete(position);
                    break;
            }
        }
    };

    private OnItemClickListener mOnItemClickListener;

    public ShippingAddressListAdapter(RecyclerView recyclerView) {
        rvContent = recyclerView;
        mAddressList = new ArrayList<>();
    }

    public void setData(List<ShippingAddress> src){
        mAddressList.clear();
        mCheckedPosition = -1;
        if(src!=null && src.size()>0){
            for(int i=0;i<src.size();i++){
                ShippingAddress item = src.get(i);
                mAddressList.add(item);
                if(item.isAsDefault()) {
                    mCheckedPosition = i;
                    continue;
                }
                //保证只有一个默认地址，防止服务器数据错误导致其它问题
                item.setAsDefault(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public void makeDefault(int position,boolean checked){
        ShippingAddress oldItem = mCheckedPosition==-1 ? null:mAddressList.get(mCheckedPosition);
        ShippingAddress newItem = mAddressList.get(position);

        //如果点击的是同一个
        if(oldItem==newItem){
            mCheckedPosition = checked ? position:-1;
            newItem.setAsDefault(checked);
            notifyAsDefault(rvContent,position,checked);
        }
        //点击的是不同的
        else{
            if(oldItem!=null){
                oldItem.setAsDefault(false);
                //更新其ui
                notifyAsDefault(rvContent,mCheckedPosition,false);
                oldItem.setAsDefault(false);
                mCheckedPosition = position;
                //更新其ui
                newItem.setAsDefault(true);
                notifyAsDefault(rvContent,mCheckedPosition,true);
            }else{
                //实际上这里一定是true
                mCheckedPosition = checked ? position:-1;
                //更新其ui
                newItem.setAsDefault(checked);
                notifyAsDefault(rvContent,mCheckedPosition,checked);
            }
        }
    }

    public void removeItem(int position){
        ShippingAddress item = mAddressList.get(position);
        ShippingAddress checkedItem = mCheckedPosition==-1 ? null:mAddressList.get(mCheckedPosition);
        if(item==checkedItem)
            mCheckedPosition = -1;
        mAddressList.remove(position);
        notifyItemRemoved(position);
    }

    private void notifyAsDefault(RecyclerView rv,int position,boolean checked){
        Holder holder = (Holder) rv.findViewHolderForAdapterPosition(position);
        if(holder!=null){
            holder.ivAsDefault.setImageResource(
                    checked ? R.drawable.ico_check_circle_en:R.drawable.ico_check_circle_dis);
        }else{
            notifyItemChanged(position);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipping_address_list,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ShippingAddress item = mAddressList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPhone.setText(item.getPhone());

        StringBuilder strBuilder = new StringBuilder();
        String str = item.getProvince();
        if(!TextUtils.isEmpty(str)) strBuilder.append(str);

        str = item.getCity();
        if(!TextUtils.isEmpty(str)) strBuilder.append(str);

        str = item.getDistrict();
        if(!TextUtils.isEmpty(str)) strBuilder.append(str);

        if(strBuilder.length()>0) strBuilder.append(' ');

        str = item.getDetailAddress();
        if(!TextUtils.isEmpty(str)) strBuilder.append(str);

        holder.tvAddress.setText(strBuilder.toString());
        holder.ivAsDefault.setImageResource(
                position==mCheckedPosition ? R.drawable.ico_check_circle_en:R.drawable.ico_check_circle_dis);
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private TextView tvName,tvPhone;
        private TextView tvAddress;
        private ImageView ivAsDefault;

        public Holder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvPhone = (TextView) v.findViewById(R.id.tv_phone);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            ivAsDefault = (ImageView) v.findViewById(R.id.iv_as_default);
            ivAsDefault.setTag(this);
            ivAsDefault.setOnClickListener(mOnClickListener);
            TextView tvEdit = (TextView) v.findViewById(R.id.tv_edit);
            tvEdit.setTag(this);
            tvEdit.setOnClickListener(mOnClickListener);
            TextView tvDelete = (TextView) v.findViewById(R.id.tv_delete);
            tvDelete.setTag(this);
            tvDelete.setOnClickListener(mOnClickListener);
        }

    }

    public interface OnItemClickListener{
        void onMakeDefault(int position,boolean checked);
        void onEdit(int position,ShippingAddress item);
        void onDelete(int position);
    }

}
