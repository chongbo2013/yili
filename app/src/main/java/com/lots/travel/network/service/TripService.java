package com.lots.travel.network.service;

import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.AddTrip;
import com.lots.travel.network.model.request.GetMatchTrips;
import com.lots.travel.network.model.request.GetTripDaysInfo;
import com.lots.travel.network.model.request.GetTripInfo;
import com.lots.travel.network.model.request.MatchRoute;
import com.lots.travel.network.model.request.PublishTrip;
import com.lots.travel.network.model.request.SaveTrip;
import com.lots.travel.network.model.result.MatchedRoute;
import com.lots.travel.network.model.result.MoreRoute;
import com.lots.travel.network.model.result.TripInfo;
import com.lots.travel.schedule.model.TripDaysInfo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nalanzi on 2017/11/2.
 */

public interface TripService {

    //获取行程/游记的base信息
    @POST("trip_getTripBaseInfo")
    Single<HttpResult<TripInfo>> getTripInfo(@Body GetTripInfo params);

    //trip_addTripBase - 添加行程/游记base
    @POST("trip_addTripBase")
    Single<HttpResult<String>> addTrip(@Body AddTrip params);

    @POST("trip_addTripDayInfo")
    Single<HttpResult<String>> saveTripInfo(@Body SaveTrip params);

    //匹配行程
    @POST("trip_matchAndLoadTripV2")
    Single<HttpResult<MatchedRoute>> matchTrip(@Body MatchRoute params);

    //获取当前行程已经匹配到的行程列表
    @POST("trip_getMatchTripList")
    Single<HttpResult<List<MoreRoute>>> getMatchTrips(@Body GetMatchTrips params);

    @POST("trip_getTripDayinfoList")
    Single<HttpResult<TripDaysInfo>> getTripDaysInfo(@Body GetTripDaysInfo params);

    //发起招募、悬赏
    @POST("trip_tripFabu")
    Single<HttpResult<String>> publishTrip(@Body PublishTrip params);

}
