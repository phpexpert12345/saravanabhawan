
package com.harperskebab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReOrderItem {
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("OrderFoodItem")
    @Expose
    private List<ReOrderFoodItem> orderFoodItem = null;
//    @SerializedName("OrderMealItem")
//    @Expose
//    private List<Object> orderMealItem = null;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }
    public List<ReOrderFoodItem> getOrderFoodItem() {
        return orderFoodItem;
    }

    public void setOrderFoodItem(List<ReOrderFoodItem> orderFoodItem) {
        this.orderFoodItem = orderFoodItem;
    }

//    public List<Object> getOrderMealItem() {
//        return orderMealItem;
//    }
//
//    public void setOrderMealItem(List<Object> orderMealItem) {
//        this.orderMealItem = orderMealItem;
//    }

}