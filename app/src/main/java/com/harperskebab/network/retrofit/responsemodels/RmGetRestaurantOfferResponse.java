
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.RestaurantDiscountCoupon;

import java.util.List;

public class RmGetRestaurantOfferResponse {

    @SerializedName("RestaurantDiscountCouponList")
    private List<RestaurantDiscountCoupon> restaurantDiscountCoupon;

    public List<RestaurantDiscountCoupon> getRestaurantDiscountCoupon() {
        return restaurantDiscountCoupon;
    }

    public void setRestaurantDiscountCoupon(List<RestaurantDiscountCoupon> restaurantDiscountCoupon) {
        this.restaurantDiscountCoupon = restaurantDiscountCoupon;
    }

}
