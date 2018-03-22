package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/12.
 */

public class GetFollowPeoples extends GetFollows {

    public GetFollowPeoples(int page,int size){
        pageNo = page;
        pageSize = size;
        dataTable = TYPE_USER;
    }

}
