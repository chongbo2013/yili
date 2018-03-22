package com.lots.travel.schedule.widget.traffic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.lots.travel.R;
import com.lots.travel.widget.picker.core.PickerAdapter;
import com.lots.travel.widget.picker.core.PickerView;

import java.util.Locale;

/**
 * Created by lWX479187 on 2017/11/7.
 */
public class TimePicker extends PickerView {
    private int mCount;

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePicker);
        mCount = a.getInteger(R.styleable.TimePicker_timeCount,24);
        a.recycle();

        setAdapter(new TimeAdapter(),0);
    }

    public int getCurrent(){
        return getValueIndex();
    }

    public void reset(){
        setValueIndex(0);
    }

    class TimeAdapter extends PickerAdapter{

        @Override
        public String getText(int pos) {
            return String.format(Locale.getDefault(),"%02d",pos);
        }

        @Override
        public Drawable getIcon(int pos) {
            return null;
        }

        @Override
        public int getCount() {
            return mCount;
        }
    }

}
