package com.lots.travel.main.info.mine;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.widget.PopupDialog;
import com.lots.travel.widget.picker.core.PickerAdapter;
import com.lots.travel.widget.picker.core.PickerView;

import java.util.Calendar;

/**
 * 输入输出month都是从0开始
 */

public class ChooseBirthDialog extends PopupDialog implements View.OnClickListener {
    private PickerView vYear,vMonth,vDay;

    private OnChooseListener onChooseListener;

    public ChooseBirthDialog(Context context,OnChooseListener listener) {
        super(context, R.layout.dialog_choose_birth);
        onChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.tv_confirm).setOnClickListener(this);

        vYear = (PickerView) dialog.findViewById(R.id.v_year);
        vMonth = (PickerView) dialog.findViewById(R.id.v_month);
        vDay = (PickerView) dialog.findViewById(R.id.v_day);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        vYear.setAdapter(new TextAdapter(1900,year),0);

        vMonth.setAdapter(new TextAdapter(1,12),0);

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        vDay.setAdapter(new TextAdapter(1,maxDay),0);

        PickerView.OnPickListener listener = new PickerView.OnPickListener() {
            @Override
            public void onPick(PickerView picker, int oldPosition, int newPosition) {
                TextAdapter adapter = (TextAdapter) vYear.getAdapter();
                int year = adapter.minValue+vYear.getValueIndex();

                adapter = (TextAdapter) vMonth.getAdapter();
                int month = adapter.minValue+vMonth.getValueIndex()-1;

                adapter = (TextAdapter) vDay.getAdapter();
                int day = adapter.minValue+vDay.getValueIndex();

                updateDay(year,month,day);
            }
        };
        vYear.setOnPickListener(listener);
        vMonth.setOnPickListener(listener);
    }

    public void show(int year,int month,int day){
        TextAdapter adapter = (TextAdapter) vYear.getAdapter();
        year = Math.max(adapter.minValue,year);
        year = Math.min(year,adapter.maxValue);
        vYear.setValueIndex(year-adapter.minValue);

        adapter = (TextAdapter) vMonth.getAdapter();
        ++month;
        month = Math.max(adapter.minValue,month);
        month = Math.min(month,adapter.maxValue);
        vMonth.setValueIndex(month-adapter.minValue);

        adapter = (TextAdapter) vDay.getAdapter();
        day = Math.max(adapter.minValue,day);
        day = Math.min(day,adapter.maxValue);
        vDay.setValueIndex(day-adapter.minValue);
        show();
    }

    private void updateDay(int year,int month,int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        TextAdapter adapter = (TextAdapter) vDay.getAdapter();
        adapter.maxValue = maxDay;
        day = Math.max(adapter.minValue,day);
        day = Math.min(day,adapter.maxValue);
        adapter.notifyDataSetChanged();
        vDay.setValueIndex(day-adapter.minValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                if(onChooseListener!=null) {
                    int year,month,day;
                    TextAdapter adapter = (TextAdapter) vYear.getAdapter();
                    year = vYear.getValueIndex()+adapter.minValue;

                    adapter = (TextAdapter) vMonth.getAdapter();
                    month = vMonth.getValueIndex()+adapter.minValue;

                    adapter = (TextAdapter) vDay.getAdapter();
                    day = vDay.getValueIndex()+adapter.minValue;

                    onChooseListener.onChoose(year,month-1,day);
                }
                break;
        }
        dismiss();
    }

    private class TextAdapter extends PickerAdapter{
        private int minValue,maxValue;

        TextAdapter(int min,int max){
            minValue = min;
            maxValue = max;
        }

        @Override
        public String getText(int pos) {
            return Integer.toString(minValue+pos);
        }

        @Override
        public Drawable getIcon(int pos) {
            return null;
        }

        @Override
        public int getCount() {
            return maxValue-minValue+1;
        }
    }

    public interface OnChooseListener{
        void onChoose(int year,int month,int day);
    }

}
