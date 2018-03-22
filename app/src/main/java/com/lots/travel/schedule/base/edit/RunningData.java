package com.lots.travel.schedule.base.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.base.ExtraData;

/**
 * Created by nalanzi on 2017/12/25.
 */

public class RunningData extends ExtraData implements Parcelable {
    private boolean detailsExpand;

    private int currentTab;

    public RunningData(){
        detailsExpand = false;
        currentTab = 1;
    }

    protected RunningData(Parcel in) {
        detailsExpand = in.readByte() != 0;
        currentTab = in.readInt();
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

    public boolean isDetailsExpand() {
        return detailsExpand;
    }

    public void setDetailsExpand(boolean detailsExpand) {
        this.detailsExpand = detailsExpand;
    }

    public boolean toggle(){
        detailsExpand = !detailsExpand;
        return detailsExpand;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (detailsExpand ? 1 : 0));
        dest.writeInt(currentTab);
    }
}
