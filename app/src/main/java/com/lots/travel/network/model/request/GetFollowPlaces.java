package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/12.
 */

public class GetFollowPlaces extends GetFollows {
    //省份
    public static final String VIEW_TYPE_PROVINCE = "0";
    //国家
    public static final String VIEW_TYPE_COUNTRY = "1";
    //城市
    public static final String VIEW_TYPE_CITY = "2";
    //景点
    public static final String VIEW_TYPE_SPOT = "3";
    //子景点
    public static final String VIEW_TYPE_SPOT_SECONDARY = "4";
    //区县
    public static final String VIEW_TYPE_DISTRICT = "6";

    private String viewType;

    public GetFollowPlaces(int page,int size,String type){
        pageNo = page;
        pageSize = size;
        dataTable = TYPE_DESTINATION;
        viewType = type;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
}
