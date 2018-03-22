package com.lots.travel.schedule.base.preview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.schedule.widget.TripPartDisplay;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.TypeUtil;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class PreviewDescHolder extends PreviewScheduleHolder {

    public static PreviewDescHolder create(ViewGroup parent, PreviewScheduleAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_desc,parent,false);
        return new PreviewDescHolder(adapter,v);
    }

    private TextView tvCurrentIndex;
    private View dividerContent;
    private TripPartDisplay tripDisplay;

    public PreviewDescHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_edit);
        tvCurrentIndex = (TextView) v.findViewById(R.id.tv_current_index);
        dividerContent = v.findViewById(R.id.divider_content);
        tripDisplay = (TripPartDisplay) v.findViewById(R.id.trip_display);
        tripDisplay.setImageLoader(mAdapter);
        tripDisplay.setOnControlListener(this);
    }

    @Override
    public void onBind() {
        super.onBind();
        tvCurrentIndex.setText(String.format(Locale.getDefault(),getString(R.string.note_preview_desc),getDay()));

        TripPart tripPart = getContent();
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
}
