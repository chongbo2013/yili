package com.lots.travel.main.live;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.widget.PopupDialog;

/**
 * Created by nalanzi on 2017/12/21.
 */
//发布现场、编写游记、发起直播、取消
public class PublishLiveDialog extends PopupDialog implements View.OnClickListener {
    private OnPublishListener mOnPublishListener;

    public PublishLiveDialog(Context context,OnPublishListener listener) {
        super(context, R.layout.dialog_publish_live);
        mOnPublishListener = listener;
    }

    @Override
    public void onViewCreated() {
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_footprint).setOnClickListener(this);
        dialog.findViewById(R.id.tv_note).setOnClickListener(this);
        dialog.findViewById(R.id.tv_live).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mOnPublishListener==null){
            dismiss();
            return;
        }

        switch (v.getId()){
            case R.id.tv_footprint:
                mOnPublishListener.onPublishFootprint();
                break;

            case R.id.tv_note:
                mOnPublishListener.onCreateNote();
                break;

            case R.id.tv_live:
                mOnPublishListener.onPublishLive();
                break;
        }

        dismiss();
    }

    public interface OnPublishListener{
        void onPublishFootprint();
        void onCreateNote();
        void onPublishLive();
    }

}
