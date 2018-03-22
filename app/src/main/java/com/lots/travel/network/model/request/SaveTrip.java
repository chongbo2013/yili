package com.lots.travel.network.model.request;

import com.lots.travel.store.db.TripPart;

import java.util.List;

/**
 * Created by nalanzi on 2017/11/7.
 */

public class SaveTrip {
    private String baseId;
    //0 - 不公开、1 - 公开
    private String status;
    private List<TripPart> dayInfos;

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TripPart> getDayInfos() {
        return dayInfos;
    }

    public void setDayInfos(List<TripPart> dayInfos) {
        this.dayInfos = dayInfos;
    }
}
