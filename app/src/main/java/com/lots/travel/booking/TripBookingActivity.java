package com.lots.travel.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.place.model.Place;
import com.lots.travel.place.widget.PlaceProfileView;
import com.lots.travel.widget.TripServiceView;

/**
 * Created by nalanzi on 2017/9/26.
 */
//未做数据保存，后续优化
public class TripBookingActivity extends RxBaseActivity implements View.OnClickListener {
    private PlaceProfileView vPlace;
    private TripServiceView vTripService;

    private Place mPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent(),savedInstanceState);

        setContentView(R.layout.activity_trip_booking);
        vPlace = (PlaceProfileView) findViewById(R.id.v_place);
        findViewById(R.id.tv_more).setOnClickListener(this);
        vTripService = (TripServiceView) findViewById(R.id.v_trip_service);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PLACE,mPlace);
    }

    private void parseIntent(Intent intent,Bundle savedInstanceState){
        mPlace = intent.getParcelableExtra(EXTRA_PLACE);
        if(savedInstanceState!=null)
            mPlace = savedInstanceState.getParcelable(EXTRA_PLACE);
    }

    private static final String EXTRA_PLACE = "place";
    public static void toBooking(Context context,Place place){
        Intent intent = new Intent(context,TripBookingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_PLACE,place);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_more:
                vTripService.turnExpandState();
                break;
        }
    }
}
