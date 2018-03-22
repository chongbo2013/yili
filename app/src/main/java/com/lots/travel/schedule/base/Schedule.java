package com.lots.travel.schedule.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2017/12/23.
 */

public class Schedule implements Parcelable {
    private String id;
    //时间
    private int count;
    private long startTime;
    //地址
    private String cityFrom,cityFromName;
    private String cityTo,cityToName;
    private String countryFrom,countryFromName;
    private String countryTo,countryToName;
    //图片
    private String cityToImg;
    private String countryToImg;

    //标签、需求 - 对应界面上的主题、需求
    private String travelTags;
    private String travelNeeds;

    public Schedule(){}

    protected Schedule(Parcel in) {
        id = in.readString();
        count = in.readInt();
        startTime = in.readLong();
        cityFrom = in.readString();
        cityFromName = in.readString();
        cityTo = in.readString();
        cityToName = in.readString();
        countryFrom = in.readString();
        countryFromName = in.readString();
        countryTo = in.readString();
        countryToName = in.readString();
        cityToImg = in.readString();
        countryToImg = in.readString();
        travelTags = in.readString();
        travelNeeds = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom,String cityFromName) {
        this.cityFrom = cityFrom;
        this.cityFromName = cityFromName;
    }

    public String getCityFromName() {
        return cityFromName;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo,String cityToName) {
        this.cityTo = cityTo;
        this.cityToName = cityToName;
    }

    public String getCityToName() {
        return cityToName;
    }

    public String getCountryFrom() {
        return countryFrom;
    }

    public void setCountryFrom(String countryFrom,String countryFromName) {
        this.countryFrom = countryFrom;
        this.countryFromName = countryFromName;
    }

    public String getCountryFromName() {
        return countryFromName;
    }

    public String getCountryTo() {
        return countryTo;
    }

    public void setCountryTo(String countryTo,String countryToName) {
        this.countryTo = countryTo;
        this.countryToName = countryToName;
    }

    public String getCountryToName() {
        return countryToName;
    }

    public String getTravelTags() {
        return travelTags;
    }

    public void setTravelTags(String travelTags) {
        this.travelTags = travelTags;
    }

    public String getTravelNeeds() {
        return travelNeeds;
    }

    public void setTravelNeeds(String travelNeeds) {
        this.travelNeeds = travelNeeds;
    }

    public String getCityToImg() {
        return cityToImg;
    }

    public void setCityToImg(String cityToImg) {
        this.cityToImg = cityToImg;
    }

    public String getCountryToImg() {
        return countryToImg;
    }

    public void setCountryToImg(String countryToImg) {
        this.countryToImg = countryToImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeInt(count);
        dest.writeLong(startTime);
        dest.writeString(cityFrom);
        dest.writeString(cityFromName);
        dest.writeString(cityTo);
        dest.writeString(cityToName);
        dest.writeString(countryFrom);
        dest.writeString(countryFromName);
        dest.writeString(countryTo);
        dest.writeString(countryToName);
        dest.writeString(cityToImg);
        dest.writeString(countryToImg);
        dest.writeString(travelTags);
        dest.writeString(travelNeeds);
    }

}
