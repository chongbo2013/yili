package com.lots.travel.booking.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lots.travel.R;
import com.lots.travel.widget.picker.tools.NumberPicker;

import java.util.Calendar;

public class TripDatePicker extends LinearLayout{
    private NumberPicker yearPicker,monthPicker;

    public TripDatePicker(Context context) {
        this(context,null);
    }

    public TripDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_trip_date_picker,this,true);
        yearPicker = (NumberPicker) findViewById(R.id.year_picker);
        monthPicker = (NumberPicker) findViewById(R.id.month_picker);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        yearPicker.setRange(year,year,year+100);

        int month = cal.get(Calendar.MONTH)+1;
        monthPicker.setRange(month,1,12);
    }

    public int getYear(){
        return yearPicker.getValue();
    }

    public int getMonth(){
        return monthPicker.getValue()-1;
    }

}
