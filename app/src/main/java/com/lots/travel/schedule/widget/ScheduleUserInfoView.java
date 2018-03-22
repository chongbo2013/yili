package com.lots.travel.schedule.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2018/1/9.
 */

public class ScheduleUserInfoView extends RelativeLayout {
    private ImageView ivPortrait;
    private TextView tvUsername,tvProfession,tvTip;
    private TextView tvOperate,tvMap;

    public ScheduleUserInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_schedule_userinfo,this,true);

        ivPortrait = (ImageView) findViewById(R.id.iv_portrait);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvProfession = (TextView) findViewById(R.id.tv_profession);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        tvOperate = (TextView) findViewById(R.id.tv_operate);
        tvMap = (TextView) findViewById(R.id.tv_map);
    }

}
