package com.lots.travel.schedule.route.move;

import android.view.ViewGroup;

import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.move.MoveComponentHolder;
import com.lots.travel.schedule.base.move.MoveDayHolder;
import com.lots.travel.schedule.base.move.MoveScheduleAdapter;
import com.lots.travel.schedule.base.move.MoveTrafficHolder;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class RouteMoveHolderFactory implements HolderFactory<MoveScheduleAdapter> {

    @Override
    public ScheduleHolder getHolder(MoveScheduleAdapter adapter, ViewGroup parent, int childType) {
        switch (childType){
            case Topology.TYPE_DAY:
                return MoveDayHolder.create(parent,adapter);

            case Topology.TYPE_SPOT:
            case Topology.TYPE_HOTEL:
            case Topology.TYPE_FOOD:
                return MoveComponentHolder.create(parent,adapter);

            case Topology.TYPE_TRAFFIC:
                return MoveTrafficHolder.create(parent,adapter);
        }
        return null;
    }

}
