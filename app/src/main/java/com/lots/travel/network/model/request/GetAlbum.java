package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2017/11/24.
 */

public class GetAlbum implements Pageable {
    private long targetId;
    private int pageNo;
    private int pageSize;

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
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
