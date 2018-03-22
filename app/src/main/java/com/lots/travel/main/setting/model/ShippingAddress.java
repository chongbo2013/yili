package com.lots.travel.main.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2018/1/10.
 */

public class ShippingAddress implements Parcelable {
    private String name;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String remark;
    private boolean asDefault;

    public ShippingAddress(){}

    protected ShippingAddress(Parcel in) {
        name = in.readString();
        phone = in.readString();
        province = in.readString();
        city = in.readString();
        district = in.readString();
        detailAddress = in.readString();
        remark = in.readString();
        asDefault = in.readByte() != 0;
    }

    public static final Creator<ShippingAddress> CREATOR = new Creator<ShippingAddress>() {
        @Override
        public ShippingAddress createFromParcel(Parcel in) {
            return new ShippingAddress(in);
        }

        @Override
        public ShippingAddress[] newArray(int size) {
            return new ShippingAddress[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isAsDefault() {
        return asDefault;
    }

    public void setAsDefault(boolean asDefault) {
        this.asDefault = asDefault;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(detailAddress);
        dest.writeString(remark);
        dest.writeByte((byte) (asDefault ? 1 : 0));
    }
}
