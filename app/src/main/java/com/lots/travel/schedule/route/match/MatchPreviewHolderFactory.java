package com.lots.travel.schedule.route.match;

import android.view.ViewGroup;

import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.preview.PreviewDateHolder;
import com.lots.travel.schedule.base.preview.PreviewDescHolder;
import com.lots.travel.schedule.base.preview.PreviewFoodHolder;
import com.lots.travel.schedule.base.preview.PreviewHotelHolder;
import com.lots.travel.schedule.base.preview.PreviewScheduleAdapter;
import com.lots.travel.schedule.base.preview.PreviewSpotHolder;
import com.lots.travel.schedule.base.preview.PreviewStayHolder;
import com.lots.travel.schedule.base.preview.PreviewTrafficHolder;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class MatchPreviewHolderFactory implements HolderFactory<PreviewScheduleAdapter> {

    @Override
    public ScheduleHolder getHolder(PreviewScheduleAdapter adapter, ViewGroup parent, int childType) {
        switch (childType){
            case Topology.TYPE_DATE:
                return PreviewDateHolder.create(parent,adapter);

            case Topology.TYPE_DESC:
                return PreviewDescHolder.create(parent, adapter);

            case Topology.TYPE_SPOT:
                return PreviewSpotHolder.create(parent, adapter);

            case Topology.TYPE_HOTEL:
                return PreviewHotelHolder.create(parent, adapter);

            case Topology.TYPE_FOOD:
                return PreviewFoodHolder.create(parent, adapter);

            case Topology.TYPE_STAY:
                return PreviewStayHolder.create(parent, adapter);

            case Topology.TYPE_TRAFFIC:
                return PreviewTrafficHolder.create(parent, adapter);
        }
        return null;
    }

}
