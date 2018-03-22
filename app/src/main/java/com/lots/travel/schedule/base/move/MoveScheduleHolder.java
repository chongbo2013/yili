package com.lots.travel.schedule.base.move;

import android.view.View;

import com.lots.travel.schedule.base.ScheduleHolder;

/**
 * Created by nalanzi on 2017/12/26.
 */

public abstract class MoveScheduleHolder extends ScheduleHolder {

    public MoveScheduleHolder(MoveScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    public void triggerAddComponent(){
        OnScheduleListener listener = ((MoveScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onAdd(getFlatPosition());
    }

    public void triggerDeleteComponent(){
        OnScheduleListener listener = ((MoveScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onDelete(getFlatPosition());
    }

}
