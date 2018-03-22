package com.lots.travel.schedule.base.detail.note;

import android.app.Activity;

import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.widget.AudioPlayer;

/**
 * Created by nalanzi on 2018/1/15.
 */

public class NoteDetailAdapter extends ScheduleAdapter {
    private AudioPlayer mAudioPlayer;
    private String mPortraitUrl;

    public NoteDetailAdapter(Activity context, Topology topology, DataManager dataManager, HolderFactory holderFactory) {
        super(context, topology, dataManager, holderFactory);
        mAudioPlayer = new AudioPlayer();
    }

    public void setPortraitUrl(String url){
        mPortraitUrl = url;
    }

    public AudioPlayer getAudioPlayer(){
        return mAudioPlayer;
    }

    public String getPortraitUrl(){
        return mPortraitUrl;
    }

}
