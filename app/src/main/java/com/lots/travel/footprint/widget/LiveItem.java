package com.lots.travel.footprint.widget;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.model.VideoData;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/19.
 */

public class LiveItem extends BaseItem {
    private ImageView ivCover,ivTag;
    private TextView tvLocation,tvVisited;

    public LiveItem(BaseFootprintAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onCreate(final BaseHolder holder, View v) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flatPos = holder.getAdapterPosition();
                int itemPos = adapter.getItemPosition(flatPos);
                Footprint data = adapter.getItem(itemPos);

                if (data == null)
                    return;

//                adapter.triggerPlayVideo(data.getTripVideo());
            }
        };

        ivCover = (ImageView) v.findViewById(R.id.iv_cover);
        ivCover.setOnClickListener(listener);
        ivTag = (ImageView) v.findViewById(R.id.iv_tag);
        tvLocation = (TextView) v.findViewById(R.id.tv_location);
        tvVisited = (TextView) v.findViewById(R.id.tv_visited);
    }

    @Override
    public void onBind(BaseHolder holder,Footprint data) {
        adapter.loadImage(data.getTripVideoImg(),ivCover);
        tvLocation.setText(data.getGpsAddress());
        String json = data.getJson();
        if(TextUtils.isEmpty(json)){
            tvVisited.setText("");
        }
        VideoData jsonData = new Gson().fromJson(json,VideoData.class);
        tvVisited.setText(String.format(Locale.getDefault(),"%s看过",jsonData.getOnLiveNum()));
    }
}
