package com.lots.travel.place.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.place.model.Place;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.CheckableImageView;
import com.lots.travel.widget.ImageLoader;

import java.util.Locale;

/**
 * Created by nalanzi on 2018/1/3.
 */

public class PlaceProfileView extends RelativeLayout {
    public static final int ID_DETAIL = R.id.tv_detail;

    private CheckableImageView ivImg;
    private TextView tvName,tvYwDay,tvDetail;
    private TextView[] tvTags;

    public PlaceProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_place_profile,this,true);

        ivImg = (CheckableImageView) findViewById(R.id.iv_img);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvYwDay = (TextView) findViewById(R.id.tv_yw_day);
        tvDetail = (TextView) findViewById(R.id.tv_detail);

        tvTags = new TextView[3];
        tvTags[0] = (TextView) findViewById(R.id.tv_tag_1);
        tvTags[1] = (TextView) findViewById(R.id.tv_tag_2);
        tvTags[2] = (TextView) findViewById(R.id.tv_tag_3);
    }

    public void setOnDetailListener(OnClickListener listener){
        tvDetail.setOnClickListener(listener);
    }

    public void setDetailTag(Object tag){
        tvDetail.setTag(tag);
    }

    public Object getDetailTag(){
        return tvDetail.getTag();
    }

    public void setPlace(ImageLoader loader,Place place){
        loader.loadImage(place.getViewImg(),ivImg);
        String placeName = TextUtils.isEmpty(place.getName()) ? place.getSpotName():place.getName();
        tvName.setText(TypeUtil.mergeString(placeName,"·",place.getCountryName()));
        if(place.getYwDay()>0)
            tvYwDay.setText(String.format(Locale.getDefault(),getResources().getString(R.string.place_yw_day),place.getYwDay()));
        String strTags = place.getTag();

        tvTags[0].setVisibility(View.INVISIBLE);
        tvTags[1].setVisibility(View.INVISIBLE);
        tvTags[2].setVisibility(View.INVISIBLE);
        if(!TextUtils.isEmpty(strTags)){
            String[] tags = strTags.split(" ");
            int tagsLength = Math.min(tags.length,3);
            for (int i=0;i<tagsLength;i++) {
                tvTags[i].setVisibility(View.VISIBLE);
                tvTags[i].setText(tags[i]);
            }
        }
    }

    public void setChecked(boolean checked){
        ivImg.setChecked(checked);
    }

}
