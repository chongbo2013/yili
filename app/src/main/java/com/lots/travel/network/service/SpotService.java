package com.lots.travel.network.service;

import com.lots.travel.network.model.request.GetPlaces;
import com.lots.travel.place.model.Place;
import com.lots.travel.store.db.Spot;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.GetCityItems;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.ViewCity;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nalanzi on 2017/10/31.
 */
//景点级别，1国家，2城市，3景点，4一般子景点
public interface SpotService {

    /**
     * 获取景点接口，有三种调用方式：
     *    1、按viewId查询
     *    2、按viewName+viewType查询
     *    3、按viewName+viewTypes查询，viewTypes使用逗号分隔
     *
     *    viewSpot_getAllViews，GET请求，
     *          viewId、viewName、viewType、viewTypes、pageNo、pageSize
     *
     */
    @GET("viewSpot_getAllViews")
    Single<HttpResult<List<ViewCity>>> getCities(@Query("viewType") String viewType);

    //根据城市获取城市餐厅
    @POST("viewSpot_getFoodListByCity")
    Single<HttpResult<List<Food>>> getFoodListByCity(@Body GetCityItems params);

    //根据城市获取城市酒店
    @POST("viewSpot_getHotelListByCity")
    Single<HttpResult<List<Hotel>>> getHotelListByCity(@Body GetCityItems params);

    //获取某城市的景点列表
    @POST("viewSpot_getViewSpotList.action")
    Single<HttpResult<List<Spot>>> getSpotListByCity(@Body GetCityItems params);

    //获取城市或者国家列表 - 不需要cityId、countryId
    @POST("viewSpot_getViewSpotList.action")
    Single<HttpResult<List<Place>>> getPlaceList(@Body GetPlaces params);

}
