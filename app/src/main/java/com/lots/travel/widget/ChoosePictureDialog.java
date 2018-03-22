package com.lots.travel.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;

public class ChoosePictureDialog extends PopupDialog implements View.OnClickListener {
    private OnChooseListener onChooseListener;

    public ChoosePictureDialog(Context context,OnChooseListener listener) {
        super(context, R.layout.dialog_choose_picture);
        onChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_album).setOnClickListener(this);
        dialog.findViewById(R.id.tv_photo).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_album:
                if(onChooseListener!=null){
                    onChooseListener.onTakeAlbum();
                }
                break;

            case R.id.tv_photo:
                if(onChooseListener!=null){
                    onChooseListener.onTakePhoto();
                }
                break;

            case R.id.tv_cancel:
                break;
        }

        dismiss();
    }

    public interface OnChooseListener{
        void onTakePhoto();
        void onTakeAlbum();
    }

}
