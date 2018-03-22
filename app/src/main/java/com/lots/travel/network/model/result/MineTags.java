package com.lots.travel.network.model.result;

import com.lots.travel.main.info.mine.model.MineTag;

import java.util.List;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class MineTags {
    private List<MineTag> data;
    private String tag;

    public List<MineTag> getData() {
        return data;
    }

    public void setData(List<MineTag> data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
