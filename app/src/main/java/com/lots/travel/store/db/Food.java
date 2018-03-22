package com.lots.travel.store.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.model.TripDaysInfo;
import com.lots.travel.util.TypeUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nalanzi on 2017/11/2.
 */
//城市搜索餐厅
@Entity
public class Food implements Parcelable {
    /**
     * area : 乌布
     * img : http://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/food/local/1491902181397_1491902181397.jpg
     * ywTotle : 还有个行程去了这里
     * viewSpotCountry : 2338
     * foodType : 东南亚菜
     * gpsGaode :
     * scoreTotle : 509
     * gps : 南纬S8°29′39.47″ 东经E115°16′22.61″
     * score : 4
     * gpsBaidu :
     * viewSpotCity : 323
     * gpsGoogle : -8.4942968646,115.2729481459
     * cityName : 巴厘岛
     * avgCostUnit : ￥
     * cName : 印度尼西亚
     * percentPeople : 64的游客去过
     * name : 脏鸭餐(乌布总店)
     * viewurl : http://m.dianping.com/shop/17773130
     * shortDesc : 很有当地特色，可欣赏原生态风光。
     * id : 1450
     * tag : 风景宜人 东南亚菜 人气火爆 美食
     * avgCost : 112
     */
    @Id
    private Long id;
    private String area;
    private String img;
    private String ywTotle;
    private int viewSpotCountry;
    private String foodType;
    private String gpsGaode;
    private String scoreTotle;
    private String gps;
    private float score;
    private String gpsBaidu;
    private int viewSpotCity;
    private String gpsGoogle;
    private String cityName;
    private String avgCostUnit;
    private String cName;
    private String percentPeople;
    private String name;
    private String viewurl;
    private String shortDesc;
    private String tag;
    private String avgCost;
    private String scheduleId;

    public static Food create(TripDaysInfo.ComponentInfo src){
        Food food = new Food();
        food.id = (long) src.getId();
        food.score = TypeUtil.str2float(src.getScore());
        food.img = src.getImg();
        food.gpsGoogle = src.getGpsGoogle();
        food.ywTotle = src.getYwTotle();
        food.percentPeople = src.getPercentPeople();
        food.foodType = src.getFoodType();
        food.name = src.getName();
        food.viewurl = src.getViewurl();
        food.shortDesc = src.getShortDesc();
        food.tag = src.getTag();
        return food;
    }

    protected Food(Parcel in) {
        id = in.readLong();
        area = in.readString();
        img = in.readString();
        ywTotle = in.readString();
        viewSpotCountry = in.readInt();
        foodType = in.readString();
        gpsGaode = in.readString();
        scoreTotle = in.readString();
        gps = in.readString();
        score = in.readFloat();
        gpsBaidu = in.readString();
        viewSpotCity = in.readInt();
        gpsGoogle = in.readString();
        cityName = in.readString();
        avgCostUnit = in.readString();
        cName = in.readString();
        percentPeople = in.readString();
        name = in.readString();
        viewurl = in.readString();
        shortDesc = in.readString();
        tag = in.readString();
        avgCost = in.readString();
        scheduleId = in.readString();
    }

    @Generated(hash = 427745186)
    public Food(Long id, String area, String img, String ywTotle, int viewSpotCountry, String foodType,
            String gpsGaode, String scoreTotle, String gps, float score, String gpsBaidu,
            int viewSpotCity, String gpsGoogle, String cityName, String avgCostUnit, String cName,
            String percentPeople, String name, String viewurl, String shortDesc, String tag,
            String avgCost, String scheduleId) {
        this.id = id;
        this.area = area;
        this.img = img;
        this.ywTotle = ywTotle;
        this.viewSpotCountry = viewSpotCountry;
        this.foodType = foodType;
        this.gpsGaode = gpsGaode;
        this.scoreTotle = scoreTotle;
        this.gps = gps;
        this.score = score;
        this.gpsBaidu = gpsBaidu;
        this.viewSpotCity = viewSpotCity;
        this.gpsGoogle = gpsGoogle;
        this.cityName = cityName;
        this.avgCostUnit = avgCostUnit;
        this.cName = cName;
        this.percentPeople = percentPeople;
        this.name = name;
        this.viewurl = viewurl;
        this.shortDesc = shortDesc;
        this.tag = tag;
        this.avgCost = avgCost;
        this.scheduleId = scheduleId;
    }

    @Generated(hash = 866324199)
    public Food() {
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(area);
        dest.writeString(img);
        dest.writeString(ywTotle);
        dest.writeInt(viewSpotCountry);
        dest.writeString(foodType);
        dest.writeString(gpsGaode);
        dest.writeString(scoreTotle);
        dest.writeString(gps);
        dest.writeFloat(score);
        dest.writeString(gpsBaidu);
        dest.writeInt(viewSpotCity);
        dest.writeString(gpsGoogle);
        dest.writeString(cityName);
        dest.writeString(avgCostUnit);
        dest.writeString(cName);
        dest.writeString(percentPeople);
        dest.writeString(name);
        dest.writeString(viewurl);
        dest.writeString(shortDesc);
        dest.writeString(tag);
        dest.writeString(avgCost);
        dest.writeString(scheduleId);
    }

    public String getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
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

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
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

    public String getFoodType() {
        return this.foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
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

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food copy(){
        Food food = new Food();
        food.id = id;
        food.area =area;
        food.img = img;
        food.ywTotle = ywTotle;
        food.viewSpotCountry = viewSpotCountry;
        food.foodType = foodType;
        food.gpsGaode = gpsGaode;
        food.scoreTotle = scoreTotle;
        food.gps = gps;
        food.score = score;
        food.gpsBaidu = gpsBaidu;
        food.viewSpotCity = viewSpotCity;
        food.gpsGoogle = gpsGoogle;
        food.cityName = cityName;
        food.avgCostUnit = avgCostUnit;
        food.cName = cName;
        food.percentPeople = percentPeople;
        food.name = name;
        food.viewurl = viewurl;
        food.shortDesc = shortDesc;
        food.tag = tag;
        food.avgCost = avgCost;
        food.scheduleId = scheduleId;
        return food;
    }
}
