package com.lots.travel.schedule.base.edit;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.schedule.widget.DescriptionLayout;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.video.VideoUtil;
import com.lots.travel.widget.SwipeItemLayout;

import java.io.File;

/**
 * Created by nalanzi on 2017/11/5.
 */

public class EditHotelHolder extends EditScheduleHolder implements View.OnClickListener,DescriptionLayout.OnActionListener {

    public static EditHotelHolder create(ViewGroup parent, EditScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_hotel,parent,false);
        return new EditHotelHolder(adapter,root);
    }

    private DescriptionLayout vDetails;
    private View detailsDivider;
    private TextView tvAdditional;

    private ImageView ivImg;
    private TextView tvName;
    private RatingBar rbScore;
    private TextView tvScoreType;
    private TextView tvTag;
    private TextView tvLocation;

    public EditHotelHolder(EditScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(getDrawable(R.drawable.ico_timeline_hotel));

        v.findViewById(R.id.btn_edit).setOnClickListener(this);
        v.findViewById(R.id.btn_delete).setOnClickListener(this);
        tvAdditional = (TextView) v.findViewById(R.id.tv_additional);
        tvAdditional.setOnClickListener(this);

        vDetails = (DescriptionLayout) v.findViewById(R.id.v_details);
        vDetails.setOnActionListener(this);
        detailsDivider = v.findViewById(R.id.details_divider);

        ((SwipeItemLayout)v).setOnSwipeListener(new SwipeItemLayout.OnSwipeListener() {
            @Override
            public boolean onSwipe(MotionEvent ev) {
                if(vDetails.dragRecyclerView(ev))
                    return true;

                RunningData running = (RunningData) getExtraData();
                boolean expand = running.isDetailsExpand();
                if(expand) {
                    running.toggle();
                    notifyChanged();
                }
                return expand;
            }
        });

        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        rbScore = (RatingBar) v.findViewById(R.id.rb_score);
        tvScoreType = (TextView) v.findViewById(R.id.tv_score_type);
        tvTag = (TextView) v.findViewById(R.id.tv_tag);
        tvLocation = (TextView) v.findViewById(R.id.tv_location);
        tvName.setOnClickListener(this);
    }

    @Override
    public void onBind() {
        RunningData running = (RunningData) getExtraData();

        if(running==null){
            running = new RunningData();
            setExtraData(running);
        }

        tvAdditional.setText(running.isDetailsExpand() ?
                R.string.schedule_arrow_up:R.string.schedule_arrow_down);
        Drawable arrow = getDrawable(running.isDetailsExpand()
                ? R.drawable.arrow_up:R.drawable.arrow_down);
        arrow.setBounds(0,0,arrow.getIntrinsicWidth(),arrow.getIntrinsicHeight());
        tvAdditional.setCompoundDrawables(null,null,arrow,null);

        vDetails.setVisibility(running.isDetailsExpand() ? View.VISIBLE:View.GONE);
        detailsDivider.setVisibility(running.isDetailsExpand() ? View.VISIBLE:View.GONE);
        vDetails.setCurrentItem(running.getCurrentTab());

        TripPart tripPart = getContent();
        Hotel hotel = getHotel();

        String url = hotel.getImg();
        if(!TextUtils.isEmpty(url)){
            loadImage(url,ivImg);
        }

        tvName.setText(hotel.getName());

        float score = TypeUtil.str2float(hotel.getScore());
        rbScore.setRating(score);
        tvScoreType.setText(hotel.getScoreType());
        tvTag.setText(hotel.getTag());
        tvLocation.setText(TypeUtil.mergeString(hotel.getDistance()," · ",hotel.getZone()));

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

    @Override
    public void onRelease() {
        vDetails.resetVoice();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit:
                ((SwipeItemLayout)itemView).close();
                break;

            case R.id.btn_delete: {
                TripPart part = getContent();
                if(part!=null && !TextUtils.isEmpty(part.getTripSound())){
                    File file = new File(part.getTripSound());
                    if(file.exists())
                        file.delete();
                }
                triggerDeleteComponent();
                break;
            }

            case R.id.tv_additional: {
                RunningData running = (RunningData) getExtraData();
                running.toggle();
                notifyChanged();
                break;
            }

            case R.id.tv_name:{
                Hotel hotel = getHotel();
                if(hotel!=null)
                    triggerScanDetails(hotel.getViewurl());
                break;
            }
        }
    }

    @Override
    public void onAction(int ac, Object data) {
        TripPart tripPart = getContent();

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
                triggerEditText(tripPart!=null ? tripPart.getTripDesc():null);
                break;

            case DescriptionLayout.AC_SCAN_IMAGES:
                triggerScanImages(tripPart.getTripImgs(), (Integer) data);
                break;

            //语音长度大于0就有效
            case DescriptionLayout.AC_ADD_AUDIO:
                triggerAddAudio((Integer) data);
                String len = Integer.toString((Integer) data);
                if(tripPart!=null && !len.equals(tripPart.getTripSoundLen())) {
                    tripPart.setTripSoundLen(len);
                    updateContent(tripPart,false);
                }
                break;

            case DescriptionLayout.AC_DELETE_VIDEO:
                if(tripPart!=null){
                    FileUtil.deleteFilesByPaths(tripPart.getTripVideo(),tripPart.getTripVideoImg());
                    tripPart.setTripVideo(null);
                    tripPart.setTripVideoImg(null);
                    updateContent(tripPart,false);
                }
                break;

            case DescriptionLayout.AC_PLAY_VIDEO:
                if(tripPart!=null){
                    VideoUtil.play(mAdapter.getContext(),tripPart.getTripVideo());
                }
                break;
        }
    }

}
