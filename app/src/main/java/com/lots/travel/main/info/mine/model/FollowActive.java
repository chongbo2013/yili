package com.lots.travel.main.info.mine.model;

/**
 * Created by nalanzi on 2018/1/23.
 */

public class FollowActive {
    //1招募；2失败3成功，4 活动进行中 5 结束
    public static final int STATUS_TRIP = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_SUCCESS = 3;
    public static final int STATUS_DOING = 4;
    public static final int STATUS_COMPLETED = 5;

    /**
     * baseId : 0
     * uuid : 853e90a9-62dd-4458-8403-4d98ce5d9fb2
     * faceImg640 : http://zyzc-bucket01.oss-cn-hangzhou.aliyuncs.com/14124/icon/1508727841248.png
     * constellation :
     * moneyStyle : AA
     * moneyDesc :
     * mobileCheck : 1
     * moneyImgs :
     * tripimg : http://china-bucket-tupian.oss-cn-hangzhou.aliyuncs.com/develop/14124/activity/1511434102595/15114337943390.png
     * cityTo : 32
     * dateFrom : 2017-11-23 00:00
     * peopleVideoImg : http://china-bucket-tupian.oss-cn-hangzhou.aliyuncs.com/develop/14124/activity/1511434102595/1511438863286.png
     * peopleVideo : http://china-bucket-shipin.oss-cn-hangzhou.aliyuncs.com/develop/14124/activity/1511434102595/1511438863286.mp4
     * mobilePhone : 1300000008
     * peopleSoundLen : 1.4
     * peopleSound : http://china-bucket-shipin.oss-cn-hangzhou.aliyuncs.com/develop/14124/activity/1511434102595/1511438844363.mp3
     * peopleDesc : 西湖
     * style : 3
     * triptitle :
     * userType : 4
     * maritalStatus : 3
     * status : 5
     * birthday :
     * peopleImgs : http://china-bucket-tupian.oss-cn-hangzhou.aliyuncs.com/develop/14124/activity/1511434102595/15114338230850.png
     * faceImg : http://zyzc-bucket01.oss-cn-hangzhou.aliyuncs.com/14124/icon/1508727841248.png
     * moneySoundLen :
     * moneyVideoImg :
     * title : 海外代购
     * moneyContain :
     * faceImg64 : http://zyzc-bucket01.oss-cn-hangzhou.aliyuncs.com/14124/icon/1508727841248.png
     * gatheringPlaceAddress : 文三路59号
     * company :
     * buyGoCount : 0
     * tag1 : 0
     * activityUserId : 14124
     * buyCount : 0
     * moneySound :
     * authStatus : 0
     * sex : 2
     * userName : a田田
     * tag4 : 0
     * people : 4
     * userId : 14124
     * tag2 : 0
     * tag3 : 0
     * realName : a田田
     * money : 0
     * moneyVideo :
     * activityStyle : 摄影约拍
     * roamId :
     * moneyNotContain :
     * dateTo : 2017-11-23 00:01
     * jsonExt : {"qq":"413555594","dingjinRefund":"1","phone":"13999","wechat":"soupcy","source":"owner","cityId":"1379","dingjin":10000,"gps":"30.277660,120.147220","localId":"","countryId":"1677","localChildId":""}
     */

    private String baseId;
    private String uuid;
    private String faceImg640;
    private String constellation;
    private String moneyStyle;
    private String moneyDesc;
    private String mobileCheck;
    private String moneyImgs;
    private String tripimg;
    private String cityTo;
    private String dateFrom;
    private String peopleVideoImg;
    private String peopleVideo;
    private String mobilePhone;
    private String peopleSoundLen;
    private String peopleSound;
    private String peopleDesc;
    private int style;
    private String triptitle;
    private String userType;
    private String maritalStatus;
    private int status;
    private String birthday;
    private String peopleImgs;
    private String faceImg;
    private String moneySoundLen;
    private String moneyVideoImg;
    private String title;
    private String moneyContain;
    private String faceImg64;
    private String gatheringPlaceAddress;
    private String company;
    private int buyGoCount;//实际确认可同行人数
    private int tag1;
    private int activityUserId;
    private int buyCount;//实际下单人数
    private String moneySound;
    private int authStatus;
    private String sex;
    private String userName;
    private int tag4;
    private int people;
    private int userId;
    private int tag2;
    private int tag3;
    private String realName;
    private int money;
    private String moneyVideo;
    private String activityStyle;
    private String roamId;
    private String moneyNotContain;
    private String dateTo;
    private String jsonExt;

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFaceImg640() {
        return faceImg640;
    }

    public void setFaceImg640(String faceImg640) {
        this.faceImg640 = faceImg640;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getMoneyStyle() {
        return moneyStyle;
    }

    public void setMoneyStyle(String moneyStyle) {
        this.moneyStyle = moneyStyle;
    }

    public String getMoneyDesc() {
        return moneyDesc;
    }

    public void setMoneyDesc(String moneyDesc) {
        this.moneyDesc = moneyDesc;
    }

    public String getMobileCheck() {
        return mobileCheck;
    }

    public void setMobileCheck(String mobileCheck) {
        this.mobileCheck = mobileCheck;
    }

    public String getMoneyImgs() {
        return moneyImgs;
    }

    public void setMoneyImgs(String moneyImgs) {
        this.moneyImgs = moneyImgs;
    }

    public String getTripimg() {
        return tripimg;
    }

    public void setTripimg(String tripimg) {
        this.tripimg = tripimg;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getPeopleVideoImg() {
        return peopleVideoImg;
    }

    public void setPeopleVideoImg(String peopleVideoImg) {
        this.peopleVideoImg = peopleVideoImg;
    }

    public String getPeopleVideo() {
        return peopleVideo;
    }

    public void setPeopleVideo(String peopleVideo) {
        this.peopleVideo = peopleVideo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPeopleSoundLen() {
        return peopleSoundLen;
    }

    public void setPeopleSoundLen(String peopleSoundLen) {
        this.peopleSoundLen = peopleSoundLen;
    }

    public String getPeopleSound() {
        return peopleSound;
    }

    public void setPeopleSound(String peopleSound) {
        this.peopleSound = peopleSound;
    }

    public String getPeopleDesc() {
        return peopleDesc;
    }

    public void setPeopleDesc(String peopleDesc) {
        this.peopleDesc = peopleDesc;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getTriptitle() {
        return triptitle;
    }

    public void setTriptitle(String triptitle) {
        this.triptitle = triptitle;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPeopleImgs() {
        return peopleImgs;
    }

    public void setPeopleImgs(String peopleImgs) {
        this.peopleImgs = peopleImgs;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public String getMoneySoundLen() {
        return moneySoundLen;
    }

    public void setMoneySoundLen(String moneySoundLen) {
        this.moneySoundLen = moneySoundLen;
    }

    public String getMoneyVideoImg() {
        return moneyVideoImg;
    }

    public void setMoneyVideoImg(String moneyVideoImg) {
        this.moneyVideoImg = moneyVideoImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoneyContain() {
        return moneyContain;
    }

    public void setMoneyContain(String moneyContain) {
        this.moneyContain = moneyContain;
    }

    public String getFaceImg64() {
        return faceImg64;
    }

    public void setFaceImg64(String faceImg64) {
        this.faceImg64 = faceImg64;
    }

    public String getGatheringPlaceAddress() {
        return gatheringPlaceAddress;
    }

    public void setGatheringPlaceAddress(String gatheringPlaceAddress) {
        this.gatheringPlaceAddress = gatheringPlaceAddress;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getBuyGoCount() {
        return buyGoCount;
    }

    public void setBuyGoCount(int buyGoCount) {
        this.buyGoCount = buyGoCount;
    }

    public int getTag1() {
        return tag1;
    }

    public void setTag1(int tag1) {
        this.tag1 = tag1;
    }

    public int getActivityUserId() {
        return activityUserId;
    }

    public void setActivityUserId(int activityUserId) {
        this.activityUserId = activityUserId;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public String getMoneySound() {
        return moneySound;
    }

    public void setMoneySound(String moneySound) {
        this.moneySound = moneySound;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
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

    public int getTag4() {
        return tag4;
    }

    public void setTag4(int tag4) {
        this.tag4 = tag4;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTag2() {
        return tag2;
    }

    public void setTag2(int tag2) {
        this.tag2 = tag2;
    }

    public int getTag3() {
        return tag3;
    }

    public void setTag3(int tag3) {
        this.tag3 = tag3;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMoneyVideo() {
        return moneyVideo;
    }

    public void setMoneyVideo(String moneyVideo) {
        this.moneyVideo = moneyVideo;
    }

    public String getActivityStyle() {
        return activityStyle;
    }

    public void setActivityStyle(String activityStyle) {
        this.activityStyle = activityStyle;
    }

    public String getRoamId() {
        return roamId;
    }

    public void setRoamId(String roamId) {
        this.roamId = roamId;
    }

    public String getMoneyNotContain() {
        return moneyNotContain;
    }

    public void setMoneyNotContain(String moneyNotContain) {
        this.moneyNotContain = moneyNotContain;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getJsonExt() {
        return jsonExt;
    }

    public void setJsonExt(String jsonExt) {
        this.jsonExt = jsonExt;
    }
}
