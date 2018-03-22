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
public class Hotel implements Parcelable {
    /**
     * img : http://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/hotel/local/1498814422564_1498814422564.png
     * ywTotle : 还有个行程去了这里
     * viewSpotCountry : 1677
     * distance : 距离市中心3km
     * scoreType :
     * gpsGaode :
     * scoreTotle :
     * gps : 北纬N30°15′39.06″ 东经E120°08′29.65″
     * score : 5
     * gpsBaidu :
     * viewSpotCity : 1379
     * gpsGoogle : 30.2585390000,120.1462840000
     * cityName : 杭州
     * zone : 西湖
     * avgCostUnit : ￥
     * cName : 中国
     * percentPeople :
     * name : 杭州静逸别墅酒店
     * viewurl : http://hotels.ctrip.com/hotel/458801.html
     * shortDesc :
     * id : 1860
     * tag :
     * avgCost :
     * scoreDesc :
     */
    @Id
    private Long id;
    private String img;
    private String ywTotle;
    private int viewSpotCountry;
    private String distance;
    private String scoreType;
    private String gpsGaode;
    private String scoreTotle;
    private String gps;
    private String score;
    private String gpsBaidu;
    private int viewSpotCity;
    private String gpsGoogle;
    private String cityName;
    private String zone;
    private String avgCostUnit;
    private String cName;
    private String percentPeople;
    private String name;
    private String viewurl;
    private String shortDesc;
    private String tag;
    private String avgCost;
    private String scoreDesc;
    private String scheduleId;

    public static Hotel create(TripDaysInfo.ComponentInfo src){
        Hotel hotel = new Hotel();
        hotel.id = (long) src.getId();
        hotel.img = src.getImg();
        hotel.ywTotle = src.getYwTotle();
//        hotel.distance = ;
        hotel.scoreType = src.getScoreType();
        hotel.scoreTotle = src.getScoreTotle();
        hotel.score = src.getScore();
        hotel.gpsGoogle = src.getGpsGoogle();
        hotel.percentPeople = src.getPercentPeople();
        hotel.name = src.getName();
        hotel.viewurl = src.getViewurl();
        hotel.shortDesc = src.getShortDesc();
        hotel.tag = src.getTag();
        hotel.scoreDesc = src.getScoreDesc();
        return hotel;
    }

    protected Hotel(Parcel in) {
        id = in.readLong();
        img = in.readString();
        ywTotle = in.readString();
        viewSpotCountry = in.readInt();
        distance = in.readString();
        scoreType = in.readString();
        gpsGaode = in.readString();
        scoreTotle = in.readString();
        gps = in.readString();
        score = in.readString();
        gpsBaidu = in.readString();
        viewSpotCity = in.readInt();
        gpsGoogle = in.readString();
        cityName = in.readString();
        zone = in.readString();
        avgCostUnit = in.readString();
        cName = in.readString();
        percentPeople = in.readString();
        name = in.readString();
        viewurl = in.readString();
        shortDesc = in.readString();
        tag = in.readString();
        avgCost = in.readString();
        scoreDesc = in.readString();
        scheduleId = in.readString();
    }

    @Generated(hash = 1288540806)
    public Hotel(Long id, String img, String ywTotle, int viewSpotCountry, String distance,
            String scoreType, String gpsGaode, String scoreTotle, String gps, String score,
            String gpsBaidu, int viewSpotCity, String gpsGoogle, String cityName, String zone,
            String avgCostUnit, String cName, String percentPeople, String name, String viewurl,
            String shortDesc, String tag, String avgCost, String scoreDesc, String scheduleId) {
        this.id = id;
        this.img = img;
        this.ywTotle = ywTotle;
        this.viewSpotCountry = viewSpotCountry;
        this.distance = distance;
        this.scoreType = scoreType;
        this.gpsGaode = gpsGaode;
        this.scoreTotle = scoreTotle;
        this.gps = gps;
        this.score = score;
        this.gpsBaidu = gpsBaidu;
        this.viewSpotCity = viewSpotCity;
        this.gpsGoogle = gpsGoogle;
        this.cityName = cityName;
        this.zone = zone;
        this.avgCostUnit = avgCostUnit;
        this.cName = cName;
        this.percentPeople = percentPeople;
        this.name = name;
        this.viewurl = viewurl;
        this.shortDesc = shortDesc;
        this.tag = tag;
        this.avgCost = avgCost;
        this.scoreDesc = scoreDesc;
        this.scheduleId = scheduleId;
    }

    @Generated(hash = 890374398)
    public Hotel() {
    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(img);
        dest.writeString(ywTotle);
        dest.writeInt(viewSpotCountry);
        dest.writeString(distance);
        dest.writeString(scoreType);
        dest.writeString(gpsGaode);
        dest.writeString(scoreTotle);
        dest.writeString(gps);
        dest.writeString(score);
        dest.writeString(gpsBaidu);
        dest.writeInt(viewSpotCity);
        dest.writeString(gpsGoogle);
        dest.writeString(cityName);
        dest.writeString(zone);
        dest.writeString(avgCostUnit);
        dest.writeString(cName);
        dest.writeString(percentPeople);
        dest.writeString(name);
        dest.writeString(viewurl);
        dest.writeString(shortDesc);
        dest.writeString(tag);
        dest.writeString(avgCost);
        dest.writeString(scoreDesc);
        dest.writeString(scheduleId);
    }

    public String getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScoreDesc() {
        return this.scoreDesc;
    }

    public void setScoreDesc(String scoreDesc) {
        this.scoreDesc = scoreDesc;
    }

    public String getAvgCost() {
        return this.avgCost;
    }

    public void setAvgCost(String avgCost) {
        this.avgCost = avgCost;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getPercentPeople() {
        return this.percentPeople;
    }

    public void setPercentPeople(String percentPeople) {
        this.percentPeople = percentPeople;
    }

    public String getCName() {
        return this.cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getAvgCostUnit() {
        return this.avgCostUnit;
    }

    public void setAvgCostUnit(String avgCostUnit) {
        this.avgCostUnit = avgCostUnit;
    }

    public String getZone() {
        return this.zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getGpsGoogle() {
        return this.gpsGoogle;
    }

    public void setGpsGoogle(String gpsGoogle) {
        this.gpsGoogle = gpsGoogle;
    }

    public int getViewSpotCity() {
        return this.viewSpotCity;
    }

    public void setViewSpotCity(int viewSpotCity) {
        this.viewSpotCity = viewSpotCity;
    }

    public String getGpsBaidu() {
        return this.gpsBaidu;
    }

    public void setGpsBaidu(String gpsBaidu) {
        this.gpsBaidu = gpsBaidu;
    }

    public String getScore() {
        return this.score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getGps() {
        return this.gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getScoreTotle() {
        return this.scoreTotle;
    }

    public void setScoreTotle(String scoreTotle) {
        this.scoreTotle = scoreTotle;
    }

    public String getGpsGaode() {
        return this.gpsGaode;
    }

    public void setGpsGaode(String gpsGaode) {
        this.gpsGaode = gpsGaode;
    }

    public String getScoreType() {
        return this.scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getViewSpotCountry() {
        return this.viewSpotCountry;
    }

    public void setViewSpotCountry(int viewSpotCountry) {
        this.viewSpotCountry = viewSpotCountry;
    }

    public String getYwTotle() {
        return this.ywTotle;
    }

    public void setYwTotle(String ywTotle) {
        this.ywTotle = ywTotle;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel copy(){
        Hotel hotel = new Hotel();
        hotel.id = id;
        hotel.img = img;
        hotel.ywTotle = ywTotle;
        hotel.viewSpotCountry = viewSpotCountry;
        hotel.distance =distance;
        hotel.scoreType =scoreType;
        hotel.gpsGaode = gpsGaode;
        hotel.scoreTotle = scoreTotle;
        hotel.gps = gps;
        hotel.score = score;
        hotel.gpsBaidu = gpsBaidu;
        hotel.viewSpotCity = viewSpotCity;
        hotel.gpsGoogle = gpsGoogle;
        hotel.cityName = cityName;
        hotel.zone = zone;
        hotel.avgCostUnit = avgCostUnit;
        hotel.cName = cName;
        hotel.percentPeople = percentPeople;
        hotel.name = name;
        hotel.viewurl = viewurl;
        hotel.shortDesc = shortDesc;
        hotel.tag = tag;
        hotel.avgCost = avgCost;
        hotel.scoreDesc = shortDesc;
        hotel.scheduleId = scheduleId;
        return hotel;
    }

}
