package com.lots.travel.store.db;

import android.text.TextUtils;

import com.lots.travel.schedule.model.TripDaysInfo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 对应保存行程/游记每日信息接口
 */
@Entity
public class TripPart {
    public static final String STYLE_SPOT = "spot";
    public static final String STYLE_TRAFFIC = "traffic";
    public static final String STYLE_HOTEL = "hotel";
    public static final String STYLE_FOOD = "food";
    public static final String STYLE_DESC = "desc";
    public static final String STYLE_PLANE = "plane";

    private static AtomicLong GENERATOR = new AtomicLong(System.currentTimeMillis());

    public static TripPart create(TripDaysInfo.TripPartInfo src){
        TripPart part = new TripPart();
        part.cityId = src.getCityId();
        part.day = Integer.toString(src.getDay());
        part.style = src.getStyle();
        part.dataKey = src.getDataKey();
        part.tripVideo = src.getTripVideo();
        part.tripVideoImg = src.getTripVideoImg();
        part.tripImgs = src.getTripImgs();
        part.tripDesc = src.getTripDesc();
        part.tripSound = src.getTripSound();
        part.tripSoundLen = src.getTripSoundLen();
        part.transport = src.getTransport();
        part.distance = src.getDistance();
        part.useHour = src.getUseHour();
        part.useMinute = src.getUseMinute();
        part.hotelIn = Integer.toString(src.getHotelIn());
        if(STYLE_TRAFFIC.equals(src.getStyle()) && src.getInfo()!=null){
            TripDaysInfo.ComponentInfo info = src.getInfo();
            part.useMinute = info.getUseMinute();
            part.useHour = info.getUseHour();
            part.distance = info.getDistance();
        }
        return part;
    }

    /**
     * cityId : 当天所在城市id（非必填）
     * hotelIn : style=hotel时有效。是否入住酒店：1是0否
     * day : 当前天,从1开始
     * style : spot景点，traffic交通，hotel酒店，food餐厅，desc每日描述；plane机票
     * dataKey : style对应的主键key
     * data :
     * tripVideo : 视频url
     * tripVideoImg : 视频封面
     * tripImgs : 图片（多张用‘,’隔开）
     * tripSound : 语音地址
     * tripSoundLen : 语音长度 x秒
     * tripDesc : 文字描述
     * transport : style=traffic交通时有效。交通方式
     * distance : style=traffic交通时有效。距离
     * useHour : style=traffic交通时有效。用时 小时
     * useMinute : style=traffic交通时有效。用户 分钟
     */
    //存储到数据库
    @Id
    private Long id;
    private String scheduleId;
    private String cityId;
    private String hotelIn;
    private String day;
    private String style;
    private String dataKey;
    private String tripVideo;
    private String tripVideoImg;
    private String tripImgs;
    private String tripSound;
    private String tripSoundLen;
    private String tripDesc;
    private String transport;
    private String distance;
    private String useHour;
    private String useMinute;

    @Generated(hash = 1639586235)
    public TripPart(Long id, String scheduleId, String cityId, String hotelIn, String day,
            String style, String dataKey, String tripVideo, String tripVideoImg,
            String tripImgs, String tripSound, String tripSoundLen, String tripDesc,
            String transport, String distance, String useHour, String useMinute) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.cityId = cityId;
        this.hotelIn = hotelIn;
        this.day = day;
        this.style = style;
        this.dataKey = dataKey;
        this.tripVideo = tripVideo;
        this.tripVideoImg = tripVideoImg;
        this.tripImgs = tripImgs;
        this.tripSound = tripSound;
        this.tripSoundLen = tripSoundLen;
        this.tripDesc = tripDesc;
        this.transport = transport;
        this.distance = distance;
        this.useHour = useHour;
        this.useMinute = useMinute;
    }
    @Generated(hash = 1735320962)
    public TripPart() {
    }

    public String getUseMinute() {
        return this.useMinute;
    }
    public void setUseMinute(String useMinute) {
        this.useMinute = useMinute;
    }
    public String getUseHour() {
        return this.useHour;
    }
    public void setUseHour(String useHour) {
        this.useHour = useHour;
    }
    public String getDistance() {
        return this.distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getTransport() {
        return this.transport;
    }
    public void setTransport(String transport) {
        this.transport = transport;
    }
    public String getTripDesc() {
        return this.tripDesc;
    }
    public void setTripDesc(String tripDesc) {
        this.tripDesc = tripDesc;
    }
    public String getTripSoundLen() {
        return this.tripSoundLen;
    }
    public void setTripSoundLen(String tripSoundLen) {
        this.tripSoundLen = tripSoundLen;
    }
    public String getTripSound() {
        return this.tripSound;
    }
    public void setTripSound(String tripSound) {
        this.tripSound = tripSound;
    }
    public String getTripImgs() {
        return this.tripImgs;
    }
    public void setTripImgs(String tripImgs) {
        this.tripImgs = tripImgs;
    }
    public String getTripVideoImg() {
        return this.tripVideoImg;
    }
    public void setTripVideoImg(String tripVideoImg) {
        this.tripVideoImg = tripVideoImg;
    }
    public String getTripVideo() {
        return this.tripVideo;
    }
    public void setTripVideo(String tripVideo) {
        this.tripVideo = tripVideo;
    }
    public String getDataKey() {
        return this.dataKey;
    }
    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }
    public String getStyle() {
        return this.style;
    }
    public void setStyle(String style) {
        this.style = style;
    }
    public String getDay() {
        return this.day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getHotelIn() {
        return this.hotelIn;
    }
    public void setHotelIn(String hotelIn) {
        this.hotelIn = hotelIn;
    }
    public String getCityId() {
        return this.cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public String getScheduleId() {
        return this.scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public static Long genId(){
        return GENERATOR.incrementAndGet();
    }

    //视频、图片、语音、文本均为空
    public boolean isContentEmpty(){
        boolean soundEmpty = true;
        if(tripSoundLen!=null){
            try{
                soundEmpty = Integer.parseInt(tripSoundLen)<=0;
            }catch (Exception e){}
        }
        soundEmpty |= TextUtils.isEmpty(tripSound);
        return TextUtils.isEmpty(tripVideo)
                && soundEmpty
                && TextUtils.isEmpty(tripImgs)
                && TextUtils.isEmpty(tripDesc);
    }

    public TripPart copy(){
        TripPart part = new TripPart();
        part.id = id;
        part.scheduleId = scheduleId;
        part.cityId = cityId;
        part.hotelIn = hotelIn;
        part.day = day;
        part.style = style;
        part.dataKey = dataKey;
        part.tripVideo = tripVideo;
        part.tripVideoImg = tripVideoImg;
        part.tripImgs = tripImgs;
        part.tripSound = tripSound;
        part.tripSoundLen = tripSoundLen;
        part.tripDesc = tripDesc;
        part.transport = transport;
        part.distance = distance;
        part.useHour = useHour;
        part.useMinute = useMinute;
        return part;
    }
}
