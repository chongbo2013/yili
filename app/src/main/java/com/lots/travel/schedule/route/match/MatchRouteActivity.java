package com.lots.travel.schedule.route.match;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.MatchRoute;
import com.lots.travel.network.model.result.MatchedRoute;
import com.lots.travel.network.service.TripService;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.route.RouteBottomActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class MatchRouteActivity extends RxBaseActivity {
    private Schedule mSchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_route);
        initData(getIntent(),savedInstanceState);
        initViews();
        requestMatchRoutes();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SCHEDULE,mSchedule);
    }

    private void initData(Intent data, Bundle savedInstanceState){
        mSchedule = data.getParcelableExtra(SCHEDULE);
        if(savedInstanceState!=null)
            mSchedule = savedInstanceState.getParcelable(SCHEDULE);
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void requestMatchRoutes(){
        //获取匹配的行程id
        MatchRoute matchParams = new MatchRoute();
        matchParams.setBaseId(mSchedule.getId());
        ServiceGenerator.createService(MatchRouteActivity.this,TripService.class)
                .matchTrip(matchParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<HttpResult<MatchedRoute>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResult<MatchedRoute> result) {
                        dismissProgressDialog();
                        MatchedRoute matchedRoute = result.getData();
                        if(matchedRoute==null || TextUtils.isEmpty(matchedRoute.getMacthBaseId())){
                            Toast.makeText(MatchRouteActivity.this,R.string.route_match_no,Toast.LENGTH_SHORT).show();
                            RouteBottomActivity.back(MatchRouteActivity.this,true,mSchedule,null);
                        }else
                            RouteBottomActivity.back(MatchRouteActivity.this,true,mSchedule,result.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(MatchRouteActivity.this,R.string.match_route_failed,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public static final String SCHEDULE = "Schedule";

    public static void toMatch(Context context,Schedule schedule){
        Intent intent = new Intent(context,MatchRouteActivity.class);
        intent.putExtra(SCHEDULE,schedule);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
