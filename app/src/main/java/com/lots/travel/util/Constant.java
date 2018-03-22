package com.lots.travel.util;

/**
 * Created by nalanzi on 2017/9/11.
 */

public class Constant {
    //应用英文名，sp名称
    public static final String SOSONA = "sosona";


    //http请求服务器
    public static final String HOST_HTTP = "http://www.sosona.cn:8080";
    //h5服务器地址 - 正式为https://web.sosona.com:2828
    public static final String HOST_H5 = "http://www.sosona.cn:2828";
    //图片地址 - 如果图片地址不全，使用它补全即可
    public static final String PATH_OSS_IMAGE = "https://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/";

    //微信开放平台
    public static final String WX_APP_ID="wx4f5dad0f41bb5a7d";
    //微信开放平台安全码
    public static final String WX_APP_SECRET="cbb6bf01c64e64aa869d497f600b1270";


    //首页 - 城市页面地址
    public static final String PATH_HOME_CITY = HOST_H5+"/travel/indexStyle";
    //首页 - 旅行页面地址
    public static final String PATH_HOME_TRIP = HOST_H5+"/travel/recruitList";
    //招募详情 - 加上招募id即可
    public static final String PATH_RECRUIT_DETAIL = HOST_H5+"/travel/goodsDetail?baseId=";
    //个人中心 - 我的订单
    public static final String PATH_MINE_ORDER = HOST_H5+"/travel/myRecruitOrderList";
    //个人中心 - 我的旅行
    public static final String PATH_MINE_TRIP = HOST_H5+"/travel/myTravelList";
    //个人中心 - 我的活动
    public static final String PATH_MINE_ACTIVITY = HOST_H5+"/travel/myActivity";

    //众筹详情 - 加上招募id即可
    public static final String PATH_CROWDFUNDING_DETAIL = HOST_H5+"/crowdfundingDetail?pid=";

    //城市
    public static final String PATH_CITY_DETAIL = HOST_H5+"/travel/cityDetail?viewId=";
}
