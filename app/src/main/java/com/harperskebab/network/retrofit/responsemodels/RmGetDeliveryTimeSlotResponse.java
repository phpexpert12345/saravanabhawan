package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.DeliveryTime;

import java.util.List;

public class RmGetDeliveryTimeSlotResponse {

    @SerializedName("TimeList")
    private List<DeliveryTime> deliveryTime;

    public List<DeliveryTime> getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(List<DeliveryTime> deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

}
