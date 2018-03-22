package com.lots.travel.recruit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lWX479187 on 2017/10/30.
 */
public class Features implements Parcelable {
    private String text;
    private List<String> imageUrls;

    private long audioDuration;
    private String audioUrl;

    private long videoDuration;
    private String videoUrl;

    public Features(){}

    protected Features(Parcel in) {
        text = in.readString();
        imageUrls = in.createStringArrayList();
        audioDuration = in.readLong();
        audioUrl = in.readString();
        videoDuration = in.readLong();
        videoUrl = in.readString();
    }

    public static final Creator<Features> CREATOR = new Creator<Features>() {
        @Override
        public Features createFromParcel(Parcel in) {
            return new Features(in);
        }

        @Override
        public Features[] newArray(int size) {
            return new Features[size];
        }
    };

    public long getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(long audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeStringList(imageUrls);
        dest.writeLong(audioDuration);
        dest.writeString(audioUrl);
        dest.writeLong(videoDuration);
        dest.writeString(videoUrl);
    }
}
