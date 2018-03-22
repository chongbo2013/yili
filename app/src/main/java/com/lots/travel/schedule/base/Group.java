package com.lots.travel.schedule.base;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {
    //哪一组
    private int mPosition;
    //child个数
    private int mChildSize;
    //展开时的位置
    private int mFlatPosition;

    public Group(){}

    public Group(int position,int childSize,int flatPosition){
        mPosition = position;
        mChildSize = childSize;
        mFlatPosition = flatPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setChildSize(int childSize){
        mChildSize = childSize;
    }

    public int getChildSize(){
        return mChildSize;
    }

    public int incrementChildSize(){
        return mChildSize++;
    }

    public int decrementChildSize(){
        return mChildSize--;
    }

    public int getFlatPosition() {
        return mFlatPosition;
    }

    public void setFlatPosition(int flatPosition) {
        mFlatPosition = flatPosition;
    }

    public int incrementFlatPosition(){
        return mFlatPosition++;
    }

    public int decrementFlatPosition(){
        return mFlatPosition--;
    }

    protected Group(Parcel in) {
        mPosition = in.readInt();
        mChildSize = in.readInt();
        mFlatPosition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPosition);
        dest.writeInt(mChildSize);
        dest.writeInt(mFlatPosition);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

}
