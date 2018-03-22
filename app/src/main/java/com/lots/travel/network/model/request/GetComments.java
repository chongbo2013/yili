package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/5.
 */

public class GetComments implements Pageable {
    /**
     * 评论类型：1 - 评价、2 - 评论
     */
    //评价
    public static final String COMMENT_EVENT_PL = "1";
    //评论
    public static final String COMMENT_EVENT_PJ = "2";

    //1 - 评价；2 - 评论
    private String eventType;
    //事件对象分类：spot 景点，trip 行程；node 游记；youji 足迹；zhibo直播
    private String dataTable;
    //事件对象主键
    private String dataKey;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    @Override
    public int getPageNo() {
        return 0;
    }

    @Override
    public void setPageNo(int pageNo) {

    }

    @Override
    public int getPageSize() {
        return 0;
    }

    @Override
    public void setPageSize(int size) {

    }
}
