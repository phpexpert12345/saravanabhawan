
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.OrderDetailItem;
import com.harperskebab.model.ReOrderItem;

import java.util.List;

public class RmGetReOrderResponse {
    @SerializedName("OrderDetailItem")
    @Expose
    private List<ReOrderItem> orderDetailItem = null;

    public List<ReOrderItem> getOrderDetailItem() {
        return orderDetailItem;
    }

    public void setOrderDetailItem(List<ReOrderItem> orderDetailItem) {
        this.orderDetailItem = orderDetailItem;
    }

}




    //old
//    @SerializedName("OrderDetailItem")
//    private List<OrderDetailItem> orderDetailItem;
//    @SerializedName("OrderFoodItem")
//    private List<OrderFoodItem> orderFoodItem;
//    @SerializedName("OrderMealItem")
//    private List<Object> orderMealItem;
//
//    public List<OrderDetailItem> getOrderDetailItem() {
//        return orderDetailItem;
//    }
//
//    public void setOrderDetailItem(List<OrderDetailItem> orderDetailItem) {
//        this.orderDetailItem = orderDetailItem;
//    }
//
//    public List<OrderFoodItem> getOrderFoodItem() {
//        return orderFoodItem;
//    }
//
//    public void setOrderFoodItem(List<OrderFoodItem> orderFoodItem) {
//        this.orderFoodItem = orderFoodItem;
//    }
//
//    public List<Object> getOrderMealItem() {
//        return orderMealItem;
//    }
//
//    public void setOrderMealItem(List<Object> orderMealItem) {
//        this.orderMealItem = orderMealItem;
//    }
//
//}
