package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/5.
 */

public class AddZan {
    /**
     * 事件对象分类：spot 景点，trip 行程；node 游记；youji 足迹；zhibo直播
     * 在点赞、评论时使用
     */

    public static final String DATA_TYPE_SPOT = "spot";

    public static final String DATA_TYPE_TRIP = "trip";

    public static final String DATA_TYPE_NOTE = "note";

    public static final String DATA_TYPE_FOOTPRINT = "youji";

    public static final String DATA_TYPE_LIVE = "zhibo";

    /**
     * 点赞、评论操作类型
     */
    public static final String ZAN_COMMENT_ADD = "add";
    public static final String ZAN_COMMENT_DELETE = "delete";

    private String dataTable;
    private String dataKey;
    private String style;

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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
