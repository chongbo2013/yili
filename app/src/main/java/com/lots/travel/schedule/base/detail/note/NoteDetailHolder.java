package com.lots.travel.schedule.base.detail.note;

import android.text.TextUtils;
import android.view.View;

import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.widget.AudioPlayer;
import com.lots.travel.schedule.widget.TripPartDisplay;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.video.VideoUtil;
import com.lots.travel.widget.images.LookUpPictureActivity;

/**
 * Created by nalanzi on 2017/12/27.
 */

public abstract class NoteDetailHolder extends ScheduleHolder implements TripPartDisplay.OnControlListener{

    public NoteDetailHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onBind() {
        int itemType = getItemViewType();
        if(itemType==Topology.TYPE_DESC
                || itemType==Topology.TYPE_SPOT
                || itemType==Topology.TYPE_HOTEL
                || itemType==Topology.TYPE_FOOD
                || itemType==Topology.TYPE_STAY) {
            RunningData running = (RunningData) getExtraData();
            if (running == null) {
                running = new RunningData();
                setExtraData(running);
            }
        }
    }

    @Override
    public void onPlayVideo() {
        TripPart tripPart = getContent();
        if(tripPart!=null){
            VideoUtil.play(mAdapter.getContext(),tripPart.getTripVideo());
        }
    }

    @Override
    public void onScanImages(int index, String[] images) {
        LookUpPictureActivity.toLookUp(mAdapter.getContext(),0,false,index,images);
    }

    @Override
    public void onPlayVoiceStart() {
        TripPart tripPart = getContent();
        if(tripPart!=null && ((TypeUtil.str2int(tripPart.getTripSoundLen()))!=0) && !TextUtils.isEmpty(tripPart.getTripSound())){
            AudioPlayer audioPlayer = ((NoteDetailAdapter)mAdapter).getAudioPlayer();
            if(audioPlayer!=null)
                audioPlayer.start(tripPart.getTripSound(),0);
        }
    }

    @Override
    public void onPlayVoiceStop() {
        AudioPlayer audioPlayer = ((NoteDetailAdapter)mAdapter).getAudioPlayer();
        if(audioPlayer!=null)
            audioPlayer.pause();
    }

    @Override
    public void onToggleText() {
        RunningData running = (RunningData) getExtraData();
        if(running!=null) {
            running.toggle();
            notifyChanged();
        }
    }

}
