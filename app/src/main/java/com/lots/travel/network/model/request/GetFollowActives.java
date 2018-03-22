package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/11.
 */
public class GetFollowActives extends GetFollows{

    public GetFollowActives(int pageNo, int pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.dataTable = TYPE_ACTIVITY;
    }

}
