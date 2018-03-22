package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2017/12/17.
 */

public class AddFootprint {
    //1 - 图片+文字；2 - 视频+文字
    private String type;
    //文本内容
    private String content;
    //图片
    private String pics;
    //视频地址
    private String video;
    //封面地址
    private String videoimg;
    //gps信息
    private GpsData gpsData;
    //0 - 不对外公开；1 - 对外公开
    private String xzshow;

    public AddFootprint(){
        gpsData = new GpsData();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoimg() {
        return videoimg;
    }

    public void setVideoimg(String videoimg) {
        this.videoimg = videoimg;
    }

    public void setLocation(double latitude,double longitude){
        gpsData.GPS = latitude+","+longitude;
    }

    public void setGpsAddress(String address){
        gpsData.GPS_Address = address;
    }

    public GpsData getGpsData() {
        return gpsData;
    }

    public void setGpsData(GpsData gpsData) {
        this.gpsData = gpsData;
    }

    public String getXzshow() {
        return xzshow;
    }

    public void setXzshow(String xzshow) {
        this.xzshow = xzshow;
    }

    public static class GpsData{
        private String GPS;
        private String GPS_Address;
    }

}
