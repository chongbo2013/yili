package com.lots.travel.schedule.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2018/1/1.
 */

public class UserInfo implements Parcelable {
    private int userId;

    private String sex;

    private String userName;

    private String realName;

    private String mobilePhone;
    //感情状况，对应maritalStatus
    private String emotion;
    //头像对应faceImg
    private String portrait;
    //对应title - 职业
    private String profession;
    //关注 - meGz
    private String meGz;
    //粉丝 - gzMe
    private String gzMe;
    //认证状态
    private int authStatus;



    public static UserInfo create(TripDaysInfo.UserInfo src){
        UserInfo info = new UserInfo();
        info.userId = src.getUserId();
        info.sex = src.getSex();
        info.userName = src.getUserName();
        info.realName = src.getRealName();
        info.mobilePhone = src.getMobilePhone();
        info.emotion = src.getMaritalStatus();
        info.portrait = src.getFaceImg();
        info.profession = src.getTitle();
        info.meGz = src.getMeGz();
        info.gzMe = src.getGzMe();
        info.authStatus = src.getAuthStatus();
        return info;
    }

    public UserInfo(){}

    protected UserInfo(Parcel in) {
        userId = in.readInt();
        sex = in.readString();
        userName = in.readString();
        realName = in.readString();
        mobilePhone = in.readString();
        emotion = in.readString();
        portrait = in.readString();
        profession = in.readString();
        meGz = in.readString();
        gzMe = in.readString();
        authStatus = in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMeGz() {
        return meGz;
    }

    public void setMeGz(String meGz) {
        this.meGz = meGz;
    }

    public String getGzMe() {
        return gzMe;
    }

    public void setGzMe(String gzMe) {
        this.gzMe = gzMe;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(sex);
        dest.writeString(userName);
        dest.writeString(realName);
        dest.writeString(mobilePhone);
        dest.writeString(emotion);
        dest.writeString(portrait);
        dest.writeString(profession);
        dest.writeString(meGz);
        dest.writeString(gzMe);
        dest.writeInt(authStatus);
    }
}
