package com.lots.travel.schedule.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.widget.images.Image;

import java.util.List;

public class Description implements Parcelable{
    //文字描述
    private String desc;

    //图片描述
    private List<Image> imageList;

    //语音描述 - 游记id_index_audio_time.amr
    private String audioPath;

    //视频描述 - 游记id_index_video_time.
    private String videoPath;

    //当前选中的是哪一个
    private int currentIndex;

    //是否显示
    private boolean visible;

    public Description(){
        currentIndex = 1;
    }

    protected Description(Parcel in) {
        desc = in.readString();
        audioPath = in.readString();
        videoPath = in.readString();
        currentIndex = in.readInt();
        visible = in.readByte() != 0;
    }

    public static final Creator<Description> CREATOR = new Creator<Description>() {
        @Override
        public Description createFromParcel(Parcel in) {
            return new Description(in);
        }

        @Override
        public Description[] newArray(int size) {
            return new Description[size];
        }
    };

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void toggle(){
        visible = !visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
        dest.writeString(audioPath);
        dest.writeString(videoPath);
        dest.writeInt(currentIndex);
        dest.writeByte((byte) (visible ? 1 : 0));
    }
}
