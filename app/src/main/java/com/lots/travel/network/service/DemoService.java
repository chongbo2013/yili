package com.lots.travel.network.service;

import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.DemoBean;
import com.lots.travel.network.model.request.Empty;
import com.lots.travel.network.model.result.LoginByMobileCode;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nalanzi on 2017/9/16.
 */
//只是测试登录后的POST和GET
public interface DemoService {

    //POST请求使用的json，千万不要在这里使用@Header注解
    @POST("common_appservice_getAlbum")
    Observable<HttpResult<Object>> getAlbum(@Body DemoBean bean);

    @GET("viewSpot_getAllViews")
    Single<HttpResult<String>> getAllViews(@Query("viewId") String viewId, @Query("viewType") String viewType,@Query("viewName") String viewName);


//    @POST("viewSpot_getFoodListByCity")
//    Single<HttpResult<String>> getFoodListByCity(@Body GetHotel params);
//
//    @POST("viewSpot_getHotelListByCity")
//    Single<HttpResult<String>> getHoltelListByCity(@Body GetHotel params);
//
//
//    @POST("viewSpot_getViewSpotList.action")
//    Single<HttpResult<String>> getViewSpotList(@Body GetHotel params);

    /*
http://sosona.cn:8080/trip/addTripDayInfo.action?from=android&userId=11941

{"baseId":"5502b2b0-49f9-406b-a385-0924cc052440","dayInfos":[{"cityId":"233","dataKey":"2386","day":"1","partId":"566280431_2","style":"spot"}],"status":"0","from":"android","userId":"11941","timestamp":"1509993368091","nonceStr":"388768","fix":"2","signature":"A1D2DBD1EE501884AAB5187EDE1DFB4B"}

     */


}
