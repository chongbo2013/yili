package com.lots.travel.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lots.travel.R;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/5.
 */

public class VoiceDescView extends LinearLayout {
    private ImageView ivPortrait;
    private VoiceBar voiceBar;
    private TextView tvLength;

    public VoiceDescView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.layout_voice_desc,this,true);

        ivPortrait = (ImageView) findViewById(R.id.iv_portrait);
        voiceBar = (VoiceBar) findViewById(R.id.voice_bar);
        tvLength = (TextView) findViewById(R.id.tv_length);
    }

    public void setPortrait(ImageLoader loader,String url){
        loader.loadImage(url,ivPortrait);
    }

    public void setVoice(int length){
        tvLength.setText(String.format(Locale.getDefault(),"%d\"",length/1000));
        voiceBar.setDuration(length);
    }

    public void setOnPlayListener(VoiceBar.OnPlayListener listener){
        voiceBar.setOnPlayListener(listener);
    }

    public void reset(){
        voiceBar.reset();
    }

}
