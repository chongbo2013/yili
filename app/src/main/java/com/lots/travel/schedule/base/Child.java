package com.lots.travel.schedule.base;

import android.os.Parcel;
import android.os.Parcelable;

public class Child implements Parcelable {
    //child类型
    private int mType;
    //所属的组
    private int mGroup;
    //实际数据，即TripPart表的id
    private long mTripKey;
    //额外数据，主要是运行时，控件的一些状态数据
    private ExtraData mExtraData;

    public Child(){}

    public Child(int type,int group){
        this(type,group,-1);
    }

    public Child(int type,int group,long tripKey){
        mType = type;
        mGroup = group;
        mTripKey = tripKey;
    }

    protected Child(Parcel in) {
        mType = in.readInt();
        mGroup = in.readInt();
        mTripKey = in.readLong();
        mExtraData = in.readParcelable(ExtraData.class.getClassLoader());
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getGroup() {
        return mGroup;
    }

    public void setGroup(int group) {
        mGroup = group;
    }

    public long getTripKey() {
        return mTripKey;
    }

    public long setTripKey(long tripKey) {
        long oldKey = mTripKey;
        mTripKey = tripKey;
        return oldKey;
    }

    public void setExtraData(ExtraData extraData){
        mExtraData = extraData;
    }

    public ExtraData getExtraData() {
        return mExtraData;
    }

    public Child copy(){
        Child child = new Child();
        child.mType = mType;
        child.mGroup = mGroup;
        child.mTripKey = mTripKey;
        child.mExtraData = mExtraData;
        return child;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeInt(mGroup);
        dest.writeLong(mTripKey);
        dest.writeParcelable(mExtraData, flags);
    }
}
