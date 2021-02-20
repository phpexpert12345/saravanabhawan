
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.Order;

import java.util.List;

public class RmGetOrderListResponse {

    @SerializedName("orders")
    private Orders orders;
    @SerializedName("success")
    private Long success;
    @SerializedName("success_msg")
    private String success_msg;
    @SerializedName("Table_Booking_Number")
    private String tableBookingNumber;

    public Orders getOrders() {
        return orders;
    }

    public String getSuccess_msg() {
        return success_msg;
    }

    public void setSuccess_msg(String success_msg) {
        this.success_msg = success_msg;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public String getTableBookingNumber() {
        return tableBookingNumber;
    }

    public void setTableBookingNumber(String tableBookingNumber) {
        this.tableBookingNumber = tableBookingNumber;
    }

    public class Orders {

        @SerializedName("OrderViewResult")
        private List<Order> order;

        public List<Order> getOrder() {
            return order;
        }

        public void setOrder(List<Order> order) {
            this.order = order;
        }

    }

}
