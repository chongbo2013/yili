package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2017/11/2.
 */

public class AddTrip {
    /**
     {
     "id":"",//id为空表示新增；不为空表示更新
     "style":"trip|note",// trip表示行程；note表示游记
     "cityFrom":"杭州|351",//出发城市（可以是中文，可以是平台里城市景点id）
     "cityTo":"351",//目的地城市（城市id）
     "days":"",//游玩天数
     "travelTag":"",//旅行标签（中文）
     "travelNeed":"",//旅行需求 （中文）
     "dateFrom":"",//出发时间
     "dateTo":"",//结束时间
     "title":"",//标题
     "img":"",//封面
     "zhaomuStatus":"1",//当style=trip时有效。1有招募；0没有招募
     "xuanshangStatus":"",//当style=trip时有效。1有悬赏；0没有悬赏
     "status":"",//0草稿；1发布状态；-1删除状态
     "myViewSpot":"",//自选景点id（过个用‘,’隔开）
     "xcshow":""//当style=note时有效，1表示开放；0表示仅自己可见
     }
     */

    private String id;
    private String style;
    private String cityFrom;
    private String cityTo;
    private String days;
    private String travelTag;
    private String travelNeed;
    private String dateFrom;
    private String dateTo;
    private String title;
    private String img;
    private String zhaomuStatus;
    private String xuanshangStatus;
    private String status;
    private String myViewSpot;
    private String xcshow;

    public AddTrip(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTravelTag() {
        return travelTag;
    }

    public void setTravelTag(String travelTag) {
        this.travelTag = travelTag;
    }

    public String getTravelNeed() {
        return travelNeed;
    }

    public void setTravelNeed(String travelNeed) {
        this.travelNeed = travelNeed;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getZhaomuStatus() {
        return zhaomuStatus;
    }

    public void setZhaomuStatus(String zhaomuStatus) {
        this.zhaomuStatus = zhaomuStatus;
    }

    public String getXuanshangStatus() {
        return xuanshangStatus;
    }

    public void setXuanshangStatus(String xuanshangStatus) {
        this.xuanshangStatus = xuanshangStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMyViewSpot() {
        return myViewSpot;
    }

    public void setMyViewSpot(String myViewSpot) {
        this.myViewSpot = myViewSpot;
    }

    public String getXcshow() {
        return xcshow;
    }

    public void setXcshow(String xcshow) {
        this.xcshow = xcshow;
    }
}
