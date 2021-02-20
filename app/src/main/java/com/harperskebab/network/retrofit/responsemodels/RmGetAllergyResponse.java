package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmGetAllergyResponse {

    @SerializedName("AlleryInfo")
    private String alleryInfo;

    public String getAlleryInfo() {
        return alleryInfo;
    }

    public void setAlleryInfo(String alleryInfo) {
        this.alleryInfo = alleryInfo;
    }

}
