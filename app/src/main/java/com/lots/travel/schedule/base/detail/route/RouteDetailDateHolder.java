package com.lots.travel.schedule.base.detail.route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailDateHolder extends RouteDetailHolder {

    public static RouteDetailDateHolder create(ViewGroup parent, RouteDetailAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_detail_date,parent,false);
        return new RouteDetailDateHolder(adapter,v);
    }

    private TextView tvDay,tvDate;

    public RouteDetailDateHolder(RouteDetailAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        tvDay = (TextView) v.findViewById(R.id.tv_day);
        tvDate = (TextView) v.findViewById(R.id.tv_date);
    }

    @Override
    public void onBind() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        tvDay.setText(String.format(Locale.getDefault(),"D%d",getDay()));
        Date date = getDate();
        if(date!=null)
            tvDate.setText(sdf.format(getDate()));
    }

    @Override
    public void onRelease() {}
}
