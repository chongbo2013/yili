package com.lots.travel.footprint.model;

/**
 * Created by nalanzi on 2018/1/26.
 */

public class DestinationData {
    private String type;
    private String score;
    private String percentPeople;
    private String shortDesc;
    private String gpsGoogle;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPercentPeople() {
        return percentPeople;
    }

    public void setPercentPeople(String percentPeople) {
        this.percentPeople = percentPeople;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getGpsGoogle() {
        return gpsGoogle;
    }

    public void setGpsGoogle(String gpsGoogle) {
        this.gpsGoogle = gpsGoogle;
    }
}
