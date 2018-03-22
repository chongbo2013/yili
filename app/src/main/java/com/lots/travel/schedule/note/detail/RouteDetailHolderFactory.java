package com.lots.travel.schedule.note.detail;

import android.view.ViewGroup;

import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.detail.route.RouteDetailDateHolder;
import com.lots.travel.schedule.base.detail.route.RouteDetailAdapter;
import com.lots.travel.schedule.base.detail.route.RouteDetailFoodHolder;
import com.lots.travel.schedule.base.detail.route.RouteDetailHotelHolder;
import com.lots.travel.schedule.base.detail.route.RouteDetailSpotHolder;
import com.lots.travel.schedule.base.detail.route.RouteDetailStayHolder;
import com.lots.travel.schedule.base.detail.route.RouteDetailTrafficHolder;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailHolderFactory implements HolderFactory<RouteDetailAdapter> {

    @Override
    public ScheduleHolder getHolder(RouteDetailAdapter adapter, ViewGroup parent, int childType) {
        switch (childType){
            case Topology.TYPE_DATE:
                return RouteDetailDateHolder.create(parent,adapter);

            case Topology.TYPE_SPOT:
                return RouteDetailSpotHolder.create(parent, adapter);

            case Topology.TYPE_HOTEL:
                return RouteDetailHotelHolder.create(parent, adapter);

            case Topology.TYPE_FOOD:
                return RouteDetailFoodHolder.create(parent, adapter);

            case Topology.TYPE_STAY:
                return RouteDetailStayHolder.create(parent, adapter);

            case Topology.TYPE_TRAFFIC:
                return RouteDetailTrafficHolder.create(parent, adapter);
        }
        return null;
    }

}
