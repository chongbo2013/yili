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
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailHotelHolder extends RouteDetailHolder implements View.OnClickListener {

    public static RouteDetailHotelHolder create(ViewGroup parent, RouteDetailAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_detail_hotel,parent,false);
        return new RouteDetailHotelHolder(adapter,v);
    }

    private ImageView ivImg;
    private TextView tvName;
    private RatingBar rbScore;
    private TextView tvScoreType;
    private TextView tvTag;
    private TextView tvShortDesc;

    public RouteDetailHotelHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_hotel);

        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        rbScore = (RatingBar) v.findViewById(R.id.rb_score);
        tvScoreType = (TextView) v.findViewById(R.id.tv_score_type);
        tvTag = (TextView) v.findViewById(R.id.tv_tag);
        tvName.setOnClickListener(this);
        tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
    }

    @Override
    public void onBind() {
        Hotel hotel = getHotel();

        loadImage(hotel.getImg(),ivImg);
        tvName.setText(hotel.getName());
        rbScore.setRating(TypeUtil.str2float(hotel.getScore()));
        tvScoreType.setText(hotel.getScoreType());
        tvTag.setText(hotel.getTag());
        tvShortDesc.setText(hotel.getShortDesc());
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
