
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.SplashBanners;

import java.util.List;

public class RmGetSplashResponse {

    @SerializedName("SplashBannersList")
    private List<SplashBanners> splashBanners;

    public List<SplashBanners> getSplashBanners() {
        return splashBanners;
    }

    public void setSplashBanners(List<SplashBanners> splashBanners) {
        this.splashBanners = splashBanners;
    }

}
