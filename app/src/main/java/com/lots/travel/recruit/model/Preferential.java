package com.lots.travel.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lWX479187 on 2017/10/30.
 */
public class Preferential implements Parcelable {
    protected Preferential(Parcel in) {
    }

    public static final Creator<Preferential> CREATOR = new Creator<Preferential>() {
        @Override
        public Preferential createFromParcel(Parcel in) {
            return new Preferential(in);
        }

        @Override
        public Preferential[] newArray(int size) {
            return new Preferential[size];
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
