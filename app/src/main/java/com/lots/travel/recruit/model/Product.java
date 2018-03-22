package com.lots.travel.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 旅游项目(产品)
 */
public class Product implements Parcelable {
    private int id;
    private String name;

    private long timeGo;
    private String placeSrc;
    private long timeBack;
    private String placeDst;

    private int remainSeats;

    private int expenses;

    private Preferential preferential;

    private Features features;

    private boolean insuranceOpen;
    private List<Insurance> insurances;
    //自动导入签证
    private boolean leadingInVisa;
    //自动导入报名须知
    private boolean leadingInEnter;
    //付费推广
    private boolean promote;

    public Product(){}

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        timeGo = in.readLong();
        placeSrc = in.readString();
        timeBack = in.readLong();
        placeDst = in.readString();
        remainSeats = in.readInt();
        expenses = in.readInt();
        preferential = in.readParcelable(Preferential.class.getClassLoader());
        features = in.readParcelable(Features.class.getClassLoader());
        insuranceOpen = in.readByte() != 0;
        insurances = in.createTypedArrayList(Insurance.CREATOR);
        leadingInVisa = in.readByte() != 0;
        leadingInEnter = in.readByte() != 0;
        promote = in.readByte() != 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getExpenses() {
        return expenses;
    }

    public void setExpenses(int expenses) {
        this.expenses = expenses;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isInsuranceOpen() {
        return insuranceOpen;
    }

    public void setInsuranceOpen(boolean insuranceOpen) {
        this.insuranceOpen = insuranceOpen;
    }

    public List<Insurance> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<Insurance> insurances) {
        this.insurances = insurances;
    }

    public boolean isLeadingInEnter() {
        return leadingInEnter;
    }

    public void setLeadingInEnter(boolean leadingInEnter) {
        this.leadingInEnter = leadingInEnter;
    }

    public boolean isLeadingInVisa() {
        return leadingInVisa;
    }

    public void setLeadingInVisa(boolean leadingInVisa) {
        this.leadingInVisa = leadingInVisa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceDst() {
        return placeDst;
    }

    public void setPlaceDst(String placeDst) {
        this.placeDst = placeDst;
    }

    public String getPlaceSrc() {
        return placeSrc;
    }

    public void setPlaceSrc(String placeSrc) {
        this.placeSrc = placeSrc;
    }

    public Preferential getPreferential() {
        return preferential;
    }

    public void setPreferential(Preferential preferential) {
        this.preferential = preferential;
    }

    public boolean isPromote() {
        return promote;
    }

    public void setPromote(boolean promote) {
        this.promote = promote;
    }

    public int getRemainSeats() {
        return remainSeats;
    }

    public void setRemainSeats(int remainSeats) {
        this.remainSeats = remainSeats;
    }

    public long getTimeBack() {
        return timeBack;
    }

    public void setTimeBack(long timeBack) {
        this.timeBack = timeBack;
    }

    public long getTimeGo() {
        return timeGo;
    }

    public void setTimeGo(long timeGo) {
        this.timeGo = timeGo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(timeGo);
        dest.writeString(placeSrc);
        dest.writeLong(timeBack);
        dest.writeString(placeDst);
        dest.writeInt(remainSeats);
        dest.writeInt(expenses);
        dest.writeParcelable(preferential, flags);
        dest.writeParcelable(features, flags);
        dest.writeByte((byte) (insuranceOpen ? 1 : 0));
        dest.writeTypedList(insurances);
        dest.writeByte((byte) (leadingInVisa ? 1 : 0));
        dest.writeByte((byte) (leadingInEnter ? 1 : 0));
        dest.writeByte((byte) (promote ? 1 : 0));
    }
}
