package com.lots.travel.schedule.base.edit;

import android.app.Activity;

import com.lots.travel.schedule.widget.AudioPlayer;
import com.lots.travel.schedule.widget.AudioRecorder;
import com.lots.travel.schedule.base.Child;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.move.MoveTopology;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.TripPart;

import java.util.List;

/**
 * Created by nalanzi on 2017/12/25.
 * 涉及到spot、food等的操作
 */
public class EditScheduleAdapter extends ScheduleAdapter {
    private AudioPlayer mAudioPlayer;
    private AudioRecorder mAudioRecorder;
    private OnScheduleListener mOnScheduleListener;

    public EditScheduleAdapter(Activity context, Topology topology, DataManager dataManager, HolderFactory<EditScheduleAdapter> holderFactory) {
        super(context, topology, dataManager, holderFactory);
        mAudioRecorder = new AudioRecorder();
        mAudioPlayer = new AudioPlayer();
    }

    public AudioPlayer getAudioPlayer(){
        return mAudioPlayer;
    }

    public AudioRecorder getAudioRecorder(){
        return mAudioRecorder;
    }

    public void setOnScheduleListener(OnScheduleListener listener){
        mOnScheduleListener = listener;
    }

    public OnScheduleListener getOnScheduleListener(){
        return mOnScheduleListener;
    }

    public void update(DataManager dataManager, MoveTopology topology){
        mDataManager = dataManager;
        //根据MoveTopology的结构进行组织
        List<Long> oldKeys = ((EditTopology)mTopology).update(topology);
        for (long key:oldKeys){
            mDataManager.remove(key,true);
        }
    }

    public void setDescContent(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        Group group = mTopology.getGroupAt(child.getGroup());
        long tripKey = mDataManager.addDesc(group.getPosition()+1,true);
        mTopology.setChildContent(itemPosition,tripKey);
    }

    public void addSpots(int insertPosition,List<Spot> spots){
        if(spots==null || spots.size()==0)
            return;

        Child child = mTopology.getChildAt(insertPosition);
        Group group = mTopology.getGroupAt(child.getGroup());

        for (int i=0;i<spots.size();i++){
            long tripKey = mDataManager.addSpot(group.getPosition()+1,spots.get(i),true);
            long[] oldKeys = ((EditTopology)mTopology).addComponent(group.getPosition(), EditTopology.TYPE_SPOT,tripKey);
            if (oldKeys!=null && oldKeys.length>0){
                for (long oldKey : oldKeys) {
                    mDataManager.remove(oldKey, true);
                }
            }
        }
    }

    public void addHotels(int insertPosition,List<Hotel> hotels){
        if(hotels==null || hotels.size()==0)
            return;

        Child child = mTopology.getChildAt(insertPosition);
        Group group = mTopology.getGroupAt(child.getGroup());

        for (int i=0;i<hotels.size();i++){
            long tripKey = mDataManager.addHotel(group.getPosition()+1,hotels.get(i),false,true);
            long[] oldKeys = ((EditTopology)mTopology).addComponent(group.getPosition(), EditTopology.TYPE_HOTEL,tripKey);
            if (oldKeys!=null && oldKeys.length>0){
                for (long oldKey : oldKeys) {
                    mDataManager.remove(oldKey, true);
                }
            }
        }
    }

    public void addFoods(int insertPosition,List<Food> foods){
        if(foods==null || foods.size()==0)
            return;

        Child child = mTopology.getChildAt(insertPosition);
        Group group = mTopology.getGroupAt(child.getGroup());

        for (int i=0;i<foods.size();i++){
            long partId = mDataManager.addFood(group.getPosition()+1,foods.get(i),true);
            long[] oldKeys = ((EditTopology)mTopology).addComponent(group.getPosition(), EditTopology.TYPE_FOOD,partId);
            if (oldKeys!=null && oldKeys.length>0){
                for (long oldKey : oldKeys) {
                    mDataManager.remove(oldKey, true);
                }
            }
        }
    }

    public void removeComponent(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        Group group = mTopology.getGroupAt(child.getGroup());
        long[] oldKeys;
        //stay
        if(itemPosition==group.getFlatPosition()+group.getChildSize()-1){
            oldKeys = ((EditTopology)mTopology).updateStay(group.getPosition(),-1);
        }else{
            oldKeys = ((EditTopology)mTopology).removeComponent(itemPosition);
        }
        if(oldKeys!=null && oldKeys.length>0){
            for (long oldKey : oldKeys) {
                mDataManager.remove(oldKey, true);
            }
        }
    }

    public void setTrafficContent(int pos,String mode,int hour,int minute){
        Child child = mTopology.getChildAt(pos);
        Group group = mTopology.getGroupAt(child.getGroup());
        long partId = mDataManager.addTraffic(group.getPosition()+1,mode,hour,minute,true);
        mTopology.setChildContent(pos,partId);
    }

    public void clearTrafficContent(int pos){
        long key = mTopology.setChildContent(pos,-1);
        mDataManager.remove(key,true);
    }

    public void setStayContent(int pos,Hotel stay){
        Child child = mTopology.getChildAt(pos);
        Group group = mTopology.getGroupAt(child.getGroup());
        TripPart tripPart = mDataManager.getItem(child.getTripKey());
        if(tripPart==null){
            long tripKey = -1;
            tripKey = mDataManager.addHotel(group.getPosition()+1,stay,true,true);
            long[] oldKeys = ((EditTopology)mTopology).updateStay(group.getPosition(),tripKey);
            mDataManager.remove(oldKeys,true);
        }else{
            tripPart.setDataKey(Long.toString(stay.getId()));
            notifyItemChanged(getFlatPosition(pos));
        }
    }

}
