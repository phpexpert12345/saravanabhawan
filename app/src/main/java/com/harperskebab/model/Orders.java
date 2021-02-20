package com.harperskebab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {
    @SerializedName("OrderViewResult")
    @Expose
    private List<OrderViewResult> orderViewResult = null;

    public List<OrderViewResult> getOrderViewResult() {
        return orderViewResult;
    }

    public void setOrderViewResult(List<OrderViewResult> orderViewResult) {
        this.orderViewResult = orderViewResult;
    }

}
