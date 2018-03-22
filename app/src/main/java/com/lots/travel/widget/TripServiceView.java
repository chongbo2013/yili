package com.lots.travel.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.lots.travel.R;
import com.lots.travel.network.model.result.TripServiceTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2018/1/4.
 */

public class TripServiceView extends ExpandableGridView{
    private List<TripServiceView.Item> tags;

    public TripServiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tags = new ArrayList<>();
        TripServiceAdapter adapter = new TripServiceAdapter(context);
        setAdapter(adapter);
    }

    public void setTags(List<TripServiceView.Item> src){
        tags.clear();
        if(src!=null && src.size()>0)
            tags.addAll(src);
        getAdapter().notifyDataSetChanged();
    }

    public String getSelectedTags(){
        StringBuilder strBuilder = new StringBuilder();
        int count = 0;
        for (int i=0;i<tags.size();i++){
            Item tag = tags.get(i);
            if(!tag.checked)
                continue;
            if(count!=0)
                strBuilder.append(",");
            ++count;
            strBuilder.append(tag.getPx());
        }
        return strBuilder.toString();
    }

    private class TripServiceAdapter extends ExpandableGridView.GridAdapter {

        TripServiceAdapter(Context ctx) {
            super(ctx);
        }

        @Override
        public boolean isItemChecked(int position) {
            return tags.get(position).checked;
        }

        @Override
        public void setItemChecked(int position, boolean checked) {
            tags.get(position).checked = checked;
        }

        @Override
        public String getItemLabel(int position) {
            return tags.get(position).getValue();
        }

        @Override
        public String getItemIcon(int position, boolean checked) {
            Item item = tags.get(position);
            return checked ? item.getLogoOn():item.getLogo();
        }

        @Override
        public int getDataCount() {
            return tags.size();
        }
    }

    public static class Item extends TripServiceTag implements Parcelable{
        private boolean checked;

        public Item(TripServiceTag tag){
            super(tag);
            checked = false;
        }

        protected Item(Parcel in) {
            super(in);
            checked = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte((byte) (checked ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }

}
