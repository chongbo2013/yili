package com.lots.travel.footprint.model;

/**
 * note - 游记
 * trip - 行程
 * zhibo - 直播
 * youji - 足迹
 * share - 分享
 * ！！！！！share是哪个、图里面的活动是哪个、地址是哪个？？？
 *
 * 参与活动  activity_join
 * 参与招募  trip_join
 * 活动  activity
 * 去过想去 - eventStyle=spot_collection、json-type：1-去过，2-想去
 */
public enum  EventStyle {
    TRIP(Footprint.TRIP,0),
    ACTIVE(Footprint.ACTIVE,1),
    NOTE(Footprint.NOTE,2),
    FOOTPRINT(Footprint.FOOTPRINT,3),
    LIVE(Footprint.LIVE,4),
    DESTINATION(Footprint.DESTINATION,5),
    ACTIVE_JOIN(Footprint.ACTIVE_JOIN,6);

    private String name;
    private int value;

    EventStyle(String name,int value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
