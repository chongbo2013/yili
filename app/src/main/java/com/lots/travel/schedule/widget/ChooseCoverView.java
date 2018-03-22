package com.lots.travel.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/9/23.
 */

public class ChooseCoverView extends FrameLayout {
    public static final int CHANGE = R.id.tv_cover_change;
    public static final int UPLOAD = R.id.tv_cover_upload;

    private ImageView ivCover;
    private TextView tvCoverChange,tvCoverUpload;

    private String coverPath;

    public ChooseCoverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_change_cover,this,true);

        ivCover = (ImageView) findViewById(R.id.iv_cover);
        tvCoverChange = (TextView) findViewById(R.id.tv_cover_change);
        tvCoverUpload = (TextView) findViewById(R.id.tv_cover_upload);
    }

    public void setCoverPath(String path){
        coverPath = path;

        if(coverPath==null){
            tvCoverUpload.setVisibility(View.VISIBLE);
            tvCoverChange.setVisibility(View.INVISIBLE);
            return;
        }else{
            tvCoverUpload.setVisibility(View.INVISIBLE);
            tvCoverChange.setVisibility(View.VISIBLE);
        }

        RequestOptions mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        Glide.with(getContext())
                .load(coverPath)
                .apply(mRequestOptions)
                .into(ivCover);
    }

    public String getCoverPath(){
        return coverPath;
    }

    public void setOnClickListener(View.OnClickListener listener){
        tvCoverChange.setOnClickListener(listener);
        tvCoverUpload.setOnClickListener(listener);
    }

}
