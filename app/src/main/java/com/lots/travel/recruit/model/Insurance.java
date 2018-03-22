package com.lots.travel.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lWX479187 on 2017/10/30.
 */
public class Insurance implements Parcelable {
    private int id;
    //名称
    private String name;
    //花费
    private int price;
    //适合人群
    private String target;
    //保障时间
    private long activeTimeStart;
    private long activeTimeEnd;

    private boolean checked;

    public Insurance(){}

    protected Insurance(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readInt();
        target = in.readString();
        activeTimeStart = in.readLong();
        activeTimeEnd = in.readLong();
        checked = in.readByte() != 0;
    }

    public static final Creator<Insurance> CREATOR = new Creator<Insurance>() {
        @Override
        public Insurance createFromParcel(Parcel in) {
            return new Insurance(in);
        }

        @Override
        public Insurance[] newArray(int size) {
            return new Insurance[size];
        }
    };

    public long getActiveTimeEnd() {
        return activeTimeEnd;
    }

    public void setActiveTimeEnd(long activeTimeEnd) {
        this.activeTimeEnd = activeTimeEnd;
    }

    public long getActiveTimeStart() {
        return activeTimeStart;
    }

    public void setActiveTimeStart(long activeTimeStart) {
        this.activeTimeStart = activeTimeStart;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(target);
        dest.writeLong(activeTimeStart);
        dest.writeLong(activeTimeEnd);
        dest.writeByte((byte) (checked ? 1 : 0));
    }
}
