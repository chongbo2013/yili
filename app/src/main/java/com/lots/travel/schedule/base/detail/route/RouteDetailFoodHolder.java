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
import com.lots.travel.store.db.Food;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailFoodHolder extends RouteDetailHolder implements View.OnClickListener {
    public static RouteDetailFoodHolder create(ViewGroup parent, RouteDetailAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_detail_food,parent,false);
        return new RouteDetailFoodHolder(adapter,v);
    }

    private ImageView ivImg;
    private TextView tvName;
    private RatingBar rbScore;
    private TextView tvFoodType;
    private TextView tvArea;
    private TextView tvShortDesc;

    public RouteDetailFoodHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_food);

        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        rbScore = (RatingBar) v.findViewById(R.id.rb_score);
        tvFoodType = (TextView) v.findViewById(R.id.tv_food_type);
        tvArea = (TextView) v.findViewById(R.id.tv_area);
        tvName.setOnClickListener(this);
        tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
    }

    @Override
    public void onBind() {
        Food food = getFood();

        loadImage(food.getImg(),ivImg);
        tvName.setText(food.getName());
        rbScore.setRating(food.getScore());
        tvFoodType.setText(food.getFoodType());
        tvArea.setText(food.getArea());
        tvShortDesc.setText(food.getShortDesc());
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
