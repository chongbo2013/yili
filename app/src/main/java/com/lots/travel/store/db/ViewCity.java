package com.lots.travel.store.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nalanzi on 2017/11/1.
 */
@Entity
public class ViewCity implements Parcelable {
    @Id
    private Long id;
    private int pp;
    private String country;
    private String cName;
    private int viewType;
    private String name;
    private String tag;
    private String viewImg;
    private String visaType;

    protected ViewCity(Parcel in) {
        id = in.readLong();
        pp = in.readInt();
        country = in.readString();
        cName = in.readString();
        viewType = in.readInt();
        name = in.readString();
        tag = in.readString();
        viewImg = in.readString();
        visaType = in.readString();
    }

    @Generated(hash = 1937973852)
    public ViewCity(Long id, int pp, String country, String cName, int viewType,
            String name, String tag, String viewImg, String visaType) {
        this.id = id;
        this.pp = pp;
        this.country = country;
        this.cName = cName;
        this.viewType = viewType;
        this.name = name;
        this.tag = tag;
        this.viewImg = viewImg;
        this.visaType = visaType;
    }

    @Generated(hash = 1064031591)
    public ViewCity() {
    }

    public static final Creator<ViewCity> CREATOR = new Creator<ViewCity>() {
        @Override
        public ViewCity createFromParcel(Parcel in) {
            return new ViewCity(in);
        }

        @Override
        public ViewCity[] newArray(int size) {
            return new ViewCity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(pp);
        dest.writeString(country);
        dest.writeString(cName);
        dest.writeInt(viewType);
        dest.writeString(name);
        dest.writeString(tag);
        dest.writeString(viewImg);
        dest.writeString(visaType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getViewImg() {
        return viewImg;
    }

    public void setViewImg(String viewImg) {
        this.viewImg = viewImg;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public String getCName() {
        return this.cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }
}
