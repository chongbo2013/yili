package com.lots.travel.schedule.base.preview;

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
import com.lots.travel.schedule.widget.TripPartDisplay;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class PreviewStayHolder extends PreviewScheduleHolder implements View.OnClickListener {
    public static PreviewStayHolder create(ViewGroup parent, PreviewScheduleAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_stay,parent,false);
        return new PreviewStayHolder(adapter,v);
    }

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvScore;
    private TextView tvLocation;
    private View dividerContent;
    private TripPartDisplay tripDisplay;

    public PreviewStayHolder(ScheduleAdapter adapter, View v) {
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

        dividerContent = v.findViewById(R.id.divider_content);
        tripDisplay = (TripPartDisplay) v.findViewById(R.id.trip_display);
        tripDisplay.setImageLoader(mAdapter);
        tripDisplay.setOnControlListener(this);
    }

    @Override
    public void onBind() {
        super.onBind();
        TripPart part = getContent();
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

        tripDisplay.setVideoImages(part.getTripVideoImg(),part.getTripImgs());
        tripDisplay.setVoice(((PreviewScheduleAdapter)mAdapter).getPortraitUrl(), TypeUtil.str2int(part.getTripSoundLen()));

        RunningData running = (RunningData) getExtraData();
        tripDisplay.setText(running.isExpanded(),part.getTripDesc());

        boolean contentVisible = part.isContentEmpty();
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
