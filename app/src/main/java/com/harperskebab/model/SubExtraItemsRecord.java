package com.harperskebab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubExtraItemsRecord {
    @SerializedName("ExtraID")
    @Expose
    private Integer extraID;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("Food_Addons_Name")
    @Expose
    private String foodAddonsName;
    @SerializedName("Food_Price_Addons")
    @Expose
    private String foodPriceAddons;
    @SerializedName("Free_Topping_Selection_allowed")
    @Expose
    private Object freeToppingSelectionAllowed;
    private final static long serialVersionUID = 1239204515226906426L;

    public Integer getExtraID() {
        return extraID;
    }

    public void setExtraID(Integer extraID) {
        this.extraID = extraID;
    }

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

    public String getFoodAddonsName() {
        return foodAddonsName;
    }

    public void setFoodAddonsName(String foodAddonsName) {
        this.foodAddonsName = foodAddonsName;
    }

    public String getFoodPriceAddons() {
        return foodPriceAddons;
    }

    public void setFoodPriceAddons(String foodPriceAddons) {
        this.foodPriceAddons = foodPriceAddons;
    }

    public Object getFreeToppingSelectionAllowed() {
        return freeToppingSelectionAllowed;
    }

    public void setFreeToppingSelectionAllowed(Object freeToppingSelectionAllowed) {
        this.freeToppingSelectionAllowed = freeToppingSelectionAllowed;
    }

}
