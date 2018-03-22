package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/11.
 */
//common_appservice_getFollowPageList的参数
public class GetFollows implements Pageable{
    //用户
    public static final String TYPE_USER = "user";
    //众筹
    public static final String TYPE_CROW_FUNDING = "product";
    //足迹
    public static final String TYPE_FOOTPRINT = "youji";
    //目的地
    public static final String TYPE_DESTINATION = "spot";
    //行程
    public static final String TYPE_TRIP = "trip";
    //游记
    public static final String TYPE_NOTE = "note";
    //直播
    public static final String TYPE_LIVE = "zhibo";
    //活动
    public static final String TYPE_ACTIVITY = "activity";
    //房屋
    public static final String TYPE_HOUSE = "house";
    //院子
    public static final String TYPE_COURTYARD = "courtyard";
    //屋主
    public static final String TYPE_HOUSE_OWNER = "houseOwner";

    protected int pageNo;
    protected int pageSize;
    protected String dataTable;

    @Override
    public int getPageNo() {
        return pageNo;
    }

    @Override
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }
}
