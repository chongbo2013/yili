package com.lots.travel.network.service;

import com.lots.travel.main.info.person.Album;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.Empty;
import com.lots.travel.network.model.request.GetAlbum;
import com.lots.travel.network.model.request.UpdateUserInfo;
import com.lots.travel.network.model.result.EditUserInfo;
import com.lots.travel.network.model.result.GetShippingResult;
import com.lots.travel.network.model.result.UserInformation;


import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 查询用户相关的一些信息
 */
public interface UserService {


    @GET("u_getUserDetail_action")
    Single<HttpResult<UserInformation>> getAllInformation(@Query("targetId") String targetId, @Query("userId") String userId);

    @POST("register_updateUserInfo")
    Single<HttpResult<EditUserInfo>> updateUserInfo(@Body UpdateUserInfo params);

    //获取相册
    @POST("common_appservice_getAlbum")
    Single<HttpResult<List<Album>>> getAlbum(@Body GetAlbum params);

    //获取收货地址
    @POST("register_getUserInfo_action")
    Single<HttpResult<GetShippingResult>> getShippingAddress(@Body Empty params);

    //添加收货地址
    @POST("register_saveDeliveryAddress")
    Single<HttpResult<String>> saveShippingAddress(@Body Empty params);

}
