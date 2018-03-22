package com.lots.travel.network.model.request;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class GetTripDaysInfo {
    private String baseId;
    private String queryType;//"viewInfo"
    private String loadAllData;//"1"

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getLoadAllData() {
        return loadAllData;
    }

    public void setLoadAllData(String loadAllData) {
        this.loadAllData = loadAllData;
    }
}
