package com.lots.travel.schedule.route.match;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetMatchTrips;
import com.lots.travel.network.model.result.MatchedRoute;
import com.lots.travel.network.model.result.MoreRoute;
import com.lots.travel.network.service.TripService;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.route.edit.RouteEditScheduleActivity;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.store.db.TripPartDao;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.VerticalLinearSpaceItemDecoration;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2018/1/2.
 */

public class MoreMatchedRoutesActivity extends RxBaseActivity implements View.OnClickListener{
    public static final String SCHEDULE = "Schedule";

    public static void toMore(Context context, Schedule schedule){
        Intent intent = new Intent(context,MoreMatchedRoutesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(SCHEDULE,schedule);
        context.startActivity(intent);
    }

    private Schedule mSchedule;
    private MoreRouteFragment mMoreRouteFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_matched_routes);

        initData(getIntent(),savedInstanceState);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoreRouteFragment.setUserVisibleHint(true);
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
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_custom).setOnClickListener(this);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(String.format(Locale.getDefault(),getString(R.string.route_match_more),mSchedule.getCityToName()));

        mMoreRouteFragment = new MoreRouteFragment();
        Bundle args = new Bundle();
        args.putString(SCHEDULE_ID,mSchedule.getId());
        mMoreRouteFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container,mMoreRouteFragment)
                .commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_custom:
                List<TripPart> tripParts = GreenDaoManager.getInstance()
                        .getTripPartDao()
                        .queryBuilder()
                        .where(TripPartDao.Properties.ScheduleId.eq(mSchedule.getId()))
                        .list();
                GreenDaoManager.getInstance()
                        .getTripPartDao()
                        .deleteInTx(tripParts);
                RouteEditScheduleActivity.toEdit(this,mSchedule);
                break;
        }
    }

    public static final String SCHEDULE_ID = "ScheduleId";

    public static class MoreRouteFragment extends PagedItemFragment<MoreRoute>{
        private MoreMatchedRoutesActivity mContext;
        private MoreRoutesAdapter mMoreRouteAdapter;
        private String mScheduleId;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext = (MoreMatchedRoutesActivity) context;
            Bundle args = getArguments();
            mScheduleId = args.getString(SCHEDULE_ID);
        }

        @Override
        public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
            super.onConfigureDisplay(configureAdapter);
            configureAdapter.addItemDecoration(new VerticalLinearSpaceItemDecoration(DensityUtil.dp2px(getContext(),10)));
        }

        @Override
        public PageIterator.PageRequest<MoreRoute> createPageRequest() {
            return new PageIterator.PageRequest<MoreRoute>() {
                @Override
                public Single<HttpResult<List<MoreRoute>>> execute(int page) {
                    GetMatchTrips params = new GetMatchTrips();
                    params.setBaseId(mScheduleId);
                    return ServiceGenerator.createService(getContext(), TripService.class)
                            .getMatchTrips(params);
                }
            };
        }

        @Override
        public AbstractLoadAdapter<MoreRoute> createAdapter(RecyclerView rv) {
            mMoreRouteAdapter = new MoreRoutesAdapter(getContext(),rv);
            mMoreRouteAdapter.setOnItemClickListener(new MoreRoutesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MoreRoute item) {
                    MatchedRoute matchedRoute = new MatchedRoute();
                    matchedRoute.setBaseId(item.getBaseId());
                    matchedRoute.setMacthBaseId(item.getMacthBaseId());
                    MatchedRoutePreviewActivity.toPreview(getContext(),mContext.mSchedule,matchedRoute,true);
                }
            });
            return mMoreRouteAdapter;
        }
    }

}
