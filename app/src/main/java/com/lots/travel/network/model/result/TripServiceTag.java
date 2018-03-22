package com.lots.travel.network.model.result;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2018/1/4.
 */

public class TripServiceTag implements Parcelable{
    /**
     * id : 99
     * logo : http://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/tags/lvxingfuwu/1494918101800.png
     * logoOn : http://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/tags/lvxingfuwu/1494918103014.png
     * px : 1
     * status : 1
     * tag : 旅行服务
     * text : 你有专业的摄影技能和相机，愿意在旅行中帮同伴拍照。
     * value : 旅拍摄影
     */

    private int id;
    private String logo;
    private String logoOn;
    private int px;
    private int status;
    private String tag;
    private String text;
    private String value;

    public TripServiceTag() {}

    public TripServiceTag(TripServiceTag src) {
        id = src.id;
        logo = src.logo;
        logoOn = src.logoOn;
        px = src.px;
        status = src.status;
        tag = src.tag;
        text = src.text;
        value = src.value;
    }

    protected TripServiceTag(Parcel in) {
        id = in.readInt();
        logo = in.readString();
        logoOn = in.readString();
        px = in.readInt();
        status = in.readInt();
        tag = in.readString();
        text = in.readString();
        value = in.readString();
    }

    public static final Creator<TripServiceTag> CREATOR = new Creator<TripServiceTag>() {
        @Override
        public TripServiceTag createFromParcel(Parcel in) {
            return new TripServiceTag(in);
        }

        @Override
        public TripServiceTag[] newArray(int size) {
            return new TripServiceTag[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoOn() {
        return logoOn;
    }

    public void setLogoOn(String logoOn) {
        this.logoOn = logoOn;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(logo);
        dest.writeString(logoOn);
        dest.writeInt(px);
        dest.writeInt(status);
        dest.writeString(tag);
        dest.writeString(text);
        dest.writeString(value);
    }
}
