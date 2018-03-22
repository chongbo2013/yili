package com.lots.travel.place.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2017/9/3.
 */
//搜索目的地时使用的条件：落地签、免签、海岛、摄影、文化、购物、周边
//不使用枚举，可能从服务器获取，枚举不适用
public class Condition implements Parcelable{
    //落地签
    public static final Condition VISA_ON_ARRIVAL = new Condition("落地签");

    //免签
    public static final Condition VISA_EXEMPTION = new Condition("免签");

    //海岛
    public static final Condition ISLAND = new Condition("海岛");

    //摄影
    public static final Condition PHOTOGRAPH = new Condition("摄影");

    //文化
    public static final Condition CULTURE = new Condition("文化");

    //购物
    public static final Condition SHOPPING = new Condition("购物");

    //周边
    public static final Condition SURROUNDING = new Condition("周边");

    public static Condition[] getConditions(){
        return new Condition[]{VISA_ON_ARRIVAL,VISA_EXEMPTION,ISLAND,PHOTOGRAPH,CULTURE,SHOPPING,SURROUNDING};
    }

    private String name;

    private Condition(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected Condition(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Condition> CREATOR = new Creator<Condition>() {
        @Override
        public Condition createFromParcel(Parcel in) {
            return new Condition(in);
        }

        @Override
        public Condition[] newArray(int size) {
            return new Condition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
