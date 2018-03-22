package com.lots.travel.schedule.base.preview;

import android.app.Activity;

import com.lots.travel.AccountManager;
import com.lots.travel.schedule.widget.AudioPlayer;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.HolderFactory;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.store.db.Account;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class PreviewScheduleAdapter extends ScheduleAdapter {
    private AudioPlayer mAudioPlayer;
    private String mPortraitUrl;

    public PreviewScheduleAdapter(Activity context, Topology topology, DataManager dataManager, HolderFactory holderFactory) {
        super(context, topology, dataManager, holderFactory);
        mAudioPlayer = new AudioPlayer();

        Account account = AccountManager.getInstance().getAccount();
        mPortraitUrl = account.getPortraitUrl();
    }

    public AudioPlayer getAudioPlayer(){
        return mAudioPlayer;
    }

    public String getPortraitUrl(){
        return mPortraitUrl;
    }

    public int getChildRelativePosition(ScheduleHolder holder){
        int itemPosition = getItemPosition(holder.getFlatPosition());
        Group group = getGroup(itemPosition);
        return itemPosition-group.getFlatPosition();
    }

    //这里component特指desc、spot、hotel、food、stay
    public boolean isComponent(ScheduleHolder holder){
        int type = holder.getItemViewType();
        return type!=Topology.TYPE_DATE && type!=Topology.TYPE_TRAFFIC;
    }

    public boolean isBottom(ScheduleHolder holder){
        int flatPosition = holder.getFlatPosition();
        return holder.getItemViewType()!=Topology.TYPE_DATE && flatPosition==getItemCount()-1;
    }

}
