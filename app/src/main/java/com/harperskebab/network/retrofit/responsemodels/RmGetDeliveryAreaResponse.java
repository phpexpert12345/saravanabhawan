package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.DeliveryArea;

import java.util.List;

public class RmGetDeliveryAreaResponse {

    @SerializedName("DeliveryAreaList")
    private List<DeliveryArea> deliveryArea;

    public List<DeliveryArea> getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(List<DeliveryArea> deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

}
