package com.lots.travel.network.service;

import com.lots.travel.main.info.mine.model.FollowActive;
import com.lots.travel.main.info.mine.model.FollowNote;
import com.lots.travel.main.info.mine.model.FollowPeople;
import com.lots.travel.main.info.mine.model.FollowPlace;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.model.request.AddComment;
import com.lots.travel.network.model.request.AddZan;
import com.lots.travel.network.model.request.FollowElement;
import com.lots.travel.network.model.request.GetComments;
import com.lots.travel.network.model.request.GetFollowActives;
import com.lots.travel.network.model.request.GetFollowNotes;
import com.lots.travel.network.model.request.GetFollowPeoples;
import com.lots.travel.network.model.request.GetFollowPlaces;
import com.lots.travel.network.model.request.GetFollowTrips;
import com.lots.travel.network.model.request.SaveShipping;
import com.lots.travel.network.model.request.SaveTags;
import com.lots.travel.network.model.request.WantGo;
import com.lots.travel.network.model.result.Comments;
import com.lots.travel.network.model.result.ExpenseTag;
import com.lots.travel.main.info.mine.model.FollowTrip;
import com.lots.travel.network.model.result.MineTags;
import com.lots.travel.network.model.result.TagUserInfo;
import com.lots.travel.network.model.result.TripServiceTag;
import com.lots.travel.network.model.result.TravelTag;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by nalanzi on 2018/1/4.
 */

public interface CommonService {
    /**
     * 1 - 旅行习惯
     * 2 - 兴趣爱好
     * 4 - 城市主题
     * 旅行服务
     * 费用包含or不包含
     * 旅行主题
     * 旅行查询条件
     */

    @GET("user_listLabelAction?tag=旅行查询条件")
    Single<HttpResult<List<TravelTag>>> getTripConditions();

    @GET("user_listLabelAction?tag=旅行主题")
    Single<HttpResult<List<TravelTag>>> getTravelTags();

    @GET("user_listLabelAction?tag=旅行服务")
    Single<HttpResult<List<TripServiceTag>>> getTripServiceTags();

    @GET("user_listLabelAction?tag=费用包含or不包含")
    Single<HttpResult<List<ExpenseTag>>> getExpenseTags();

    @GET("user_listLabelAction?tags=1,2")
    Single<HttpResult<List<MineTags>>> getMineTags();

    //保存用户标签
    @POST("register_saveTagInfo")
    Single<HttpResult<TagUserInfo>> saveTags(@Body SaveTags params);

    //通用评论列表
    @POST("common_appservice_getCommentPageList")
    Single<HttpResult<Comments>> getComments(@Body GetComments params);

    //通用点赞接口
    @POST("common_appservice_addZan")
    Single<HttpResult<String>> addZan(@Body AddZan params);

    //通用评论接口
    @POST("common_appservice_addComment")
    Single<HttpResult<String>> addComment(@Body AddComment params);

    //关注的旅行达人
    @POST("common_appservice_getFollowPageList")
    Single<HttpResult<List<FollowPeople>>> getFollowPeoples(@Body GetFollowPeoples params);

    //我想去的目的地
    @POST("common_appservice_getFollowPageList")
    Single<HttpResult<List<FollowPlace>>> getFollowPlaces(@Body GetFollowPlaces params);

    //招募收藏
    @POST("common_appservice_getFollowPageList")
    Single<HttpResult<List<FollowTrip>>> getFollowTrips(@Body GetFollowTrips params);

    //活动收藏
    @POST("common_appservice_getFollowPageList")
    Single<HttpResult<List<FollowActive>>> getFollowActives(@Body GetFollowActives params);

    //游记收藏
    @POST("common_appservice_getFollowPageList")
    Single<HttpResult<List<FollowNote>>> getFollowNotes(@Body GetFollowNotes params);

    //想去、去过接口
    @POST("common_appservice_addWantgo")
    Single<HttpResult<String>> wantGo(@Body WantGo params);

    //收货地址
    @POST("register_saveDeliveryAddress")
    Single<HttpResult<SaveShipping>> saveShippingAddress(@Body SaveShipping params);

    //关注
    @POST("common_appservice_followElement")
    Single<HttpResult<String>> followElement(@Body FollowElement params);
}
