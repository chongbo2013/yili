package com.lots.travel.schedule.base.preview;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.base.ExtraData;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RunningData extends ExtraData implements Parcelable {
    private boolean expanded;

    public RunningData(){}

    protected RunningData(Parcel in) {
        expanded = in.readByte() != 0;
    }

    public static final Creator<RunningData> CREATOR = new Creator<RunningData>() {
        @Override
        public RunningData createFromParcel(Parcel in) {
            return new RunningData(in);
        }

        @Override
        public RunningData[] newArray(int size) {
            return new RunningData[size];
        }
    };

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void toggle(){
        expanded = !expanded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (expanded ? 1 : 0));
    }
}
