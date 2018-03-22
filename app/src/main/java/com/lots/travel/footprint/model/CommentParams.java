package com.lots.travel.footprint.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.lots.travel.main.info.mine.model.FollowNote;

import org.json.JSONObject;

/**
 * Created by nalanzi on 2018/2/6.
 */

public class CommentParams implements Parcelable{
    private String id;
    private String eventStyle;
    private String gpsAddress;
    private String tripImgs;
    private String tripVideo;
    private String tripVideoImg;
    private String tripTitle;
    private int hasZan;
    private int commentTotles;
    private int zanTotles;

    public CommentParams(Footprint t){
        id = t.getId();
        eventStyle = t.getEventStyle();
        gpsAddress = t.getGpsAddress();
        if(t.getEventStyle().equals(Footprint.NOTE)){
            try{
                String json = t.getJson();
                JSONObject obj = new JSONObject(json);
                gpsAddress = obj.getString("cityToName");
            }catch (Exception e){
                Log.e(CommentParams.class.getName(),e.toString());
            }
        }
        tripImgs = t.getTripImgs();
        tripVideo = t.getTripVideo();
        tripVideoImg = t.getTripVideoImg();
        tripTitle = t.getTripTitle();
        hasZan = t.getHasZan();
        commentTotles = t.getCommentTotles();
        zanTotles = t.getZanTotles();
    }

    public CommentParams(FollowNote n){
        id = n.getDataKey();
        eventStyle = Footprint.NOTE;
        try{
            String json = n.getJson();
            JSONObject obj = new JSONObject(json);
            gpsAddress = obj.getString("cityToName");
        }catch (Exception e){
            Log.e(CommentParams.class.getName(),e.toString());
        }
        tripImgs = n.getTripImgs();
        tripVideo = n.getTripVideo();
        tripVideoImg = n.getTripVideoImg();
        tripTitle = n.getTripTitle();
        hasZan = n.getHasZan();
        commentTotles = n.getCommentTotles();
        zanTotles = n.getZanTotles();
    }

    protected CommentParams(Parcel in) {
        id = in.readString();
        eventStyle = in.readString();
        gpsAddress = in.readString();
        tripImgs = in.readString();
        tripVideo = in.readString();
        tripVideoImg = in.readString();
        tripTitle = in.readString();
        hasZan = in.readInt();
        commentTotles = in.readInt();
        zanTotles = in.readInt();
    }

    public static final Creator<CommentParams> CREATOR = new Creator<CommentParams>() {
        @Override
        public CommentParams createFromParcel(Parcel in) {
            return new CommentParams(in);
        }

        @Override
        public CommentParams[] newArray(int size) {
            return new CommentParams[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }

    public String getGpsAddress() {
        return gpsAddress;
    }

    public void setGpsAddress(String gpsAddress) {
        this.gpsAddress = gpsAddress;
    }

    public String getTripImgs() {
        return tripImgs;
    }

    public void setTripImgs(String tripImgs) {
        this.tripImgs = tripImgs;
    }

    public String getTripVideo() {
        return tripVideo;
    }

    public void setTripVideo(String tripVideo) {
        this.tripVideo = tripVideo;
    }

    public String getTripVideoImg() {
        return tripVideoImg;
    }

    public void setTripVideoImg(String tripVideoImg) {
        this.tripVideoImg = tripVideoImg;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public int getCommentTotles() {
        return commentTotles;
    }

    public void setCommentTotles(int commentTotles) {
        this.commentTotles = commentTotles;
    }

    public int incComment(){
        return ++commentTotles;
    }

    public int decComment(){
        return --commentTotles;
    }

    public int getZanTotles() {
        return zanTotles;
    }

    public void setZanTotles(int zanTotles) {
        this.zanTotles = zanTotles;
    }

    public int incZan(){
        return ++zanTotles;
    }

    public int decZan(){
        return --zanTotles;
    }

    public int getHasZan() {
        return hasZan;
    }

    public void setHasZan(int hasZan) {
        this.hasZan = hasZan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(eventStyle);
        dest.writeString(gpsAddress);
        dest.writeString(tripImgs);
        dest.writeString(tripVideo);
        dest.writeString(tripVideoImg);
        dest.writeString(tripTitle);
        dest.writeInt(hasZan);
        dest.writeInt(commentTotles);
        dest.writeInt(zanTotles);
    }
}
