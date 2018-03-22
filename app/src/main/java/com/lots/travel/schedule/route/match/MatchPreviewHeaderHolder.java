package com.lots.travel.schedule.route.match;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.preview.PreviewScheduleAdapter;
import com.lots.travel.schedule.base.preview.PreviewScheduleHolder;
import com.lots.travel.store.db.Account;
import com.lots.travel.store.db.ViewCity;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class MatchPreviewHeaderHolder extends PreviewScheduleHolder {

    public static MatchPreviewHeaderHolder create(ViewGroup parent, PreviewScheduleAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_head,parent,false);
        return new MatchPreviewHeaderHolder(adapter,v);
    }

    private ImageView ivCover;
    //显示目的地名称
    private TextView tvTripName;
    //显示"摄影/度假"+" "+"旅行游记"
    private TextView tvTravelTags;

    private ImageView ivCountry;
    private TextView tvCountry;

    private ImageView ivPortrait;
    private TextView tvUsername;
    private TextView tvProfession;

    public MatchPreviewHeaderHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        ivCover = (ImageView) v.findViewById(R.id.iv_cover);
        tvTripName = (TextView) v.findViewById(R.id.tv_trip_name);
        tvTravelTags = (TextView) v.findViewById(R.id.tv_travel_tags);
        ivCountry = (ImageView) v.findViewById(R.id.iv_country);
        tvCountry = (TextView) v.findViewById(R.id.tv_country);
        ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
        tvUsername = (TextView) v.findViewById(R.id.tv_username);
        tvProfession = (TextView) v.findViewById(R.id.tv_profession);
    }

    @Override
    public void onBind() {
        super.onBind();
        loadImage(mAdapter.getDstImg(),ivCover);
        tvTripName.setText(mAdapter.getDstName());
        tvTravelTags.setText(getTravelTags(mAdapter.getTravelTags()));
        loadImage(mAdapter.getDstCountryImg(),ivCountry);
        tvCountry.setText(mAdapter.getDstCountryName());
        Account account = AccountManager.getInstance().getAccount();
        loadImage(account.getPortraitUrl(),ivPortrait);
        tvUsername.setText(account.getUsername());
        tvProfession.setText(account.getProfession());
    }

    private String getTravelTags(String tags){
        StringBuilder strBuilder = new StringBuilder();
        if(!TextUtils.isEmpty(tags)){
            String[] frags = tags.split(",");
            for(int i=0;i<frags.length;i++){
                if(i!=0)
                    strBuilder.append('/');
                strBuilder.append(frags[i]);
            }
            strBuilder.append(' ');
        }
        strBuilder.append("旅行游记");
        return strBuilder.toString();
    }

    @Override
    public void onRelease() {}

}
