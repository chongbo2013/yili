package com.lots.travel.schedule.base;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.TripPart;

import java.util.Date;

/**
 * Created by nalanzi on 2017/12/25.
 */

public abstract class ScheduleHolder extends RecyclerView.ViewHolder {
    private Drawable leftIcon;
    protected final ScheduleAdapter mAdapter;

    public ScheduleHolder(ScheduleAdapter adapter,View v) {
        super(v);
        mAdapter = adapter;
        onCreate(v);
    }

    public void loadImage(String url, ImageView target){
        mAdapter.loadImage(url, target);
    }

    public String getString(int id){
        return mAdapter.getContext().getString(id);
    }

    public Drawable getDrawable(int id){
        return ContextCompat.getDrawable(mAdapter.getContext(),id);
    }

    public int getColor(int id){
        return ContextCompat.getColor(mAdapter.getContext(),id);
    }

    public Drawable getLeftIcon(){
        return leftIcon;
    }

    public final void setLeftIcon(Drawable drawable){
        leftIcon = drawable;
    }

    public final void setLeftIcon(int id){
        leftIcon = ContextCompat.getDrawable(mAdapter.getContext(),id);
    }

    public int getItemPosition(){
        return mAdapter.getItemPosition(getAdapterPosition());
    }

    public int getFlatPosition(){
        return getAdapterPosition();
    }

    public int getDay(){
        return mAdapter.getGroupPosition(getItemPosition())+1;
    }

    public Date getDate(){
        return mAdapter.getDateAt(getItemPosition()+1);
    }

    public TripPart getContent(){
        return mAdapter.getItemContent(getItemPosition());
    }

    public boolean isContentEmpty(){
        return mAdapter.isItemContentEmpty(getItemPosition());
    }

    public void updateContent(TripPart tripPart,boolean notify){
        mAdapter.updateItemContent(getItemPosition(),tripPart,notify);
    }

    public Object getExtraData(){
        return mAdapter.getItemExtraData(getItemPosition());
    }

    public void setExtraData(ExtraData data){
        mAdapter.setItemExtraData(getItemPosition(),data);
    }

    public Spot getSpot(){
        return mAdapter.spotAt(getItemPosition());
    }

    public Hotel getHotel(){
        return mAdapter.hotelAt(getItemPosition());
    }

    public Food getFood(){
        return mAdapter.foodAt(getItemPosition());
    }

    public void notifyChanged(){
        mAdapter.notifyItemChanged(getAdapterPosition());
    }

    public abstract void onCreate(View v);

    public abstract void onBind();

    public abstract void onRelease();

}
