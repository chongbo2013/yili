package com.lots.travel.schedule.base.move;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class MoveComponentHolder extends MoveScheduleHolder implements View.OnClickListener {

    public static MoveComponentHolder create(ViewGroup parent, MoveScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_move_component,parent,false);
        return new MoveComponentHolder(adapter,root);
    }

    private ImageView ivPortrait;
    private TextView tvName;
    private RatingBar ratingBar;

    public MoveComponentHolder(MoveScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);

        v.findViewById(R.id.btn_delete).setOnClickListener(this);
    }

    @Override
    public void onBind() {
        String url = "";
        float score = 0f;
        String name = "";

        switch (getItemViewType()){
            case Topology.TYPE_SPOT: {
                Spot spot = getSpot();
                if(spot!=null){
                    url = spot.getViewImg();
                    score = TypeUtil.str2float(spot.getScore());
                    name = spot.getSpotName();
                }
                break;
            }

            case Topology.TYPE_HOTEL: {
                Hotel hotel = getHotel();
                if(hotel!=null){
                    url = hotel.getImg();
                    score = TypeUtil.str2float(hotel.getScore());
                    name = hotel.getName();
                }
                break;
            }

            case Topology.TYPE_FOOD: {
                Food food = getFood();
                if(food!=null){
                    url = food.getImg();
                    score = food.getScore();
                    name = food.getName();
                }
                break;
            }
        }

        loadImage(url,ivPortrait);
        tvName.setText(name);
        ratingBar.setRating(score);
    }

    @Override
    public void onRelease() {}

    @Override
    public void onClick(View v) {
        triggerDeleteComponent();
    }
}
