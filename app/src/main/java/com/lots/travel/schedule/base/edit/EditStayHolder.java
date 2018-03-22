package com.lots.travel.schedule.base.edit;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class EditStayHolder extends EditScheduleHolder implements View.OnClickListener,DescriptionLayout.OnActionListener {

    public static EditStayHolder create(ViewGroup parent, EditScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_stay,parent,false);
        return new EditStayHolder(adapter,root);
    }

    private View vChoose,vStay;
    private DescriptionLayout vDetails;
    private View detailsDivider;
    private TextView tvAdditional;

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvScore;
    private TextView tvLocation;
    private TextView tvTag;

    public EditStayHolder(EditScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_stay);

        vChoose = v.findViewById(R.id.v_choose);
        vChoose.setOnClickListener(this);
        vStay = v.findViewById(R.id.v_stay);

        vDetails = (DescriptionLayout) v.findViewById(R.id.v_details);
        vDetails.setOnActionListener(this);
        detailsDivider = v.findViewById(R.id.details_divider);

        tvAdditional = (TextView) v.findViewById(R.id.tv_additional);
        tvAdditional.setOnClickListener(this);

        v.findViewById(R.id.btn_edit).setOnClickListener(this);
        v.findViewById(R.id.btn_delete).setOnClickListener(this);

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
                return expand||!isContentEmpty();
            }
        });

        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvScore = (TextView) v.findViewById(R.id.tv_score);
        tvLocation = (TextView) v.findViewById(R.id.tv_location);
        tvTag = (TextView) v.findViewById(R.id.tv_tag);
        tvName.setOnClickListener(this);
    }

    @Override
    public void onBind() {
        RunningData running = (RunningData) getExtraData();
        if(running==null){
            running = new RunningData();
            setExtraData(running);
        }

        TripPart tripPart = getContent();
        if(tripPart==null){
            vChoose.setVisibility(View.VISIBLE);
            vStay.setVisibility(View.GONE);
        }else{
            vChoose.setVisibility(View.GONE);
            vStay.setVisibility(View.VISIBLE);

            tvAdditional.setText(running.isDetailsExpand() ?
                    R.string.schedule_arrow_up:R.string.schedule_arrow_down);
            Drawable arrow = getDrawable(running.isDetailsExpand()
                    ? R.drawable.arrow_up:R.drawable.arrow_down);
            arrow.setBounds(0,0,arrow.getIntrinsicWidth(),arrow.getIntrinsicHeight());
            tvAdditional.setCompoundDrawables(null,null,arrow,null);

            vDetails.setVisibility(running.isDetailsExpand() ? View.VISIBLE:View.GONE);
            detailsDivider.setVisibility(running.isDetailsExpand() ? View.VISIBLE:View.GONE);
            vDetails.setCurrentItem(running.getCurrentTab());

            TripPart part = getContent();
            Hotel stay = getHotel();

            String url = stay.getImg();
            if(!TextUtils.isEmpty(url)){
                loadImage(url,ivImg);
            }

            tvName.setText(stay.getName());

            String score = TextUtils.isEmpty(stay.getScore()) ? null:stay.getScore()+"分 ";
            //String scoreDesc = item.getScoreDesc();
            String scoreType = stay.getScoreType();
            String scoreTotle = TextUtils.isEmpty(stay.getScoreTotle()) ? null:stay.getScoreTotle()+"条评价";

            String extra = TypeUtil.mergeStrings(/*scoreDesc," ",*/scoreType," ",scoreTotle);
            tvScore.setText(showScore(score,extra));

            tvTag.setText(stay.getTag());
            tvLocation.setText(TypeUtil.mergeString(stay.getDistance()," · ",stay.getZone()));

            vDetails.setText(part.getTripDesc());
            vDetails.setImageList(TypeUtil.str2arrays(part.getTripImgs()));

            String filepath = part.getTripSound();
            if(filepath==null)
                filepath = genVoicePath();
            part.setTripSound(filepath);
            vDetails.setVoiceFilepath(filepath,0,TypeUtil.str2int(part.getTripSoundLen()));
            vDetails.setVoiceControl(((EditScheduleAdapter)mAdapter).getAudioRecorder(),((EditScheduleAdapter)mAdapter).getAudioPlayer());
            vDetails.setVideoSource(part.getTripVideo(),part.getTripVideoImg());
        }
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
                TripPart tripPart = getContent();
                if(tripPart!=null && !TextUtils.isEmpty(tripPart.getTripSound())){
                    File file = new File(tripPart.getTripSound());
                    if(file.exists())
                        file.delete();
                }
                triggerDeleteComponent();
                break;
            }

            case R.id.v_choose:{
                triggerAddComponents();
                break;
            }

            case R.id.tv_additional: {
                RunningData running = (RunningData) getExtraData();
                running.toggle();
                notifyChanged();
                break;
            }

            case R.id.tv_name:{
                Hotel stay = getHotel();
                if(stay!=null)
                    triggerScanDetails(stay.getViewurl());
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
