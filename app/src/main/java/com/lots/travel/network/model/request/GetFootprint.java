package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2017/11/18.
 */

public class GetFootprint implements Pageable {
    private int pageNo;
    private int pageSize;
    private String queryType;//private - 个人、public - 全球
    private long targetId;
    private String eventStyle;//note - 游记、trip - 行程招募、zhibo - 直播、youji - 足迹、share - 分享
    private String zhaomuStatus;//1 - 是、0 - 否
    private String xuanshangStatus;

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

    public String getZhaomuStatus() {
        return zhaomuStatus;
    }

    public void setZhaomuStatus(String zhaomuStatus) {
        this.zhaomuStatus = zhaomuStatus;
    }

    public String getXuanshangStatus() {
        return xuanshangStatus;
    }

    public void setXuanshangStatus(String xuanshangStatus) {
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
    public void setPageSize(int size) {
        this.pageSize = size;
    }
}
