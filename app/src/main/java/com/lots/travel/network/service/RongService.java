package com.lots.travel.network.service;

import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.Empty;
import com.lots.travel.network.model.request.GetRongToken;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nalanzi on 2018/1/31.
 */

public interface RongService {

    //获取融云即时通讯token
    @POST("rongAPI_getToken")
    Single<HttpResult<String>> getToken(@Body GetRongToken params);

}
