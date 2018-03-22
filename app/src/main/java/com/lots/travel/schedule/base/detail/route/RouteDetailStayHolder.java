package com.lots.travel.schedule.base.detail.route;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailStayHolder extends RouteDetailHolder implements View.OnClickListener {
    public static RouteDetailStayHolder create(ViewGroup parent, RouteDetailAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_detail_stay,parent,false);
        return new RouteDetailStayHolder(adapter,v);
    }

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvScore;
    private TextView tvLocation;
    private TextView tvShortDesc;

    public RouteDetailStayHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_stay);

        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvScore = (TextView) v.findViewById(R.id.tv_score);
        tvLocation = (TextView) v.findViewById(R.id.tv_location);
        tvName.setOnClickListener(this);
        tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
    }

    @Override
    public void onBind() {
        Hotel hotel = getHotel();

        loadImage(hotel.getImg(),ivImg);
        tvName.setText(hotel.getName());
        String score = TextUtils.isEmpty(hotel.getScore()) ? null:hotel.getScore()+"分 ";
        //String scoreDesc = item.getScoreDesc();
        String scoreType = hotel.getScoreType();
        String scoreTotle = TextUtils.isEmpty(hotel.getScoreTotle()) ? null:hotel.getScoreTotle()+"条评价";

        String extra = TypeUtil.mergeStrings(/*scoreDesc," ",*/scoreType," ",scoreTotle);
        tvScore.setText(showScore(score,extra));

        tvLocation.setText(TypeUtil.mergeString(hotel.getDistance(),"·",hotel.getZone()));

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

    private SpannableStringBuilder showScore(String value, String extra){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        int textColor;
        int end = 0;
        if(!TextUtils.isEmpty(value)){
            strBuilder.append(value);
            end = strBuilder.length();
            textColor = getColor(R.color.color_main);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if(!TextUtils.isEmpty(extra)){
            strBuilder.append(extra);
            textColor = getColor(R.color.color_text);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strBuilder;
    }

}
