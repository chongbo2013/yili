package com.lots.travel.schedule.widget.traffic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.lots.travel.schedule.model.TrafficMode;
import com.lots.travel.widget.picker.core.PickerAdapter;
import com.lots.travel.widget.picker.core.PickerView;

/**
 * Created by lWX479187 on 2017/11/7.
 */
public class TrafficPicker extends PickerView {
    private TrafficMode[] modes;

    public TrafficPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        modes = TrafficMode.values();
        setAdapter(new TrafficAdapter(),0);
    }

    public String getMode(){
        return modes[getValueIndex()].getName();
    }

    private class TrafficAdapter extends PickerAdapter{

        @Override
        public String getText(int pos) {
            return modes[pos].getName();
        }

        @Override
        public Drawable getIcon(int pos) {
            return ContextCompat.getDrawable(getContext(),modes[pos].getIcon());
        }

        @Override
        public int getCount() {
            return modes.length;
        }
    }

}
