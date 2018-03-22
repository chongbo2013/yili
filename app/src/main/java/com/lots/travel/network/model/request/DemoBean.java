package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2017/9/16.
 */

public class DemoBean {
    private int targetId;
    private int pageNo;
    private int pageSize;

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
