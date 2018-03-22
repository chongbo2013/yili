package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class MatchRoute {
    private String baseId;
    private String useMyspot;
    private String matchBaseId;

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getUseMyspot() {
        return useMyspot;
    }

    public void setUseMyspot(String useMyspot) {
        this.useMyspot = useMyspot;
    }

    public String getMatchBaseId() {
        return matchBaseId;
    }

    public void setMatchBaseId(String matchBaseId) {
        this.matchBaseId = matchBaseId;
    }
}
