package com.lots.travel.schedule.base.move;

import android.app.Activity;

import com.lots.travel.schedule.base.Child;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;

import java.util.List;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class MoveScheduleAdapter extends ScheduleAdapter {
    private OnScheduleListener mOnScheduleListener;

    public MoveScheduleAdapter(Activity context, Topology topology, DataManager dataManager, HolderFactory holderFactory) {
        super(context, topology, dataManager, holderFactory);
    }

    public void setOnScheduleListener(OnScheduleListener listener){
        mOnScheduleListener = listener;
    }

    public OnScheduleListener getOnScheduleListener(){
        return mOnScheduleListener;
    }

    public boolean isGroupLastComponent(int itemPosition){
        return ((MoveTopology)mTopology).isGroupLastComponent(itemPosition);
    }

    public boolean canMove(int pos){
        int type = getItemViewType(pos);
        return type!= Topology.TYPE_DAY && type!=Topology.TYPE_TRAFFIC;
    }

    public boolean canSwap(int p1,int p2){
        int iP2 = getItemPosition(p2);
        if(iP2==0)
            return false;

        Child child = mTopology.getChildAt(iP2);
        int type = child.getType();
        return type!=Topology.TYPE_TRAFFIC;
    }

    public void swap(int p1,int p2){
        ((MoveTopology)mTopology).move(getItemPosition(p1),getItemPosition(p2));
    }

    public void addSpots(int groupPos,List<Spot> spots){
        if(spots!=null && spots.size()>0){
            Group group = mTopology.getGroupAt(groupPos);

            for (int i=0;i<spots.size();i++){
                long partId = mDataManager.addSpot(group.getPosition()+1,spots.get(i),false);
                ((MoveTopology)mTopology).addComponent(group.getPosition(),Topology.TYPE_SPOT,partId);
            }
        }
    }

    public void addHotels(int groupPos,List<Hotel> hotels){
        if(hotels!=null && hotels.size()>0){
            Group group = mTopology.getGroupAt(groupPos);

            for (int i=0;i<hotels.size();i++){
                long partId = mDataManager.addHotel(group.getPosition()+1,hotels.get(i),false,false);
                ((MoveTopology)mTopology).addComponent(group.getPosition(),Topology.TYPE_HOTEL,partId);
            }
        }
    }

    public void addFoods(int groupPos,List<Food> foods){
        if(foods!=null && foods.size()>0){
            Group group = mTopology.getGroupAt(groupPos);

            for (int i=0;i<foods.size();i++){
                long partId = mDataManager.addFood(group.getPosition()+1,foods.get(i),false);
                ((MoveTopology)mTopology).addComponent(group.getPosition(),Topology.TYPE_FOOD,partId);
            }
        }
    }

    public void deleteComponent(int pos){
        long[] oldKeys = ((MoveTopology)mTopology).deleteComponent(pos);
        if(oldKeys!=null && oldKeys.length!=0){
            for (long oldKey : oldKeys) {
                mDataManager.remove(oldKey, false);
            }
        }
    }

}
