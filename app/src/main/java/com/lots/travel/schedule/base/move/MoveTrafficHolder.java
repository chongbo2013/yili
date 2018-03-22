package com.lots.travel.schedule.base.move;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class MoveTrafficHolder extends MoveScheduleHolder {

    public static MoveTrafficHolder create(ViewGroup parent, MoveScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_move_traffic,parent,false);
        return new MoveTrafficHolder(adapter,root);
    }

    public MoveTrafficHolder(MoveScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {}

    @Override
    public void onBind() {}

    @Override
    public void onRelease() {}
}
