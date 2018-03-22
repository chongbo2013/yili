package com.lots.travel.main.info.mine;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.widget.PopupDialog;

/**
 * Created by nalanzi on 2017/12/14.
 */

public class ChooseEmotionDialog extends PopupDialog implements View.OnClickListener {
    public static final int TYPE_SINGLE = 0;

    public static final int TYPE_MARRIED = 1;

    public static final int TYPE_DIVORCE = 2;

    private OnChooseListener onChooseListener;

    public ChooseEmotionDialog(Context context, OnChooseListener listener) {
        super(context, R.layout.dialog_choose_emotion);
        onChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_single).setOnClickListener(this);
        dialog.findViewById(R.id.tv_married).setOnClickListener(this);
        dialog.findViewById(R.id.tv_divorce).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int sex = TYPE_SINGLE;
        switch (v.getId()){
            case R.id.tv_single:
                sex = TYPE_SINGLE;
                break;

            case R.id.tv_married:
                sex = TYPE_MARRIED;
                break;

            case R.id.tv_divorce:
                sex = TYPE_DIVORCE;
                break;

            case R.id.tv_cancel:
                dismiss();
                return;
        }
        if(onChooseListener!=null)
            onChooseListener.onChoose(sex);
        dismiss();
    }

    public interface OnChooseListener{
        void onChoose(int emotion);
    }

}
