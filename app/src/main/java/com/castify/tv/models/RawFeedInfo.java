package com.castify.tv.models;

import com.google.gson.JsonObject;

public class RawFeedInfo {
    private String finalClickUrl;

    private JsonObject requestData;

    private JsonObject demandData;

    public String getfinalClickUrl() {
        return finalClickUrl;
    }

    public void setfinalClickUrl(String finalClickUrl) {
        this.finalClickUrl = finalClickUrl;
    }

    public String getFinalClickUrl() {
        return finalClickUrl;
    }

    public void setFinalClickUrl(String finalClickUrl) {
        this.finalClickUrl = finalClickUrl;
    }

    public JsonObject getRequestData() {
        return requestData;
    }

    public void setRequestData(JsonObject requestData) {
        this.requestData = requestData;
    }

    public JsonObject getDemandData() {
        return demandData;
    }

    public void setDemandData(JsonObject demandData) {
        this.demandData = demandData;
    }
}
