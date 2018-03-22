package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/11.
 */
//common_appservice_getFollowPageList的参数
public class GetFollowNotes extends GetFollows{

    public GetFollowNotes(int pageNo,int pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.dataTable = TYPE_NOTE;
    }

}
