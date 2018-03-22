package com.lots.travel.schedule.route;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.place.ChoosePlaceActivity;
import com.lots.travel.place.model.Place;
import com.lots.travel.store.db.Spot;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/9/27.
 */

public class RouteChoosePlaceActivity extends ChoosePlaceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConfirmButtonText(R.string.place_dst_choose);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteBottomActivity.back(RouteChoosePlaceActivity.this,false,null,null);
            }
        });
    }

    @Override
    public void onChooseChanged(boolean checked) {
        setConfirmButton(checked);
        setConfirmButtonText(checked ?
                R.string.route_create_create:R.string.place_dst_choose);
    }

    @Override
    public void onConfirm(Place place) {
        ChooseSpotsActivity.toChoose(this,place);
    }

    @Override
    public void onBackPressed() {
        RouteBottomActivity.back(RouteChoosePlaceActivity.this,false,null,null);
    }
}
