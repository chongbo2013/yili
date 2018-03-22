package com.lots.travel.schedule.widget;

/**
 * Created by nalanzi on 2017/11/21.
 */

public interface OnVoiceCallback{
    void onRecordStart(long maxLength);
    void onRecordComplete(long totalLength);
    void onPlayStart(long startLength,long totalLength);
    void onPlayStop(long endLength,long totalLength);
    void onRunning(long currentLength,long maxLength);
}
