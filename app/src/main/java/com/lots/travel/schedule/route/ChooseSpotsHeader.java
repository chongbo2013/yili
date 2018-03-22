package com.lots.travel.schedule.route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/12/28.
 */

public class ChooseSpotsHeader extends AbstractLoadAdapter.HeaderFooterHolder {

    public static ChooseSpotsHeader create(ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_choose_spots,parent,false);
        return new ChooseSpotsHeader(v);
    }

    View layoutSearchBar;
    private TextView tvTripName;

    private String tripName;

    public ChooseSpotsHeader(View v) {
        super(v);
    }

    public void setTripName(String name){
        tripName = name;
    }

    @Override
    public void onCreate(View v) {
        layoutSearchBar = v.findViewById(R.id.layout_searchbar);
        tvTripName = (TextView) v.findViewById(R.id.tv_trip_name);
    }

    @Override
    public void onBind() {
        tvTripName.setText(tripName);
    }

    @Override
    public void onDetach() {

    }
}
