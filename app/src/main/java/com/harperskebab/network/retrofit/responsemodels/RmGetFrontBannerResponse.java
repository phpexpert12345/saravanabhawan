package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.FrontBanner;

import java.util.List;

public class RmGetFrontBannerResponse {

    @SerializedName("FrontBannersList")
    private List<FrontBanner> frontBanner;

    public List<FrontBanner> getFrontBanner() {
        return frontBanner;
    }

    public void setFrontBanner(List<FrontBanner> frontBanner) {
        this.frontBanner = frontBanner;
    }

}
