package com.lots.travel.footprint.widget;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.DestinationData;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/11/19.
 */

public class DestinationItem extends BaseItem {
    private ImageView ivImg;
    private TextView tvName;
    private RatingBar rbScore;
    private TextView tvPercentPeople;
    private TextView tvShortDesc;
    private TextView tvDetail;


    public DestinationItem(BaseFootprintAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onCreate(BaseHolder holder, View v) {
        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        rbScore = (RatingBar) v.findViewById(R.id.rb_score);
        tvPercentPeople = (TextView) v.findViewById(R.id.tv_percent_people);
        tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
        tvDetail = (TextView) v.findViewById(R.id.tv_detail);
    }

    @Override
    public void onBind(BaseHolder holder,Footprint t) {
        String[] imgs = TypeUtil.str2arrays(t.getTripImgs());
        if(imgs!=null && imgs.length>0)
            adapter.loadImage(imgs[0],ivImg);

        tvName.setText(t.getTripTitle());

        String str = t.getJson();
        if(TextUtils.isEmpty(str))
            return;
        DestinationData data = new Gson().fromJson(str,DestinationData.class);

        str = data.getScore();
        if(TextUtils.isEmpty(str)){
            rbScore.setVisibility(View.INVISIBLE);
        }else{
            rbScore.setVisibility(View.VISIBLE);
            rbScore.setRating(TypeUtil.str2float(str));
        }

        tvPercentPeople.setText(data.getPercentPeople());
        tvShortDesc.setText(data.getShortDesc());
    }
}
