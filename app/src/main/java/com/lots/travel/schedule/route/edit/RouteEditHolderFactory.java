package com.lots.travel.schedule.route.edit;

import android.view.ViewGroup;

import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.edit.EditAddHolder;
import com.lots.travel.schedule.base.edit.EditDateHolder;
import com.lots.travel.schedule.base.edit.EditDescHolder;
import com.lots.travel.schedule.base.edit.EditFoodHolder;
import com.lots.travel.schedule.base.edit.EditHotelHolder;
import com.lots.travel.schedule.base.edit.EditScheduleAdapter;
import com.lots.travel.schedule.base.edit.EditSpotHolder;
import com.lots.travel.schedule.base.edit.EditStayHolder;
import com.lots.travel.schedule.base.edit.EditTrafficHolder;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class RouteEditHolderFactory implements HolderFactory<EditScheduleAdapter> {

    @Override
    public ScheduleHolder getHolder(EditScheduleAdapter adapter, ViewGroup parent, int childType) {
        switch (childType){
            case Topology.TYPE_DATE:
                return EditDateHolder.create(parent,adapter);

            case Topology.TYPE_DESC:
                return EditDescHolder.create(parent,adapter);

            case Topology.TYPE_SPOT:
                return EditSpotHolder.create(parent,adapter);

            case Topology.TYPE_HOTEL:
                return EditHotelHolder.create(parent,adapter);

            case Topology.TYPE_FOOD:
                return EditFoodHolder.create(parent, adapter);

            case Topology.TYPE_STAY:
                return EditStayHolder.create(parent, adapter);

            case Topology.TYPE_TRAFFIC:
                return EditTrafficHolder.create(parent, adapter);

            case Topology.TYPE_ADD:
                return EditAddHolder.create(parent,adapter);
        }
        return null;
    }

}
