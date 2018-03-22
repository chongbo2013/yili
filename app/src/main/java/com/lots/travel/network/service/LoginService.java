package com.lots.travel.network.service;

import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.WeChatLogin;
import com.lots.travel.network.model.result.LoginByMobileCode;
import com.lots.travel.network.model.result.LoginByWeChat;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nalanzi on 2017/9/16.
 */

public interface LoginService {

    //手机号登录
    @GET("login_by_mobileCode")
    Single<HttpResult<LoginByMobileCode>> loginByMobileCode(@Query("mobile") String mobile, @Query("code") String code);

    //获取验证码
    @GET("get_mobile_code")
    Single<HttpResult<String>> getMobileCode(@Query("mobile") String mobile);

    //微信登录
    @POST("register_saveWeixinInfo")
    Single<HttpResult<LoginByWeChat>> loginByWeChat(@Body WeChatLogin param);

}