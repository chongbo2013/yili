package com.lots.travel.place.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lots.travel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2017/10/16.
 */

public class MenuSpinnerAdapter extends BaseAdapter {
    private List<Item> itemList;

    public MenuSpinnerAdapter(){
        itemList = new ArrayList<>();
    }

    public void setItemList(List<Item> items){
        itemList.clear();
        itemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_address,parent,false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else
            holder = (Holder) convertView.getTag();

        Item item = itemList.get(position);

        holder.divider.setVisibility(position==0 ?
                View.INVISIBLE:View.VISIBLE);
        holder.tvName.setText(item.getName());

        return convertView;
    }

    class Holder{
        View divider;
        TextView tvName;

        Holder(View root){
            tvName = (TextView) root.findViewById(R.id.tv_name);
            divider = root.findViewById(R.id.divider);
        }

    }

    public static class Item{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
