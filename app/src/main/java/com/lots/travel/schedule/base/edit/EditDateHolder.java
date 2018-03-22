package com.lots.travel.schedule.base.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class EditDateHolder extends EditScheduleHolder {

    public static EditDateHolder create(ViewGroup parent, EditScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_date,parent,false);
        return new EditDateHolder(adapter,root);
    }

    private TextView tvDay,tvDate;

    public EditDateHolder(EditScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        tvDay = (TextView) v.findViewById(R.id.tv_day);
        tvDate = (TextView) v.findViewById(R.id.tv_date);
    }

    @Override
    public void onBind() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        int day = getDay();
        tvDay.setText(String.format(Locale.getDefault(),"D%d",day));
        Date date = getDate();
        if(date!=null)
            tvDate.setText(sdf.format(getDate()));
    }

    @Override
    public void onRelease() {}
}
