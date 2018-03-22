package com.lots.travel.schedule.base.detail.note;

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
import com.lots.travel.schedule.widget.TripPartDisplay;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class NoteDetailSpotHolder extends NoteDetailHolder implements View.OnClickListener {
    public static NoteDetailSpotHolder create(ViewGroup parent, NoteDetailAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_detail_spot,parent,false);
        return new NoteDetailSpotHolder(adapter,v);
    }

    private ImageView ivImg;
    private TextView tvName;
    private RatingBar rbScore;
    private TextView tvPercentPeople;
    private View dividerContent;
    private TripPartDisplay tripDisplay;

    public NoteDetailSpotHolder(ScheduleAdapter adapter, View v) {
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

        dividerContent = v.findViewById(R.id.divider_content);
        tripDisplay = (TripPartDisplay) v.findViewById(R.id.trip_display);
        tripDisplay.setImageLoader(mAdapter);
        tripDisplay.setOnControlListener(this);
    }

    @Override
    public void onBind() {
        super.onBind();
        TripPart tripPart = getContent();
        Spot spot = getSpot();

        loadImage(spot.getViewImg(),ivImg);
        tvName.setText(spot.getSpotName());
        tvName.setTag(spot.getViewurl());
        rbScore.setRating(TypeUtil.str2float(spot.getScore()));
        tvPercentPeople.setText(spot.getPercentPeople());

        tripDisplay.setVideoImages(tripPart.getTripVideoImg(),tripPart.getTripImgs());
        tripDisplay.setVoice(((NoteDetailAdapter)mAdapter).getPortraitUrl(), TypeUtil.str2int(tripPart.getTripSoundLen()));

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
