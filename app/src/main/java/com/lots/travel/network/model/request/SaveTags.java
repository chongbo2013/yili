package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2018/1/22.
 */

public class SaveTags {
    private Long userId;
    private String tags;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
