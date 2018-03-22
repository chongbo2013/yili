package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/23.
 */

public class FollowElement {
    //关注人
    public static final String TYPE_USER = "user";
    //收藏众筹
    public static final String TYPE_PRODUCT = "product";
    //收藏用户足迹
    public static final String TYPE_FOOTPRINT = "youji";
    //收藏目的地
    public static final String TYPE_DESTINATION = "spot";
    //行程
    public static final String TYPE_TRIP = "trip";
    //游记
    public static final String TYPE_NOTE = "note";
    //直播
    public static final String TYPE_LIVE = "zhibo";
    //活动
    public static final String TYPE_ACTIVE = "activity";

    //关注
    public static final String ACTION_FOLLOW = "follow";
    //取消关注
    public static final String ACTION_UNFOLLOW = "unFollow";

    private String dataTable;
    private String dataKey;
    private String style;

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
