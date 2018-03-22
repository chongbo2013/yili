package com.lots.travel.schedule.base.detail.route;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.store.db.Spot;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailSpotHolder extends RouteDetailHolder implements View.OnClickListener {
    public static RouteDetailSpotHolder create(ViewGroup parent, RouteDetailAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_detail_spot,parent,false);
        return new RouteDetailSpotHolder(adapter,v);
    }

    private ImageView ivImg;
    private TextView tvName;
    private RatingBar rbScore;
    private TextView tvPercentPeople;
    private TextView tvShortDesc;

    public RouteDetailSpotHolder(RouteDetailAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_spot);

        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        rbScore = (RatingBar) v.findViewById(R.id.rb_score);
        tvPercentPeople = (TextView) v.findViewById(R.id.tv_percent_people);
        tvName.setOnClickListener(this);

        tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
    }

    @Override
    public void onBind() {
        Spot spot = getSpot();

        loadImage(spot.getViewImg(),ivImg);
        tvName.setText(spot.getSpotName());
        tvName.setTag(spot.getViewurl());
        rbScore.setRating(TypeUtil.str2float(spot.getScore()));
        tvPercentPeople.setText(spot.getPercentPeople());
        tvShortDesc.setText(spot.getShortDesc());
    }

    @Override
    public void onRelease() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_name:
                String url = (String) v.getTag();
                if(!TextUtils.isEmpty(url))
                    WebViewActivity.toWeb(mAdapter.getContext(),null,url,false);
                break;
        }
    }
}
