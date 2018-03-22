package com.lots.travel.schedule.widget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Created by nalanzi on 2017/9/24.
 */
//播放器焦点、息屏等等未处理
public class AudioPlayer implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener {
    private static final String TAG = "AudioPlayer";
    private MediaPlayer mediaPlayer;
    private OnCallbackListener onCallbackListener;

    public void setOnCallbackListener(OnCallbackListener listener){
        onCallbackListener = listener;
    }

    public void start(String path,int current){
        if(mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
        }else{
            try{
                mediaPlayer.reset();
            }catch (Exception e){
                Log.i(TAG,e.toString());
            }
        }

        try {
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(current);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.i(TAG,e.toString());
        }
    }

    public float getProgress(){
        return mediaPlayer!=null ? mediaPlayer.getCurrentPosition()*1f/mediaPlayer.getDuration():0f;
    }

    public void continuePlay(){
        if(mediaPlayer!=null)
            mediaPlayer.start();
    }

    public void pause(){
        if(mediaPlayer!=null)
            mediaPlayer.pause();
    }

    public void stop(){
        if(mediaPlayer==null)
            return;

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(onCallbackListener!=null)
            onCallbackListener.onCompleted();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(onCallbackListener!=null)
            onCallbackListener.onError();
        return false;
    }

    public interface OnCallbackListener{
        void onCompleted();
        void onError();
    }

}
