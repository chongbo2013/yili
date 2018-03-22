package com.lots.travel.network.service;

import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.AddFootprint;
import com.lots.travel.network.model.request.GetFootprint;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nalanzi on 2017/11/18.
 */

public interface FootprintService {

    @POST("trip_get3and1List")
    Single<HttpResult<List<Footprint>>> getFootprint(@Body GetFootprint params);

    //新增足迹
    @POST("youji_addYouji")
    Single<HttpResult<String>> addFootprint(@Body AddFootprint params);

}
