package com.lots.travel.main.info.mine.model;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class MineTag {
    public static final String TAG_TRAVEL = "1";

    public static final String TAG_INTEREST = "2";

    /**
     * id : 4
     * px : 4
     * status : 1
     * tag : 1
     * value : 自驾游
     */

    private int id;
    private int px;
    private int status;
    private String tag;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
