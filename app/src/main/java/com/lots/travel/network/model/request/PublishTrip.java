package com.lots.travel.network.model.request;

import java.util.List;

/**
 * Created by nalanzi on 2018/1/4.
 */

public class PublishTrip {
    private String baseId;
    private List<Recruit> recruit;

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public List<Recruit> getRecruit() {
        return recruit;
    }

    public void setRecruit(List<Recruit> recruit) {
        this.recruit = recruit;
    }

    public static class Recruit{
        //"156"
        private String cityFrom,cityTo;
        //"2017-09-10"
        private String dateFrom,dateTo;
        //1：招募、2：悬赏
        private String style;
        //提供的服务 99,100,203、其他服务
        private String provideService,provideOtherService;
        //服务需求时间、开始时间、结束时间
        private String serviceTime,serviceTimeS,serviceTimeE;
        //集合地点："杭州"、详细地址
        private String gatheringPlace,gatheringPlaceAddress;
        //人数
        private String people;
        //人数
        private String peopleDesc;
        private String peopleVideo,peopleVideoImg;
        private String peopleImgs;
        private String peopleSound,peopleSoundLen;
        //金额
        private String money;
        private String moneyContain,moneyNotContain;

        private String moneyDesc;
        private String moneyVideo,moneyVideoImg;
        private String moneyImgs;
        private String moneySound,moneySoundLen;
        //1：是、0：否
        private String hasRoomDiff;
        private String roomDiff;
        //保险
        private String needInsurance;
        private String insurance;
        //优惠选项
        private String couponkeys;

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

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getProvideService() {
            return provideService;
        }

        public void setProvideService(String provideService) {
            this.provideService = provideService;
        }

        public String getProvideOtherService() {
            return provideOtherService;
        }

        public void setProvideOtherService(String provideOtherService) {
            this.provideOtherService = provideOtherService;
        }

        public String getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(String serviceTime) {
            this.serviceTime = serviceTime;
        }

        public String getServiceTimeS() {
            return serviceTimeS;
        }

        public void setServiceTimeS(String serviceTimeS) {
            this.serviceTimeS = serviceTimeS;
        }

        public String getServiceTimeE() {
            return serviceTimeE;
        }

        public void setServiceTimeE(String serviceTimeE) {
            this.serviceTimeE = serviceTimeE;
        }

        public String getGatheringPlace() {
            return gatheringPlace;
        }

        public void setGatheringPlace(String gatheringPlace) {
            this.gatheringPlace = gatheringPlace;
        }

        public String getGatheringPlaceAddress() {
            return gatheringPlaceAddress;
        }

        public void setGatheringPlaceAddress(String gatheringPlaceAddress) {
            this.gatheringPlaceAddress = gatheringPlaceAddress;
        }

        public String getPeople() {
            return people;
        }

        public void setPeople(String people) {
            this.people = people;
        }

        public String getPeopleDesc() {
            return peopleDesc;
        }

        public void setPeopleDesc(String peopleDesc) {
            this.peopleDesc = peopleDesc;
        }

        public String getPeopleVideo() {
            return peopleVideo;
        }

        public void setPeopleVideo(String peopleVideo) {
            this.peopleVideo = peopleVideo;
        }

        public String getPeopleVideoImg() {
            return peopleVideoImg;
        }

        public void setPeopleVideoImg(String peopleVideoImg) {
            this.peopleVideoImg = peopleVideoImg;
        }

        public String getPeopleImgs() {
            return peopleImgs;
        }

        public void setPeopleImgs(String peopleImgs) {
            this.peopleImgs = peopleImgs;
        }

        public String getPeopleSound() {
            return peopleSound;
        }

        public void setPeopleSound(String peopleSound) {
            this.peopleSound = peopleSound;
        }

        public String getPeopleSoundLen() {
            return peopleSoundLen;
        }

        public void setPeopleSoundLen(String peopleSoundLen) {
            this.peopleSoundLen = peopleSoundLen;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMoneyContain() {
            return moneyContain;
        }

        public void setMoneyContain(String moneyContain) {
            this.moneyContain = moneyContain;
        }

        public String getMoneyNotContain() {
            return moneyNotContain;
        }

        public void setMoneyNotContain(String moneyNotContain) {
            this.moneyNotContain = moneyNotContain;
        }

        public String getMoneyDesc() {
            return moneyDesc;
        }

        public void setMoneyDesc(String moneyDesc) {
            this.moneyDesc = moneyDesc;
        }

        public String getMoneyVideo() {
            return moneyVideo;
        }

        public void setMoneyVideo(String moneyVideo) {
            this.moneyVideo = moneyVideo;
        }

        public String getMoneyVideoImg() {
            return moneyVideoImg;
        }

        public void setMoneyVideoImg(String moneyVideoImg) {
            this.moneyVideoImg = moneyVideoImg;
        }

        public String getMoneyImgs() {
            return moneyImgs;
        }

        public void setMoneyImgs(String moneyImgs) {
            this.moneyImgs = moneyImgs;
        }

        public String getMoneySound() {
            return moneySound;
        }

        public void setMoneySound(String moneySound) {
            this.moneySound = moneySound;
        }

        public String getMoneySoundLen() {
            return moneySoundLen;
        }

        public void setMoneySoundLen(String moneySoundLen) {
            this.moneySoundLen = moneySoundLen;
        }

        public String getHasRoomDiff() {
            return hasRoomDiff;
        }

        public void setHasRoomDiff(String hasRoomDiff) {
            this.hasRoomDiff = hasRoomDiff;
        }

        public String getRoomDiff() {
            return roomDiff;
        }

        public void setRoomDiff(String roomDiff) {
            this.roomDiff = roomDiff;
        }

        public String getNeedInsurance() {
            return needInsurance;
        }

        public void setNeedInsurance(String needInsurance) {
            this.needInsurance = needInsurance;
        }

        public String getInsurance() {
            return insurance;
        }

        public void setInsurance(String insurance) {
            this.insurance = insurance;
        }

        public String getCouponkeys() {
            return couponkeys;
        }

        public void setCouponkeys(String couponkeys) {
            this.couponkeys = couponkeys;
        }
    }

}
