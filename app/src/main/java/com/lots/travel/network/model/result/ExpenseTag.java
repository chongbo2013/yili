package com.lots.travel.network.model.result;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nalanzi on 2018/1/4.
 */

public class ExpenseTag implements Parcelable{
    /**
     * id : 117
     * logo : https://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/tags/feiyongbaohanorbubaohan/1495097813496.png
     * logoDisabled : https://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/tags/feiyongbaohanorbubaohan/1495097814012.png
     * logoOn : https://zhongyou-hz.oss-cn-hangzhou.aliyuncs.com/tags/feiyongbaohanorbubaohan/1495097813995.png
     * px : 10
     * status : 1
     * tag : 费用包含or不包含
     * text :
     * value : 部分正餐
     */

    private int id;
    private String logo;
    private String logoDisabled;
    private String logoOn;
    private int px;
    private int status;
    private String tag;
    private String text;
    private String value;

    public ExpenseTag(ExpenseTag src){
        id = src.id;
        logo = src.logo;
        logoDisabled = src.logoDisabled;
        logoOn = src.logoOn;
        px  = src.px;
        status = src.status;
        tag = src.tag;
        text = src.text;
        value = src.value;
    }

    protected ExpenseTag(Parcel in) {
        id = in.readInt();
        logo = in.readString();
        logoDisabled = in.readString();
        logoOn = in.readString();
        px = in.readInt();
        status = in.readInt();
        tag = in.readString();
        text = in.readString();
        value = in.readString();
    }

    public static final Creator<ExpenseTag> CREATOR = new Creator<ExpenseTag>() {
        @Override
        public ExpenseTag createFromParcel(Parcel in) {
            return new ExpenseTag(in);
        }

        @Override
        public ExpenseTag[] newArray(int size) {
            return new ExpenseTag[size];
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

    public String getLogoDisabled() {
        return logoDisabled;
    }

    public void setLogoDisabled(String logoDisabled) {
        this.logoDisabled = logoDisabled;
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
        dest.writeString(logoDisabled);
        dest.writeString(logoOn);
        dest.writeInt(px);
        dest.writeInt(status);
        dest.writeString(tag);
        dest.writeString(text);
        dest.writeString(value);
    }
}
