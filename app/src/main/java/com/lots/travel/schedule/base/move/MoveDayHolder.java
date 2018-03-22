package com.lots.travel.schedule.base.move;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class MoveDayHolder extends MoveScheduleHolder implements View.OnClickListener {

    public static MoveDayHolder create(ViewGroup parent, MoveScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_move_day,parent,false);
        return new MoveDayHolder(adapter,root);
    }

    private TextView tvDay;
    private TextView tvAdd;

    public MoveDayHolder(MoveScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        tvDay = (TextView) v.findViewById(R.id.tv_day);
        tvAdd = (TextView) v.findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(this);
    }

    @Override
    public void onBind() {
        tvDay.setText(String.format(Locale.getDefault(),"D%d",getDay()));
    }

    @Override
    public void onRelease() {}

    @Override
    public void onClick(View v) {
        triggerAddComponent();
    }
}
