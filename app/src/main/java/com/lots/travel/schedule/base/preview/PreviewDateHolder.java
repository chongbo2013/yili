package com.lots.travel.schedule.base.preview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class PreviewDateHolder extends PreviewScheduleHolder {

    public static PreviewDateHolder create(ViewGroup parent, PreviewScheduleAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_date,parent,false);
        return new PreviewDateHolder(adapter,v);
    }

    private TextView tvDay,tvDate;

    public PreviewDateHolder(PreviewScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        tvDay = (TextView) v.findViewById(R.id.tv_day);
        tvDate = (TextView) v.findViewById(R.id.tv_date);
    }

    @Override
    public void onBind() {
        super.onBind();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        tvDay.setText(String.format(Locale.getDefault(),"D%d",getDay()));
        Date date = getDate();
        if(date!=null)
            tvDate.setText(sdf.format(getDate()));
    }

    @Override
    public void onRelease() {}
}
