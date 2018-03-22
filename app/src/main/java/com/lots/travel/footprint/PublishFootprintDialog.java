package com.lots.travel.footprint;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.widget.PopupDialog;

public class PublishFootprintDialog extends PopupDialog implements View.OnClickListener {
    private OnChooseListener mOnChooseListener;

    public PublishFootprintDialog(Context context, OnChooseListener listener) {
        super(context, R.layout.dialog_publish_footprint);
        mOnChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_video).setOnClickListener(this);
        dialog.findViewById(R.id.tv_album).setOnClickListener(this);
        dialog.findViewById(R.id.tv_photo).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mOnChooseListener==null) {
            dismiss();
            return;
        }

        switch (v.getId()){
            case R.id.tv_video:
                mOnChooseListener.onChooseVideo();
                break;

            case R.id.tv_album:
                mOnChooseListener.onChooseAlbum();
                break;

            case R.id.tv_photo:
                mOnChooseListener.onChoosePhoto();
                break;

            case R.id.tv_cancel:
                break;
        }

        dismiss();
    }

    public interface OnChooseListener{
        void onChooseVideo();
        void onChooseAlbum();
        void onChoosePhoto();
    }

}
