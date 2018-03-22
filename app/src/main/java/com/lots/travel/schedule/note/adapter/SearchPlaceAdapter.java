package com.lots.travel.schedule.note.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.store.db.ViewCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2017/10/31.
 */

public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.Holder> {
    private List<ViewCity> spotList;

    private OnItemClickListener onItemClickListener;

    public SearchPlaceAdapter(){
        spotList = new ArrayList<>();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag();
            if(onItemClickListener!=null)
                onItemClickListener.onItemClick(spotList.get(holder.getAdapterPosition()));
        }
    };

    public void setSpotList(List<ViewCity> src){
        spotList.clear();
        if(src!=null)
            spotList.addAll(src);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_place,parent,false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ViewCity spot = spotList.get(position);
        holder.tvName.setText(spot.getName());
        holder.tvCountry.setText(spot.getCountry());
    }

    @Override
    public int getItemCount() {
        return spotList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tvName,tvCountry;

        public Holder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvCountry = (TextView) v.findViewById(R.id.tv_country);
            v.setTag(this);
            v.setOnClickListener(onClickListener);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(ViewCity spot);
    }

}
