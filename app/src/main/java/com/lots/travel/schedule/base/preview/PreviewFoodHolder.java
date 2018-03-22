package com.lots.travel.schedule.base.preview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.schedule.widget.TripPartDisplay;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class PreviewFoodHolder extends PreviewScheduleHolder implements View.OnClickListener {
    public static PreviewFoodHolder create(ViewGroup parent, PreviewScheduleAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_food,parent,false);
        return new PreviewFoodHolder(adapter,v);
    }

    private ImageView ivImg;
    private TextView tvName;
    private RatingBar rbScore;
    private TextView tvFoodType;
    private TextView tvArea;

    private View dividerContent;
    private TripPartDisplay tripDisplay;

    public PreviewFoodHolder(ScheduleAdapter adapter, View v) {
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

        dividerContent = v.findViewById(R.id.divider_content);
        tripDisplay = (TripPartDisplay) v.findViewById(R.id.trip_display);
        tripDisplay.setImageLoader(mAdapter);
        tripDisplay.setOnControlListener(this);
    }

    @Override
    public void onBind() {
        super.onBind();
        TripPart tripPart = getContent();
        Food food = getFood();

        loadImage(food.getImg(),ivImg);
        tvName.setText(food.getName());
        rbScore.setRating(food.getScore());
        tvFoodType.setText(food.getFoodType());
        tvArea.setText(food.getArea());

        tripDisplay.setVideoImages(tripPart.getTripVideoImg(),tripPart.getTripImgs());
        tripDisplay.setVoice(((PreviewScheduleAdapter)mAdapter).getPortraitUrl(), TypeUtil.str2int(tripPart.getTripSoundLen()));

        RunningData running = (RunningData) getExtraData();
        tripDisplay.setText(running.isExpanded(),tripPart.getTripDesc());

        boolean contentVisible = tripPart.isContentEmpty();
        dividerContent.setVisibility(contentVisible ? View.GONE:View.VISIBLE);
        tripDisplay.setVisibility(contentVisible ? View.GONE:View.VISIBLE);
    }

    @Override
    public void onRelease() {
        tripDisplay.reset();
    }

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
