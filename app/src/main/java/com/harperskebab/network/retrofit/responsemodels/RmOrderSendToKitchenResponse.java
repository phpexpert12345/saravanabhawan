
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmOrderSendToKitchenResponse {

    @SerializedName("order_identifyno")
    private String orderIdentifyno;
    @SerializedName("success")
    private Long success;

    public String getOrderIdentifyno() {
        return orderIdentifyno;
    }

    public void setOrderIdentifyno(String orderIdentifyno) {
        this.orderIdentifyno = orderIdentifyno;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

}
