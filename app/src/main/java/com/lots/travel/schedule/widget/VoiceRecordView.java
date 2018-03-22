package com.lots.travel.schedule.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lots.travel.R;

import java.io.File;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/20.
 */
//在其detach时停止
public class VoiceRecordView extends LinearLayout implements OnVoiceCallback {
    private TextView tvProgress;
    private VoiceCircleView vVoice;
    private TextView tvDelete;

    private AudioRecorder recorder;
    private AudioPlayer player;

    private String filepath;
    private OnVoiceListener onVoiceListener;

    public VoiceRecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.layout_voice_record,this,true);

        tvProgress = (TextView) findViewById(R.id.tv_progress);
        vVoice = (VoiceCircleView) findViewById(R.id.v_voice);
        vVoice.setCallback(this);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reset(false);
                tvProgress.setText(showMills(0,60000));
            }
        });

        tvProgress.setText(showMills(0,60000));
        vVoice.setPhase(false,0,0,60000);
    }

    public void setControl(AudioRecorder recorder,AudioPlayer player){
        this.recorder = recorder;
        this.player = player;
    }

    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    public void setPhase(int phase,long startLength,long totalLength){
        startLength = Math.min(startLength,60000);
        totalLength = Math.min(totalLength,60000);
        tvProgress.setText(showMills(phase==1 ? startLength:0,phase==1 ? totalLength:60000));
        vVoice.setPhase(phase==1,startLength,totalLength,60000);
    }

    public void reset(boolean saveFile){
        int length = vVoice.getFilepathLength();

        if(vVoice.isRunning()) {
            recorder.stopRecord();
            player.stop();
            if(onVoiceListener !=null){
                onVoiceListener.onLengthChanged(length);
            }
        }

        setPhase(0, 0, 0);

        if(!saveFile){
            length = 0;
            File file = new File(filepath);
            if(file.exists())
                file.delete();
        }
    }

    public void setOnVoiceListener(OnVoiceListener listener){
        onVoiceListener = listener;
    }

    @Override
    public void onRecordStart(long maxLength) {
        tvProgress.setText(showMills(0,maxLength));
        recorder.setMaxDuration((int) maxLength);
        recorder.setFilePath(filepath);
        recorder.startRecord();
    }

    @Override
    public void onRecordComplete(long totalLength) {
        tvProgress.setText(showMills(0,totalLength));
        recorder.stopRecord();
        if(onVoiceListener !=null){
            onVoiceListener.onLengthChanged((int) totalLength);
        }
    }

    @Override
    public void onPlayStart(long startLength,long totalLength) {
        startLength = 0;
        tvProgress.setText(showMills(startLength,totalLength));
        player.start(filepath, (int) startLength);
    }

    @Override
    public void onPlayStop(long endLength,long totalLength) {
        tvProgress.setText(
                showMills(endLength==totalLength ? 0:endLength,totalLength));
        player.stop();
    }

    @Override
    public void onRunning(long currentLength, long totalLength) {
        tvProgress.setText(showMills(currentLength,totalLength));

    }

    private SpannableStringBuilder showMills(long current,long total){
        String currentStr = String.format(Locale.getDefault(),"%02d.%02d",(int) (current/1000),(int) (current%1000/10));
        String totalStr = String.format(Locale.getDefault(),"%02d.%02d",(int) (total/1000),(int) (total%1000/10));

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append(currentStr).append('/');
        int textColor = ContextCompat.getColor(getContext(),R.color.color_main);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(totalStr);
        textColor = ContextCompat.getColor(getContext(),R.color.color_text_secondary);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),currentStr.length(),strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    public interface OnVoiceListener {
        void onLengthChanged(int length);
    }

}
