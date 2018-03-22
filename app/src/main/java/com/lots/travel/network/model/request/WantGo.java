package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/12.
 */

public class WantGo {
    //景点
    public static final String TYPE_SPOT = "spot";
    //景点
    public static final String TYPE_TRIP = "trip";
    //景点
    public static final String TYPE_NOTE = "note";
    //景点
    public static final String TYPE_FOOTPRINT = "youji";
    //景点
    public static final String TYPE_LIVE = "zhibo";

    /**
     * dataTable : spot 景点，trip 行程；note 游记；youji 足迹；zhibo直播 ...
     * dataKey : xxx
     * type :
     * dateTime :
     * imgs :
     * video :
     * videoImg :
     * sound :
     * soundLen :
     * descript :
     */

    private String dataTable;
    private String dataKey;
    //0 - 想去、1 - 去过
    private String type;
    private String dateTime;
    private String imgs;
    private String video;
    private String videoImg;
    private String sound;
    private String soundLen;
    private String descript;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSoundLen() {
        return soundLen;
    }

    public void setSoundLen(String soundLen) {
        this.soundLen = soundLen;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
