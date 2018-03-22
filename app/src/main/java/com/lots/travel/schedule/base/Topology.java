package com.lots.travel.schedule.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 可能需要在变化拓扑的过程中，进行界面更新，持有Adapter更方便
 * 由于type就只有这么几种，所以直接在此处定义，可以在不同topology中定义不同的type。
 */
public class Topology implements Parcelable{
    //第几日
    public static final int TYPE_DATE = 1;
    //当日描述
    public static final int TYPE_DESC = 2;
    //景点
    public static final int TYPE_SPOT = 3;
    //酒店
    public static final int TYPE_HOTEL = 4;
    //餐厅
    public static final int TYPE_FOOD = 5;
    //入住酒店
    public static final int TYPE_STAY = 6;
    //交通方式
    public static final int TYPE_TRAFFIC = 7;
    //添加components按钮
    public static final int TYPE_ADD = 8;
    //move时需要
    public static final int TYPE_DAY = 9;

    protected List<Group> mGroups;
    protected List<Child> mChildren;
    private ScheduleAdapter mAdapter;

    public Topology(){
        mGroups = new ArrayList<>();
        mChildren = new ArrayList<>();
    }

    public static final Creator<Topology> CREATOR = new Creator<Topology>() {
        @Override
        public Topology createFromParcel(Parcel in) {
            return new Topology(in);
        }

        @Override
        public Topology[] newArray(int size) {
            return new Topology[size];
        }
    };

    public void setAdapter(ScheduleAdapter adapter){
        mAdapter = adapter;
    }

    public Group getGroupAt(int gPos){
        return mGroups.get(gPos);
    }

    public int getGroupCount(){
        return mGroups.size();
    }

    public Child getChildAt(int pos){
        return mChildren.get(pos);
    }

    public int getChildCount(){
        return mChildren.size();
    }

   //childPos是add position，通过add position不一定能够确定其group
   public void addChild(int insertPosition, Group group, int childType, long tripKey){
       Child child = new Child(childType,group.getPosition(),tripKey);

       mChildren.add(insertPosition,child);
       group.incrementChildSize();

       for (int i=group.getPosition()+1;i<mGroups.size();i++){
           Group temp = mGroups.get(i);
           temp.incrementFlatPosition();
       }

       notifyChildInserted(insertPosition);
   }

   public long removeChild(int childPosition){
       Child child = mChildren.get(childPosition);
       Group group = mGroups.get(child.getGroup());

       mChildren.remove(childPosition);
       group.decrementChildSize();

       for (int i=group.getPosition()+1;i<mGroups.size();i++){
           Group temp = mGroups.get(i);
           temp.decrementFlatPosition();
       }

       notifyChildRemoved(childPosition);
       return child.getTripKey();
   }

   public long setChildContent(int childPosition,long tripKey){
        Child child = mChildren.get(childPosition);
        long oldTripKey = child.getTripKey();
        child.setTripKey(tripKey);
        notifyChildChanged(childPosition);
        return oldTripKey;
   }

   public long getChildContent(int childPosition){
       Child child = null;
       if(childPosition<mChildren.size())
           child = mChildren.get(childPosition);
       return child==null ? -1:child.getTripKey();
   }

   public void notifyChildrenChanged(){
       if(mAdapter!=null)
           mAdapter.notifyDataSetChanged();
   }

   public void notifyChildChanged(int itemPosition){
       if(mAdapter!=null) {
           mAdapter.notifyItemChanged(mAdapter.getFlatPosition(itemPosition));
       }
   }

   public void notifyChildInserted(int itemPosition){
       if(mAdapter!=null) {
           mAdapter.notifyItemInserted(mAdapter.getFlatPosition(itemPosition));
       }
   }

   public void notifyChildRemoved(int itemPosition){
       if(mAdapter!=null) {
           mAdapter.notifyItemRemoved(mAdapter.getFlatPosition(itemPosition));
       }
   }

   public void notifyChildMoved(int from,int to){
       if(mAdapter!=null) {
           int from1 = mAdapter.getFlatPosition(from);
           int to1 = mAdapter.getFlatPosition(to);
           mAdapter.notifyItemMoved(from1,to1);
       }
   }

   protected Topology(Parcel in) {
        mGroups = in.createTypedArrayList(Group.CREATOR);
        mChildren = in.createTypedArrayList(Child.CREATOR);
   }

   @Override
   public int describeContents() {
        return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mGroups);
        dest.writeTypedList(mChildren);
   }

}
