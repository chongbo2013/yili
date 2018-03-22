package com.lots.travel.footprint.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Footprint implements Parcelable{
    public static final String TRIP = "trip";
    public static final String ACTIVE = "activity";
    public static final String ACTIVE_JOIN = "activity_join";
    public static final String NOTE = "note";
    public static final String FOOTPRINT = "youji";
    public static final String LIVE = "zhibo";
    public static final String DESTINATION = "spot_collection";

    private String id;
    private String gpsData;
    private String gpsAddress;
    //字段中名称为private
    @SerializedName("private")
    private String privateState;
    //字段中名称为public
    @SerializedName("public")
    private String publicState;
    private String faceImg;
    private String px;
    private String faceImg640;
    private String userTitle;
    private String faceImg64;
    private String tripTitle;
    private String readTotle;
    private String json;
    private String event;
    private String tripVideo;
    // ,分隔
    private String tripImgs;
    private int imgCount;
    private long creattime;
    private int authStatus;
    private String sex;
    private int hasZan;
    private String zd;
    //，分隔
    private String tripVideoImg;
    private String userName;
    private int zanTotles;
    private long userId;
    private String eventStyle;
    private int commentTotles;

    //tag
    private String tag2;

    protected Footprint(Parcel in) {
        id = in.readString();
        gpsData = in.readString();
        gpsAddress = in.readString();
        privateState = in.readString();
        publicState = in.readString();
        faceImg = in.readString();
        px = in.readString();
        faceImg640 = in.readString();
        userTitle = in.readString();
        faceImg64 = in.readString();
        tripTitle = in.readString();
        readTotle = in.readString();
        json = in.readString();
        event = in.readString();
        tripVideo = in.readString();
        tripImgs = in.readString();
        imgCount = in.readInt();
        creattime = in.readLong();
        authStatus = in.readInt();
        sex = in.readString();
        hasZan = in.readInt();
        zd = in.readString();
        tripVideoImg = in.readString();
        userName = in.readString();
        zanTotles = in.readInt();
        userId = in.readLong();
        eventStyle = in.readString();
        commentTotles = in.readInt();
        tag2 = in.readString();
    }

    public static final Creator<Footprint> CREATOR = new Creator<Footprint>() {
        @Override
        public Footprint createFromParcel(Parcel in) {
            return new Footprint(in);
        }

        @Override
        public Footprint[] newArray(int size) {
            return new Footprint[size];
        }
    };

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public int getCommentTotles() {
        return commentTotles;
    }

    public void setCommentTotles(int commentTotles) {
        this.commentTotles = commentTotles;
    }

    public int incComment(){
        return ++commentTotles;
    }

    public int decComment(){
        return --commentTotles;
    }

    public long getCreattime() {
        return creattime;
    }

    public void setCreattime(long creattime) {
        this.creattime = creattime;
    }

    public String getTripVideo() {
        return tripVideo;
    }

    public void setTripVideo(String tripVideo) {
        this.tripVideo = tripVideo;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }

    public String getFaceImg640() {
        return faceImg640;
    }

    public void setFaceImg640(String faceImg640) {
        this.faceImg640 = faceImg640;
    }

    public String getFaceImg64() {
        return faceImg64;
    }

    public void setFaceImg64(String faceImg64) {
        this.faceImg64 = faceImg64;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public String getGpsData() {
        return gpsData;
    }

    public void setGpsData(String gpsData) {
        this.gpsData = gpsData;
    }

    public int getHasZan() {
        return hasZan;
    }

    public void setHasZan(int hasZan) {
        this.hasZan = hasZan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImgCount() {
        return imgCount;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getPrivateState() {
        return privateState;
    }

    public void setPrivateState(String privateState) {
        this.privateState = privateState;
    }

    public String getPublicState() {
        return publicState;
    }

    public void setPublicState(String publicState) {
        this.publicState = publicState;
    }

    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }

    public String getReadTotle() {
        return readTotle;
    }

    public void setReadTotle(String readTotle) {
        this.readTotle = readTotle;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTripImgs() {
        return tripImgs;
    }

    public void setTripImgs(String tripImgs) {
        this.tripImgs = tripImgs;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public String getTripVideoImg() {
        return tripVideoImg;
    }

    public void setTripVideoImg(String tripVideoImg) {
        this.tripVideoImg = tripVideoImg;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public int getZanTotles() {
        return zanTotles;
    }

    public void setZanTotles(int zanTotles) {
        this.zanTotles = zanTotles;
    }

    public int incZan(){
        return ++zanTotles;
    }

    public int decZan(){
        return --zanTotles;
    }

    public String getZd() {
        return zd;
    }

    public void setZd(String zd) {
        this.zd = zd;
    }

    public String getGpsAddress() {
        return gpsAddress;
    }

    public void setGpsAddress(String gpsAddress) {
        this.gpsAddress = gpsAddress;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(gpsData);
        dest.writeString(gpsAddress);
        dest.writeString(privateState);
        dest.writeString(publicState);
        dest.writeString(faceImg);
        dest.writeString(px);
        dest.writeString(faceImg640);
        dest.writeString(userTitle);
        dest.writeString(faceImg64);
        dest.writeString(tripTitle);
        dest.writeString(readTotle);
        dest.writeString(json);
        dest.writeString(event);
        dest.writeString(tripVideo);
        dest.writeString(tripImgs);
        dest.writeInt(imgCount);
        dest.writeLong(creattime);
        dest.writeInt(authStatus);
        dest.writeString(sex);
        dest.writeInt(hasZan);
        dest.writeString(zd);
        dest.writeString(tripVideoImg);
        dest.writeString(userName);
        dest.writeInt(zanTotles);
        dest.writeLong(userId);
        dest.writeString(eventStyle);
        dest.writeInt(commentTotles);
        dest.writeString(tag2);
    }
}
