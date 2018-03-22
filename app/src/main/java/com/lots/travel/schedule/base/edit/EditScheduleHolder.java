package com.lots.travel.schedule.base.edit;

import java.io.File;
import android.view.View;
import com.lots.travel.AccountManager;
import com.lots.travel.schedule.base.ScheduleHolder;
import com.lots.travel.store.FileStore;

/**
 * Created by nalanzi on 2017/12/26.
 */

public abstract class EditScheduleHolder extends ScheduleHolder {

    public EditScheduleHolder(EditScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    public String genVoicePath(){
        return new FileStore().getCacheVoiceDir()+File.separator+
                AccountManager.getInstance().getUserId()+"_"+System.currentTimeMillis()+"_"+getFlatPosition()+".amr";
    }

    public void triggerAddDesc(){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onAddDesc(getFlatPosition());
    }

    public void triggerEditText(String text){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onEditText(getFlatPosition(),text);
    }

    public void triggerAddAudio(int length){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onAddAudio(getFlatPosition(),length);
    }

    public void triggerAddVideo(){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onAddVideo(getFlatPosition());
    }

    public void triggerScanImages(String images,int imagePos){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onScanImages(getFlatPosition(),images,imagePos);
    }

    public void triggerAddImages(){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onAddImages(getFlatPosition());
    }

    public void triggerScanDetails(String url){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onScanDetails(url);
    }

    public void triggerAddComponents(){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onAddComponents(getFlatPosition());
    }

    public void triggerDeleteComponent(){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onDeleteComponent(getFlatPosition());
    }

    public void triggerAddTraffic(){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onAddTraffic(getFlatPosition());
    }

    public void triggerDeleteTraffic(){
        OnScheduleListener listener = ((EditScheduleAdapter)mAdapter).getOnScheduleListener();
        if(listener!=null)
            listener.onDeleteTraffic(getFlatPosition());
    }

}
