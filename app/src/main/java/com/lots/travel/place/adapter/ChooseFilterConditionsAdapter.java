package com.lots.travel.place.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.place.model.Condition;

/**
 * Created by nalanzi on 2017/9/3.
 */

public class ChooseFilterConditionsAdapter extends RecyclerView.Adapter<ChooseFilterConditionsAdapter.Holder>{
    private Condition[] mConditions;
    private OnItemClickListener mOnItemClickListener;

    public ChooseFilterConditionsAdapter(){
        mConditions = Condition.getConditions();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_filter_conditions,parent,false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Condition condition = mConditions[position];
        holder.tvText.setText(condition.getName());
        holder.divider.setVisibility(position==0 ?
                View.INVISIBLE:View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mConditions.length;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvText;
        View divider;

        Holder(View itemView) {
            super(itemView);

            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            divider = itemView.findViewById(R.id.divider);

            tvText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener!=null)
                mOnItemClickListener.onItemClick(mConditions[getAdapterPosition()]);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Condition condition);
    }

}
