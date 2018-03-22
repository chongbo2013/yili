package com.lots.travel.place.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2017/12/18.
 */

public class Place implements Parcelable {
    /**
     * pxHot : 0
     * px : 10
     * recommend : 20
     * viewImg : https://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/1506660420429.jpg
     * countryId : 1596
     * score : 3
     * tagType : 文化 摄影
     * tag : 文化 摄影
     * ywTotle : 还有个行程去了这里
     * gpsGoogle : 8.9806034000,38.7577605000
     * viewId : 133714
     * percentPeople :
     * viewType : 2
     * name : 亚的斯亚贝巴
     * shortDesc : 非洲第四大城市，商业与自然的和谐存在。
     * countryName : 埃塞俄比亚
     * ywDay : 1
     * card : 1
     * status : 1
     * spotName : 亚的斯亚贝巴
     */
    private int viewId;
    private int pxHot;
    private int px;
    private int recommend;
    private String viewImg;
    private int countryId;
    private String score;
    private String tagType;
    private String tag;
    private String ywTotle;
    private String gpsGoogle;
    private String percentPeople;
    private int viewType;
    private String name;
    private String shortDesc;
    private String countryName;
    private int ywDay;
    private int card;
    private int status;
    private String spotName;

    public Place(){}

    protected Place(Parcel in) {
        viewId = in.readInt();
        pxHot = in.readInt();
        px = in.readInt();
        recommend = in.readInt();
        viewImg = in.readString();
        countryId = in.readInt();
        score = in.readString();
        tagType = in.readString();
        tag = in.readString();
        ywTotle = in.readString();
        gpsGoogle = in.readString();
        percentPeople = in.readString();
        viewType = in.readInt();
        name = in.readString();
        shortDesc = in.readString();
        countryName = in.readString();
        ywDay = in.readInt();
        card = in.readInt();
        status = in.readInt();
        spotName = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public int getPxHot() {
        return pxHot;
    }

    public void setPxHot(int pxHot) {
        this.pxHot = pxHot;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getViewImg() {
        return viewImg;
    }

    public void setViewImg(String viewImg) {
        this.viewImg = viewImg;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getYwTotle() {
        return ywTotle;
    }

    public void setYwTotle(String ywTotle) {
        this.ywTotle = ywTotle;
    }

    public String getGpsGoogle() {
        return gpsGoogle;
    }

    public void setGpsGoogle(String gpsGoogle) {
        this.gpsGoogle = gpsGoogle;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public String getPercentPeople() {
        return percentPeople;
    }

    public void setPercentPeople(String percentPeople) {
        this.percentPeople = percentPeople;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getYwDay() {
        return ywDay;
    }

    public void setYwDay(int ywDay) {
        this.ywDay = ywDay;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewId);
        dest.writeInt(pxHot);
        dest.writeInt(px);
        dest.writeInt(recommend);
        dest.writeString(viewImg);
        dest.writeInt(countryId);
        dest.writeString(score);
        dest.writeString(tagType);
        dest.writeString(tag);
        dest.writeString(ywTotle);
        dest.writeString(gpsGoogle);
        dest.writeString(percentPeople);
        dest.writeInt(viewType);
        dest.writeString(name);
        dest.writeString(shortDesc);
        dest.writeString(countryName);
        dest.writeInt(ywDay);
        dest.writeInt(card);
        dest.writeInt(status);
        dest.writeString(spotName);
    }
}
