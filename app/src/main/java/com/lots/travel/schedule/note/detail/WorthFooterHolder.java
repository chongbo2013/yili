package com.lots.travel.schedule.note.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.ScheduleHolder;

/**
 * Created by nalanzi on 2018/1/17.
 */

public class WorthFooterHolder extends ScheduleHolder {

    public static WorthFooterHolder create(RecyclerView rv, ScheduleAdapter adapter){
        View v = LayoutInflater.from(rv.getContext()).inflate(R.layout.item_note_detail_worth,rv,false);
        return new WorthFooterHolder(adapter,v);
    }

    public WorthFooterHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {}

    @Override
    public void onBind() {}

    @Override
    public void onRelease() {}
}
