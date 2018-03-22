package com.lots.travel.store.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.model.TripDaysInfo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nalanzi on 2017/11/2.
 */
@Entity
public class Spot implements Parcelable {

    /**
     * pxHot : 0
     * px : 10
     * recommend : 20
     * cityId : 1379
     * viewImg : https://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/1498822086193.jpg
     * countryId : 1677
     * score : 5
     * cityName : 杭州
     * ywTotle : 还有81777个行程去了这里
     * ywTime : 180
     * gpsGoogle : 30.2427011000,120.1502699000
     * viewId : 1367
     * percentPeople : 90%的旅行达人选择该点
     * viewType : 3
     * name : 西湖
     * viewurl : http://www.mafengwo.cn/poi/1093.html
     * shortDesc : 杭州最著名的景点，“西湖十景”令人流连忘返
     * countryName : 中国
     * status : 1
     * spotName : 西湖
     */
    @Id
    private Long viewId;
    private int pxHot;
    private int px;
    private int recommend;
    private int cityId;
    private String viewImg;
    private int countryId;
    private String score;
    private String cityName;
    private String ywTotle;
    private String ywTime;
    private String gpsGoogle;
    private String percentPeople;
    private int viewType;
    private String name;
    private String viewurl;
    private String shortDesc;
    private String countryName;
    private int status;
    private String spotName;
    private String scheduleId;

    public static Spot create(TripDaysInfo.ComponentInfo src){
        Spot spot = new Spot();
        spot.viewId = (long) src.getViewId();
//        spot.pxHot = ;
//        spot.px = ;
//        spot.recommend = ;
//        spot.cityId = ;
        spot.viewImg = src.getViewImg();
//        spot.countryId = ;
        spot.score = src.getScore();
        spot.cityName = src.getCityName();
        spot.ywTotle = src.getYwTotle();
//        spot.ywTime = ;
        spot.gpsGoogle = src.getGpsGoogle();
        spot.percentPeople = src.getPercentPeople();
        spot.viewType = src.getViewType();
//        spot.name;
        spot.viewurl = src.getViewurl();
        spot.shortDesc = src.getShortDesc();
        spot.countryName = src.getCountryName();
        spot.status = src.getStatus();
        spot.spotName = src.getSpotName();
        return spot;
    }
    
    protected Spot(Parcel in) {
        viewId = in.readLong();
        pxHot = in.readInt();
        px = in.readInt();
        recommend = in.readInt();
        cityId = in.readInt();
        viewImg = in.readString();
        countryId = in.readInt();
        score = in.readString();
        cityName = in.readString();
        ywTotle = in.readString();
        ywTime = in.readString();
        gpsGoogle = in.readString();
        percentPeople = in.readString();
        viewType = in.readInt();
        name = in.readString();
        viewurl = in.readString();
        shortDesc = in.readString();
        countryName = in.readString();
        status = in.readInt();
        spotName = in.readString();
        scheduleId = in.readString();
    }

    @Generated(hash = 1115599193)
    public Spot(Long viewId, int pxHot, int px, int recommend, int cityId,
            String viewImg, int countryId, String score, String cityName,
            String ywTotle, String ywTime, String gpsGoogle, String percentPeople,
            int viewType, String name, String viewurl, String shortDesc,
            String countryName, int status, String spotName, String scheduleId) {
        this.viewId = viewId;
        this.pxHot = pxHot;
        this.px = px;
        this.recommend = recommend;
        this.cityId = cityId;
        this.viewImg = viewImg;
        this.countryId = countryId;
        this.score = score;
        this.cityName = cityName;
        this.ywTotle = ywTotle;
        this.ywTime = ywTime;
        this.gpsGoogle = gpsGoogle;
        this.percentPeople = percentPeople;
        this.viewType = viewType;
        this.name = name;
        this.viewurl = viewurl;
        this.shortDesc = shortDesc;
        this.countryName = countryName;
        this.status = status;
        this.spotName = spotName;
        this.scheduleId = scheduleId;
    }

    @Generated(hash = 817251004)
    public Spot() {
    }

    public static final Creator<Spot> CREATOR = new Creator<Spot>() {
        @Override
        public Spot createFromParcel(Parcel in) {
            return new Spot(in);
        }

        @Override
        public Spot[] newArray(int size) {
            return new Spot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(viewId);
        dest.writeInt(pxHot);
        dest.writeInt(px);
        dest.writeInt(recommend);
        dest.writeInt(cityId);
        dest.writeString(viewImg);
        dest.writeInt(countryId);
        dest.writeString(score);
        dest.writeString(cityName);
        dest.writeString(ywTotle);
        dest.writeString(ywTime);
        dest.writeString(gpsGoogle);
        dest.writeString(percentPeople);
        dest.writeInt(viewType);
        dest.writeString(name);
        dest.writeString(viewurl);
        dest.writeString(shortDesc);
        dest.writeString(countryName);
        dest.writeInt(status);
        dest.writeString(spotName);
        dest.writeString(scheduleId);
    }

    public String getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSpotName() {
        return this.spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getViewurl() {
        return this.viewurl;
    }

    public void setViewurl(String viewurl) {
        this.viewurl = viewurl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getPercentPeople() {
        return this.percentPeople;
    }

    public void setPercentPeople(String percentPeople) {
        this.percentPeople = percentPeople;
    }

    public String getGpsGoogle() {
        return this.gpsGoogle;
    }

    public void setGpsGoogle(String gpsGoogle) {
        this.gpsGoogle = gpsGoogle;
    }

    public String getYwTime() {
        return this.ywTime;
    }

    public void setYwTime(String ywTime) {
        this.ywTime = ywTime;
    }

    public String getYwTotle() {
        return this.ywTotle;
    }

    public void setYwTotle(String ywTotle) {
        this.ywTotle = ywTotle;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getScore() {
        return this.score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getCountryId() {
        return this.countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getViewImg() {
        return this.viewImg;
    }

    public void setViewImg(String viewImg) {
        this.viewImg = viewImg;
    }

    public int getCityId() {
        return this.cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getRecommend() {
        return this.recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getPx() {
        return this.px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getPxHot() {
        return this.pxHot;
    }

    public void setPxHot(int pxHot) {
        this.pxHot = pxHot;
    }

    public Long getViewId() {
        return this.viewId;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

    public Spot copy(){
        Spot spot = new Spot();
        spot.viewId = viewId;
        spot.pxHot = pxHot;
        spot.px = px;
        spot.recommend = recommend;
        spot.cityId = cityId;
        spot.viewImg = viewImg;
        spot.countryId = countryId;
        spot.score = score;
        spot.cityName = cityName;
        spot.ywTotle = ywTotle;
        spot.ywTime = ywTime;
        spot.gpsGoogle = gpsGoogle;
        spot.percentPeople = percentPeople;
        spot.viewType = viewType;
        spot.name = name;
        spot.viewurl =viewurl;
        spot.shortDesc = shortDesc;
        spot.countryName = countryName;
        spot.status = status;
        spot.spotName = spotName;
        spot.scheduleId = scheduleId;
        return spot;
    }

}
