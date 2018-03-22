package com.lots.travel.schedule.widget;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by nalanzi on 2017/9/24.
 */
//使用MediaRecorder录音、MediaPlayer播放，格式为amr
//不太手机可能音量过小，换成使用AudioRecorder控制音量但是需要处理格式问题，后期更换。
public class AudioRecorder {
    private static final String TAG = "AudioRecorder";

    private MediaRecorder mediaRecorder;

    private String filePath;

    private int maxDuration = 600000;

    private long startTime,endTime;

    public AudioRecorder(){
        filePath = "/dev/null";
    }

    public void setFilePath(String path){
        filePath = path;
    }

    public void setMaxDuration(int duration){
        maxDuration = duration;
    }

    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mediaRecorder == null)
            mediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            /* ③准备 */
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.setMaxDuration(maxDuration);
            mediaRecorder.prepare();
            /* ④开始 */
            mediaRecorder.start();
            startTime = System.currentTimeMillis();
        } catch (IllegalStateException | IOException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    public float getProgress(){
        return (System.currentTimeMillis()-startTime)*1f/maxDuration;
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();

        try{
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            mediaRecorder.setPreviewDisplay(null);

            mediaRecorder.stop();
        }catch (RuntimeException e){
            Log.e(TAG,e.toString());
        }

        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;

        return Math.min(maxDuration,endTime-startTime);
    }

    //计算分贝
    private double updateMicStatus() {
        double db = 0;// 分贝

        if (mediaRecorder != null) {
            double ratio = (double)mediaRecorder.getMaxAmplitude();
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
            }
        }

        return db;
    }

}
