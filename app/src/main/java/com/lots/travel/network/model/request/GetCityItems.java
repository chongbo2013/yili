package com.lots.travel.network.model.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2017/11/2.
 */

public class GetCityItems implements Pageable,Parcelable {
    private long cityId;
    private int pageNo;
    private int pageSize;
    private String keyword;

    public GetCityItems(){}

    protected GetCityItems(Parcel in) {
        cityId = in.readLong();
        pageNo = in.readInt();
        pageSize = in.readInt();
        keyword = in.readString();
    }

    public static final Creator<GetCityItems> CREATOR = new Creator<GetCityItems>() {
        @Override
        public GetCityItems createFromParcel(Parcel in) {
            return new GetCityItems(in);
        }

        @Override
        public GetCityItems[] newArray(int size) {
            return new GetCityItems[size];
        }
    };

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(cityId);
        dest.writeInt(pageNo);
        dest.writeInt(pageSize);
        dest.writeString(keyword);
    }
}
