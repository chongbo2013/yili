package com.lots.travel.schedule.base.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.schedule.widget.DescriptionLayout;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.video.VideoUtil;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/5.
 */

public class EditDescHolder extends EditScheduleHolder implements View.OnClickListener,DescriptionLayout.OnActionListener{

    public static EditDescHolder create(ViewGroup parent, EditScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_desc,parent,false);
        return new EditDescHolder(adapter,root);
    }

    private View vDesc,vAdd;
    private TextView tvCurrentDay;
    private DescriptionLayout vDetails;

    public EditDescHolder(EditScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        vDesc = v.findViewById(R.id.v_desc);
        vAdd = v.findViewById(R.id.v_add);
        tvCurrentDay = (TextView) v.findViewById(R.id.tv_current_day);
        vDetails = (DescriptionLayout) v.findViewById(R.id.v_details);

        vAdd.setOnClickListener(this);
        vDetails.setOnActionListener(this);

        setLeftIcon(R.drawable.ico_timeline_edit);
    }

    @Override
    public void onBind() {
        TripPart tripPart = getContent();
        RunningData running = (RunningData) getExtraData();

        if(running==null) {
            running = new RunningData();
            setExtraData(running);
        }

        vAdd.setVisibility(tripPart==null ? View.VISIBLE:View.GONE);
        vDesc.setVisibility(tripPart==null ? View.GONE:View.VISIBLE);
        vDetails.setCurrentItem(running.getCurrentTab());

        tvCurrentDay.setText(String.format(
                Locale.getDefault(),getString(R.string.schedule_desc_group),getDay()));

        if(tripPart!=null){
            vDetails.setCurrentItem(running.getCurrentTab());
            vDetails.setText(tripPart.getTripDesc());
            vDetails.setImageList(TypeUtil.str2arrays(tripPart.getTripImgs()));

            String filepath = tripPart.getTripSound();
            if(filepath==null)
                filepath = genVoicePath();
            tripPart.setTripSound(filepath);
            vDetails.setVoiceFilepath(filepath,0,TypeUtil.str2int(tripPart.getTripSoundLen()));
            vDetails.setVoiceControl(((EditScheduleAdapter)mAdapter).getAudioRecorder(),((EditScheduleAdapter)mAdapter).getAudioPlayer());
            vDetails.setVideoSource(tripPart.getTripVideo(),tripPart.getTripVideoImg());
        }
    }

    @Override
    public void onRelease() {
        vDetails.resetVoice();
    }

    @Override
    public void onClick(View v) {
        triggerAddDesc();
    }

    @Override
    public void onAction(int ac, Object data) {
        TripPart part = getContent();
        switch (ac){
            case DescriptionLayout.AC_CHANGE_TAB:
                RunningData running = (RunningData) getExtraData();
                running.setCurrentTab((Integer) data);
                break;

            case DescriptionLayout.AC_ADD_IMAGES:
                triggerAddImages();
                break;

            case DescriptionLayout.AC_ADD_VIDEO:
                triggerAddVideo();
                break;

            case DescriptionLayout.AC_EDIT_TEXT:
                triggerEditText(part!=null ? part.getTripDesc():null);
                break;

            case DescriptionLayout.AC_SCAN_IMAGES:
                triggerScanImages(part.getTripImgs(),(Integer) data);
                break;

            //语音长度大于0就有效
            case DescriptionLayout.AC_ADD_AUDIO:
                triggerAddAudio((Integer) data);
                String len = Integer.toString((Integer) data);
                if(part!=null && !len.equals(part.getTripSoundLen())) {
                    part.setTripSoundLen(len);
                    updateContent(part,false);
                }
                break;

            case DescriptionLayout.AC_DELETE_VIDEO:
                if(part!=null){
                    FileUtil.deleteFilesByPaths(part.getTripVideo(),part.getTripVideoImg());
                    part.setTripVideo(null);
                    part.setTripVideoImg(null);
                    updateContent(part,false);
                }
                break;

            case DescriptionLayout.AC_PLAY_VIDEO:
                if(part!=null){
                    VideoUtil.play(mAdapter.getContext(),part.getTripVideo());
                }
                break;
        }
    }

}
