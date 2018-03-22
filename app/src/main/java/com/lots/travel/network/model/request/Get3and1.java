package com.lots.travel.network.model.request;

/**
 * Created by Lenovo on 2017/11/13.
 */

public class Get3and1 implements Pageable {
    private String queryType;

    private long targetId;

    private String eventStyle;

    private int zhaomuStatus;

    private int xuanshangStatus;

    private int pageNo;

    private int pageSize;

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }

    public int getZhaomuStatus() {
        return zhaomuStatus;
    }

    public void setZhaomuStatus(int zhaomuStatus) {
        this.zhaomuStatus = zhaomuStatus;
    }

    public int getXuanshangStatus() {
        return xuanshangStatus;
    }

    public void setXuanshangStatus(int xuanshangStatus) {
        this.xuanshangStatus = xuanshangStatus;
    }

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
}
