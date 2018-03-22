package com.lots.travel.schedule.note.detail;

import android.view.ViewGroup;

import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.detail.note.NoteDetailDateHolder;
import com.lots.travel.schedule.base.detail.note.NoteDetailDescHolder;
import com.lots.travel.schedule.base.detail.note.NoteDetailAdapter;
import com.lots.travel.schedule.base.detail.note.NoteDetailFoodHolder;
import com.lots.travel.schedule.base.detail.note.NoteDetailHotelHolder;
import com.lots.travel.schedule.base.detail.note.NoteDetailSpotHolder;
import com.lots.travel.schedule.base.detail.note.NoteDetailStayHolder;
import com.lots.travel.schedule.base.detail.note.NoteDetailTrafficHolder;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class NoteDetailHolderFactory implements HolderFactory<NoteDetailAdapter> {

    @Override
    public ScheduleHolder getHolder(NoteDetailAdapter adapter, ViewGroup parent, int childType) {
        switch (childType){
            case Topology.TYPE_DATE:
                return NoteDetailDateHolder.create(parent,adapter);

            case Topology.TYPE_DESC:
                return NoteDetailDescHolder.create(parent, adapter);

            case Topology.TYPE_SPOT:
                return NoteDetailSpotHolder.create(parent, adapter);

            case Topology.TYPE_HOTEL:
                return NoteDetailHotelHolder.create(parent, adapter);

            case Topology.TYPE_FOOD:
                return NoteDetailFoodHolder.create(parent, adapter);

            case Topology.TYPE_STAY:
                return NoteDetailStayHolder.create(parent, adapter);

            case Topology.TYPE_TRAFFIC:
                return NoteDetailTrafficHolder.create(parent, adapter);
        }
        return null;
    }

}
