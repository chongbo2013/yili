package com.lots.travel.network.model.request;

import com.lots.travel.store.db.Account;

/**
 * Created by nalanzi on 2018/1/31.
 */

public class GetRongToken {
    private String userId;
    private String userName;
    private String portraitUri;

    public GetRongToken(Account account){
        userId = account.getUserId()+"";
        userName = account.getUsername();
        portraitUri = account.getPortraitUrl();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }
}
