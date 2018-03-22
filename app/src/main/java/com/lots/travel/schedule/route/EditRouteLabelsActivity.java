package com.lots.travel.schedule.route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.AddTrip;
import com.lots.travel.network.model.result.TravelTag;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.network.service.TripService;
import com.lots.travel.place.model.Place;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.route.match.MatchRouteActivity;
import com.lots.travel.schedule.route.widget.EditTripDayView;
import com.lots.travel.schedule.widget.ChooseRequirementView;
import com.lots.travel.schedule.widget.ChooseTagsView;
import com.lots.travel.store.db.Spot;
import com.lots.travel.util.Constant;
import com.lots.travel.util.LocateUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class EditRouteLabelsActivity extends RxBaseActivity implements View.OnClickListener {
    private EditTripDayView vEditTripDay;
    private ChooseTagsView vChooseTags;
    private ChooseRequirementView vChooseNeeds;

    private Place mPlace;
    private List<Spot> mSpots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route_labels);

        initArgs(getIntent(),savedInstanceState);
        initViews();

        requestTags();
    }

    private void initArgs(Intent data,Bundle savedInstanceState){
        mPlace = data.getParcelableExtra(PLACE);
        mSpots = data.getParcelableArrayListExtra(SPOTS);
        if(savedInstanceState!=null) {
            mPlace = savedInstanceState.getParcelable(PLACE);
            mSpots = savedInstanceState.getParcelableArrayList(SPOTS);
        }
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_complete).setOnClickListener(this);

        vEditTripDay = (EditTripDayView) findViewById(R.id.v_edit_trip_day);
        vChooseTags = (ChooseTagsView) findViewById(R.id.v_choose_tags);
        vChooseNeeds = (ChooseRequirementView) findViewById(R.id.v_choose_needs);

        vEditTripDay.setDayCount(5);

        TextView tvThemeTip = (TextView) findViewById(R.id.tv_theme_tip);
        tvThemeTip.setText(
                showTip(getString(R.string.edit_labels_theme_1),getString(R.string.edit_labels_theme_2)));

        TextView tvRequirementTip = (TextView) findViewById(R.id.tv_requirement_tip);
        tvRequirementTip.setText(
                showTip(getString(R.string.edit_labels_requirement_1),getString(R.string.edit_labels_requirement_2)));
    }

    private void requestTags(){
        ServiceGenerator.createService(this, CommonService.class)
                .getTravelTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<HttpResult<List<TravelTag>>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<List<TravelTag>> result) {
                        vChooseTags.setTags(result.getData());
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    private SpannableStringBuilder showTip(String str1,String str2){
        int end;
        int textSize,textColor;

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append(str1);
        end = strBuilder.length();
        textSize = (int) getResources().getDimension(R.dimen.head_small);
        textColor = ContextCompat.getColor(this,R.color.color_title_big);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(str2);
        textSize = (int) getResources().getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(this,R.color.color_title_big);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SPOTS, (ArrayList<? extends Parcelable>) mSpots);
        outState.putParcelable(PLACE,mPlace);
    }

    public static final String SPOTS = "Spots";
    public static final String PLACE = "Place";

    public static void toEditLabels(Context context, Place place, List<Spot> spots){
        Intent intent = new Intent(context,EditRouteLabelsActivity.class);
        intent.putExtra(PLACE,place);
        intent.putParcelableArrayListExtra(SPOTS, (ArrayList<? extends Parcelable>) spots);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_complete:
                int dayCount = vEditTripDay.getDayCount();
                if(dayCount==0){
                    Toast.makeText(this,R.string.edit_labels_daycount_0,Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder strBuilder = new StringBuilder();
                for (int i=0;i<mSpots.size();i++){
                    if(i!=0)
                        strBuilder.append(",");
                    strBuilder.append(mSpots.get(i).getViewId());
                }
                requestCreateRoute(dayCount,vChooseTags.getCheckedTagsString(),vChooseNeeds.getCheckedRequirementsString(),strBuilder.toString());
                break;
        }
    }

    private void requestCreateRoute(final int count, final String travelTags, final String travelNeeds, String spots){
        AddTrip params = new AddTrip();
        params.setStyle("trip");
        params.setCityFrom(LocateUtil.getCity());
        params.setCityTo(Integer.toString(mPlace.getViewId()));
        params.setDays(Integer.toString(count));
        params.setTravelTag(travelTags);
        params.setTravelNeed(travelNeeds);
        params.setZhaomuStatus("1");
        params.setXuanshangStatus("1");
        params.setStatus("0");
        params.setMyViewSpot(spots);
        ServiceGenerator.createService(this, TripService.class)
            .addTrip(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleAdapter<HttpResult<String>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    showProgressDialog();
                }

                @Override
                public void onSuccess(@NonNull HttpResult<String> result) {
                    dismissProgressDialog();
                    Schedule schedule = new Schedule();
                    schedule.setId(result.getData());
                    schedule.setCount(count);
                    schedule.setCityTo(mPlace.getViewId()+"",mPlace.getName());
                    schedule.setTravelTags(travelTags);
                    schedule.setTravelNeeds(travelNeeds);
                    String url = mPlace.getViewImg();
                    if(!TextUtils.isEmpty(url)) {
                        url = url.startsWith("http") ? url: Constant.PATH_OSS_IMAGE+url;
                        schedule.setCityToImg(url);
                    }
                    MatchRouteActivity.toMatch(EditRouteLabelsActivity.this,schedule);
                }

                @Override
                public void onError(Throwable e) {
                    dismissProgressDialog();
                    new DefaultExceptionHandler().handleException(EditRouteLabelsActivity.this,e);
                }
            });
    }

}
