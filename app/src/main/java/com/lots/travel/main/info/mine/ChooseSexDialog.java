package com.lots.travel.main.info.mine;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.widget.PopupDialog;

/**
 * Created by nalanzi on 2017/12/14.
 */

public class ChooseSexDialog extends PopupDialog implements View.OnClickListener {
    public static final int TYPE_SECRET = 0;

    public static final int TYPE_MALE = 1;

    public static final int TYPE_FEMALE = 2;

    private OnChooseListener onChooseListener;

    public ChooseSexDialog(Context context,OnChooseListener listener) {
        super(context, R.layout.dialog_choose_sex);
        onChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_secret).setOnClickListener(this);
        dialog.findViewById(R.id.tv_male).setOnClickListener(this);
        dialog.findViewById(R.id.tv_female).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int sex = TYPE_SECRET;
        switch (v.getId()){
            case R.id.tv_secret:
                sex = TYPE_SECRET;
                break;

            case R.id.tv_male:
                sex = TYPE_MALE;
                break;

            case R.id.tv_female:
                sex = TYPE_FEMALE;
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
        void onChoose(int sex);
    }

}
