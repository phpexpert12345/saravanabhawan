package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.RestaurantBranch;

import java.util.List;

public class RmGetBranchResponse {

    @SerializedName("RestaurantBranchList")
    private List<RestaurantBranch> restaurantBranch;

    public List<RestaurantBranch> getRestaurantBranch() {
        return restaurantBranch;
    }

    public void setRestaurantBranch(List<RestaurantBranch> restaurantBranch) {
        this.restaurantBranch = restaurantBranch;
    }

}
