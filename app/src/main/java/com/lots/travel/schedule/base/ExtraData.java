package com.lots.travel.schedule.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class ExtraData implements Parcelable {
    public ExtraData(){}

    protected ExtraData(Parcel in) {}

    public static final Creator<ExtraData> CREATOR = new Creator<ExtraData>() {
        @Override
        public ExtraData createFromParcel(Parcel in) {
            return new ExtraData(in);
        }

        @Override
        public ExtraData[] newArray(int size) {
            return new ExtraData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
