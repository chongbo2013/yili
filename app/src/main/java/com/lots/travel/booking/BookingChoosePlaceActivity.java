package com.lots.travel.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lots.travel.R;
import com.lots.travel.place.ChoosePlaceActivity;
import com.lots.travel.place.model.Place;
import com.lots.travel.store.db.Spot;

/**
 * Created by nalanzi on 2017/9/27.
 */

public class BookingChoosePlaceActivity extends ChoosePlaceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConfirmButtonText(R.string.place_dst_choose);
    }

    @Override
    public void onChooseChanged(boolean checked) {
        setConfirmButton(checked);
        setConfirmButtonText(checked ?
                R.string.booking:R.string.place_dst_choose);
    }

    @Override
    public void onConfirm(Place place) {
        TripBookingActivity.toBooking(this,place);
    }

}
