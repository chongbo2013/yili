package com.lots.travel.network.model.result;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class MatchedRoute implements Parcelable {
    private String baseId;
    private String macthBaseId;

    public MatchedRoute(){}

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getMacthBaseId() {
        return macthBaseId;
    }

    public void setMacthBaseId(String macthBaseId) {
        this.macthBaseId = macthBaseId;
    }

    protected MatchedRoute(Parcel in) {
        baseId = in.readString();
        macthBaseId = in.readString();
    }

    public static final Creator<MatchedRoute> CREATOR = new Creator<MatchedRoute>() {
        @Override
        public MatchedRoute createFromParcel(Parcel in) {
            return new MatchedRoute(in);
        }

        @Override
        public MatchedRoute[] newArray(int size) {
            return new MatchedRoute[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(baseId);
        dest.writeString(macthBaseId);
    }
}
