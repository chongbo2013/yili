package com.lots.travel.network.model.request;

/**
 * Created by lWX479187 on 2017/9/22.
 */
public interface Pageable {
    int getPageNo();
    void setPageNo(int pageNo);

    int getPageSize();
    void setPageSize(int size);
}
