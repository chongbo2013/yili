package com.lots.travel.schedule.base.detail.route;

import android.app.Activity;

import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;

/**
 * Created by nalanzi on 2018/1/15.
 */

public class RouteDetailAdapter extends ScheduleAdapter {

    public RouteDetailAdapter(Activity context, Topology topology, DataManager dataManager, HolderFactory holderFactory) {
        super(context, topology, dataManager, holderFactory);
    }

}
