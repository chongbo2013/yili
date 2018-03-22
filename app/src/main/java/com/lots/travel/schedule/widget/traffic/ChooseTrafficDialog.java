package com.lots.travel.schedule.widget.traffic;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.BaseDialog;
import com.lots.travel.widget.picker.core.PickerView;

/**
 * Created by lWX479187 on 2017/11/7.
 */
public class ChooseTrafficDialog extends BaseDialog implements View.OnClickListener {
    private TrafficPicker vMode;
    private TimePicker vHour,vMinute;

    private OnChooseListener onChooseListener;

    public ChooseTrafficDialog(Context context) {
        super(context, R.layout.dialog_choose_traffic);
    }

    public void setOnChooseListener(OnChooseListener listener){
        onChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Dialog dialog = getDialog();
        vMode = (TrafficPicker) dialog.findViewById(R.id.v_mode);
        vHour = (TimePicker) dialog.findViewById(R.id.v_hour);
        vMinute = (TimePicker) dialog.findViewById(R.id.v_minute);

        vMode.setOnPickListener(new PickerView.OnPickListener() {
            @Override
            public void onPick(PickerView picker, int oldPosition, int newPosition) {
                vHour.reset();
                vMinute.reset();
            }
        });

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                break;

            case R.id.btn_save:
                if(onChooseListener!=null)
                    onChooseListener.onChoose(
                            vMode.getMode(),vHour.getCurrent(),vMinute.getCurrent());
                break;
        }

        dismiss();
    }



    public interface OnChooseListener{
        void onChoose(String mode, int hour, int minute);
    }

}
