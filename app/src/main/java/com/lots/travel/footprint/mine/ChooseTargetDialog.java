package com.lots.travel.footprint.mine;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.widget.OnChooseListener;
import com.lots.travel.widget.PopupDialog;

public class ChooseTargetDialog extends PopupDialog implements View.OnClickListener {
    public static final int TYPE_PUBLIC = 0;
    public static final int TYPE_FRIENDS = 1;
    public static final int TYPE_ME = 2;
    public static final int TYPE_DELETE = 3;

    private OnChooseListener mOnChooseListener;

    public ChooseTargetDialog(Context context,OnChooseListener listener) {
        super(context, R.layout.dialog_choose_target);
        mOnChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        setOnClickListener(this);
    }

    private void setOnClickListener(View.OnClickListener listener){
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_public).setOnClickListener(listener);
        dialog.findViewById(R.id.tv_friends).setOnClickListener(listener);
        dialog.findViewById(R.id.tv_me).setOnClickListener(listener);
        dialog.findViewById(R.id.tv_delete).setOnClickListener(listener);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_public:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_PUBLIC);
                break;

            case R.id.tv_friends:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_FRIENDS);
                break;

            case R.id.tv_me:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_ME);
                break;

            case R.id.tv_delete:
                if(mOnChooseListener!=null)
                    mOnChooseListener.onChoose(TYPE_DELETE);
                break;
        }
        dismiss();
    }
}
