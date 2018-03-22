package com.lots.travel.schedule.route.match;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.GetTripDaysInfo;
import com.lots.travel.network.model.result.MatchedRoute;
import com.lots.travel.network.service.TripService;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.base.preview.PreviewScheduleAdapter;
import com.lots.travel.schedule.base.preview.PreviewTopology;
import com.lots.travel.schedule.model.TripDaysInfo;
import com.lots.travel.schedule.model.UserInfo;
import com.lots.travel.schedule.route.edit.RouteEditScheduleActivity;
import com.lots.travel.schedule.widget.CircleIndicatorLayout;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.FoodDao;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.HotelDao;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.SpotDao;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.store.db.TripPartDao;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.widget.WindowInsertsToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class MatchedRoutePreviewActivity extends RxBaseActivity implements View.OnClickListener {
    private MatchedRoute mMatchedRoute;
    private Schedule mSourceSchedule;
    private boolean mFromMatchedTrips;

    private Schedule mSchedule;
    private UserInfo mUserInfo;

    private PreviewTopology mTopology;
    private DataManager mDataManager;
    private PreviewScheduleAdapter mScheduleAdapter;

    private int[] mTempLocation = new int[2];

    private TextView tvTitle;
    private Drawable toolbarBackground;
    private WindowInsertsToolbarLayout toolbarContainer;
    private CircleIndicatorLayout circleIndicator;
    private RecyclerView rvContent;
    private MatchPreviewHeaderHolder hHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_route_preview);

        //状态栏
        SystemBarCompat.fullscreen(this);

        initData(getIntent(),savedInstanceState);
        initViews();
        requestLoadData();
    }

    private void initData(Intent data,Bundle savedInstanceState){
        mSourceSchedule = data.getParcelableExtra(SOURCE_SCHEDULE);
        mMatchedRoute = data.getParcelableExtra(MATCHED_ROUTE);
        mFromMatchedTrips = data.getBooleanExtra(FROM_MATCHED_TRIPS,false);
        if(savedInstanceState!=null) {
            mSourceSchedule = savedInstanceState.getParcelable(SOURCE_SCHEDULE);
            mMatchedRoute = savedInstanceState.getParcelable(MATCHED_ROUTE);
            mFromMatchedTrips = savedInstanceState.getBoolean(FROM_MATCHED_TRIPS);
        }
    }

    private void initViews(){
        toolbarContainer = (WindowInsertsToolbarLayout) findViewById(R.id.toolbar_container);
        toolbarBackground = toolbarContainer.getBackground().mutate();

        tvTitle = (TextView) findViewById(R.id.tv_title);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        Button btnMore = (Button) findViewById(R.id.btn_more);
        btnMore.setText(mFromMatchedTrips ?
                R.string.matched_route_route_back:R.string.matched_route_route_more);
        btnMore.setOnClickListener(this);
        findViewById(R.id.btn_copy).setOnClickListener(this);

        circleIndicator = (CircleIndicatorLayout) findViewById(R.id.circle_indicator);

        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                if (toolbarBackground==null || mScheduleAdapter==null || hHeader==null)
                    return;

                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                int flatPosition = layoutManager.findFirstVisibleItemPosition();

                if(flatPosition!=0){
                    toolbarBackground.setAlpha(255);
                    tvTitle.setAlpha(1);
                    circleIndicator.setVisibility(View.VISIBLE);
                }else{
                    hHeader.itemView.getLocationOnScreen(mTempLocation);
                    int headerTop = mTempLocation[1];
                    toolbarContainer.getLocationOnScreen(mTempLocation);
                    int toolbarTop = mTempLocation[1];
                    int heightAbs = Math.abs(hHeader.itemView.getHeight()-toolbarContainer.getHeight());
                    if(heightAbs==0)
                        return;
                    float ratio = Math.min(1,(toolbarTop-headerTop)*1f/heightAbs);
                    tvTitle.setAlpha(ratio);
                    toolbarBackground.setAlpha((int) (255*ratio));
                    circleIndicator.setVisibility(ratio==1 ? View.VISIBLE:View.INVISIBLE);
                }

                if(circleIndicator.getVisibility()!=View.VISIBLE)
                    return;
                //获取range
                circleIndicator.getLocationOnScreen(mTempLocation);
                int indicatorTop = mTempLocation[1];
                final int childCount = rv.getChildCount();
                for (int i=0;i<childCount;i++){
                    View child = rv.getChildAt(i);
                    child.getLocationOnScreen(mTempLocation);
                    int childTop = mTempLocation[1]-layoutManager.getTopDecorationHeight(child);
                    int childBottom = childTop+child.getHeight()+layoutManager.getBottomDecorationHeight(child);
                    if(childTop<=indicatorTop && childBottom>=indicatorTop){
                        int childFlatPosition = rv.getChildAdapterPosition(child);
                        int childItemPosition = mScheduleAdapter.getItemPosition(childFlatPosition);
                        int range = mScheduleAdapter.getGroupPosition(childItemPosition);
                        if(range!=-1)
                            circleIndicator.checkToPosition(range);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mScheduleAdapter!=null
                && mScheduleAdapter.getAudioPlayer()!=null){
            mScheduleAdapter.getAudioPlayer().stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_share:
                ;
                break;

            case R.id.btn_more:
                if(mFromMatchedTrips){
                    finish();
                    return;
                }
                Schedule schedule = new Schedule();
                schedule.setId(mMatchedRoute.getBaseId());
                schedule.setCount(mSchedule.getCount());
                schedule.setCityTo(mSchedule.getCityTo(),mSchedule.getCityToName());
                schedule.setCityFrom(mSourceSchedule.getCityFrom(),mSourceSchedule.getCityFromName());
                schedule.setCityToImg(mSchedule.getCityToImg());
                schedule.setTravelTags(mSourceSchedule.getTravelTags());
                schedule.setTravelNeeds(mSourceSchedule.getTravelNeeds());
                MoreMatchedRoutesActivity.toMore(this,schedule);
                break;

            case R.id.btn_copy:
                //EditTopology.create()生成EditTopology
                //需要将TripPart部分 text、imgs、sound、soundLen、video、videoImg清除掉
                copySchedule();
                break;
        }
    }

    private void requestLoadData(){
        Single.create(
                new SingleOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                        //加载数据
                        GetTripDaysInfo params = new GetTripDaysInfo();
                        params.setBaseId(mMatchedRoute.getMacthBaseId());
                        params.setLoadAllData("1");
                        params.setQueryType("viewInfo");
                        HttpResult<TripDaysInfo> result = ServiceGenerator.createService(MatchedRoutePreviewActivity.this, TripService.class)
                                .getTripDaysInfo(params)
                                .blockingGet();
                        //存储至数据库
                        TripDaysInfo tripInfo = result.getData();
                        mUserInfo = UserInfo.create(tripInfo.getUserInfo());
                        mSchedule = DataManager.createSchedule(tripInfo);
                        //根据数据库数据建立topology
                        mDataManager = DataManager.create(mSchedule);
                        //加载数据库存储的相关
                        mTopology = PreviewTopology.create(mDataManager);
                        e.onSuccess(true);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        dismissProgressDialog();

                        mScheduleAdapter = new PreviewScheduleAdapter(MatchedRoutePreviewActivity.this,mTopology,mDataManager,new MatchPreviewHolderFactory());
                        circleIndicator.setCircleCount(mScheduleAdapter.getGroupCount());
                        //添加头部
                        hHeader = MatchPreviewHeaderHolder.create(rvContent,mScheduleAdapter);
                        mScheduleAdapter.addHeader(mScheduleAdapter.genHeaderFooterTypes(), hHeader);
                        rvContent.setAdapter(mScheduleAdapter);
                        rvContent.addItemDecoration(new MatchPreviewTimelineDecoration(MatchedRoutePreviewActivity.this,.5f));
                        tvTitle.setText(mScheduleAdapter.getDstName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(MatchedRoutePreviewActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void copySchedule(){
        String srcId = mSchedule.getId();
        String dstId = mMatchedRoute.getBaseId();

        List<TripPart> dstParts = new ArrayList<>();
        List<TripPart> srcParts = GreenDaoManager.getInstance()
                .getTripPartDao()
                .queryBuilder()
                .where(TripPartDao.Properties.ScheduleId.eq(srcId))
                .list();
        for (int i=0;i<srcParts.size();i++){
            TripPart part = srcParts.get(i);
            if(TripPart.STYLE_DESC.equals(part.getStyle()))
                continue;

            part = part.copy();
            part.setId(TripPart.genId());
            part.setScheduleId(dstId);
            part.setTripDesc(null);
            part.setTripImgs(null);
            part.setTripSound(null);
            part.setTripSound("0");
            part.setTripVideo(null);
            part.setTripVideoImg(null);

            dstParts.add(part);
        }
        GreenDaoManager.getInstance()
                .getTripPartDao()
                .insertOrReplaceInTx(dstParts);

        List<Spot> dstSpots = new ArrayList<>();
        List<Spot> srcSpots = GreenDaoManager.getInstance()
                .getSpotDao()
                .queryBuilder()
                .where(SpotDao.Properties.ScheduleId.eq(srcId))
                .list();
        for (int i=0;i<srcSpots.size();i++){
            Spot spot = srcSpots.get(i);
            spot = spot.copy();
            spot.setScheduleId(dstId);
            dstSpots.add(spot);
        }
        GreenDaoManager.getInstance()
                .getSpotDao()
                .insertOrReplaceInTx(dstSpots);

        List<Food> dstFoods = new ArrayList<>();
        List<Food> srcFoods = GreenDaoManager.getInstance()
                .getFoodDao()
                .queryBuilder()
                .where(FoodDao.Properties.ScheduleId.eq(srcId))
                .list();
        for (int i=0;i<srcFoods.size();i++){
            Food food = srcFoods.get(i);
            food = food.copy();
            food.setScheduleId(dstId);
            dstFoods.add(food);
        }
        GreenDaoManager.getInstance()
                .getFoodDao()
                .insertOrReplaceInTx(dstFoods);

        List<Hotel> dstHotels = new ArrayList<>();
        List<Hotel> srcHotels = GreenDaoManager.getInstance()
                .getHotelDao()
                .queryBuilder()
                .where(HotelDao.Properties.ScheduleId.eq(srcId))
                .list();
        for (int i=0;i<srcHotels.size();i++){
            Hotel hotel = srcHotels.get(i);
            hotel = hotel.copy();
            hotel.setScheduleId(dstId);
            dstHotels.add(hotel);
        }
        GreenDaoManager.getInstance()
                .getHotelDao()
                .insertOrReplaceInTx(dstHotels);

        Schedule schedule = new Schedule();
        schedule.setId(dstId);
        schedule.setCount(mSchedule.getCount());
        schedule.setStartTime(-1);
        schedule.setCityTo(mSchedule.getCityTo(),mSchedule.getCityToName());
        schedule.setCityFrom(mSchedule.getCityFrom(),mSchedule.getCityFromName());
        schedule.setTravelTags(mSchedule.getTravelTags());
        schedule.setTravelTags(mSchedule.getTravelNeeds());
        RouteEditScheduleActivity.toEdit(this,schedule);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SOURCE_SCHEDULE,mSourceSchedule);
        outState.putParcelable(MATCHED_ROUTE,mMatchedRoute);
        outState.putBoolean(FROM_MATCHED_TRIPS,mFromMatchedTrips);
    }

    public static final String FROM_MATCHED_TRIPS = "FromMatchedTrips";
    public static final String SOURCE_SCHEDULE = "Schedule";
    public static final String MATCHED_ROUTE = "MatchedRoute";

    public static void toPreview(Context context,Schedule srcSchedule,MatchedRoute matchedRoute,boolean fromMatcheddTrips){
        Intent intent = new Intent(context,MatchedRoutePreviewActivity.class);
        intent.putExtra(SOURCE_SCHEDULE,srcSchedule);
        intent.putExtra(MATCHED_ROUTE,matchedRoute);
        intent.putExtra(FROM_MATCHED_TRIPS,fromMatcheddTrips);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
