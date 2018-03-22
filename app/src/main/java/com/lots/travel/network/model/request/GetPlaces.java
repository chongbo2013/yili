package com.lots.travel.network.model.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2017/12/18.
 */

public class GetPlaces implements Pageable {
    private int pageNo;
    private int pageSize;
    private String keyword;
    //景点级别
    private String viewType;
    //城市分类
    private String tagType;

    public GetPlaces(){}

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
