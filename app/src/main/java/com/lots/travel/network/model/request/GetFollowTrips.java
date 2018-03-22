package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/11.
 */
public class GetFollowTrips extends GetFollows{

    public GetFollowTrips(int pageNo,int pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.dataTable = TYPE_TRIP;
    }

}
