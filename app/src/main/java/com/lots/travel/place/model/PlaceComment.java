package com.lots.travel.place.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.AccountManager;
import com.lots.travel.store.FileStore;

import java.io.File;

/**
 * Created by nalanzi on 2018/1/3.
 */

public class PlaceComment implements Parcelable {
    private float rating;
    private int currentItem;

    private String text;
    private String[] images;

    private long soundLength;
    private String soundFilepath;

    private String videoFilepath;
    private String videoCover;

    public PlaceComment(){
        currentItem = 1;
        images = new String[0];
        soundFilepath = new FileStore().getCacheVoiceDir()+ File.separator+
                AccountManager.getInstance().getUserId()+"_"+System.currentTimeMillis()+"_place_comment.amr";
    }

    protected PlaceComment(Parcel in) {
        rating = in.readFloat();
        currentItem = in.readInt();
        text = in.readString();
        images = in.createStringArray();
        soundLength = in.readLong();
        soundFilepath = in.readString();
        videoFilepath = in.readString();
        videoCover = in.readString();
    }

    public static final Creator<PlaceComment> CREATOR = new Creator<PlaceComment>() {
        @Override
        public PlaceComment createFromParcel(Parcel in) {
            return new PlaceComment(in);
        }

        @Override
        public PlaceComment[] newArray(int size) {
            return new PlaceComment[size];
        }
    };

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public long getSoundLength() {
        return soundLength;
    }

    public void setSoundLength(long soundLength) {
        this.soundLength = soundLength;
    }

    public String getSoundFilepath() {
        return soundFilepath;
    }

    public void setSoundFilepath(String soundFilepath) {
        this.soundFilepath = soundFilepath;
    }

    public String getVideoFilepath() {
        return videoFilepath;
    }

    public void setVideoFilepath(String videoFilepath) {
        this.videoFilepath = videoFilepath;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(rating);
        dest.writeInt(currentItem);
        dest.writeString(text);
        dest.writeStringArray(images);
        dest.writeLong(soundLength);
        dest.writeString(soundFilepath);
        dest.writeString(videoFilepath);
        dest.writeString(videoCover);
    }
}
