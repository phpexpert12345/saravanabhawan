
package com.harperskebab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderFoodItem {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("FoodItemID")
    @Expose
    private Long FoodItemID;
    @SerializedName("ItemsName")
    @Expose
    private String itemsName;
    @SerializedName("item_size")
    @Expose
    private String itemSize;
    @SerializedName("ExtraTopping")
    @Expose
    private String extraTopping;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("menuprice")
    @Expose
    private String menuprice;

    public String getFood_Icon() {
        return food_Icon;
    }

    public void setFood_Icon(String food_Icon) {
        this.food_Icon = food_Icon;
    }

    @SerializedName("food_Icon")
    private String food_Icon;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getItemsName() {
        return itemsName;
    }

    public void setItemsName(String itemsName) {
        this.itemsName = itemsName;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getExtraTopping() {
        return extraTopping;
    }

    public void setExtraTopping(String extraTopping) {
        this.extraTopping = extraTopping;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMenuprice() {
        return menuprice;
    }

    public void setMenuprice(String menuprice) {
        this.menuprice = menuprice;
    }

    public Long getFoodItemID() {
        return FoodItemID;
    }

    public void setFoodItemID(Long foodItemID) {
        FoodItemID = foodItemID;
    }
}