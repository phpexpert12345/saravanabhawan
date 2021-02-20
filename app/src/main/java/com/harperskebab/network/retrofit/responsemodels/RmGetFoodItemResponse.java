package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.FoodItem;

import java.util.List;

public class RmGetFoodItemResponse {

    @SerializedName("RestaurantMenItemsSize")
    private List<FoodItem> foodItem;

    public List<FoodItem> getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(List<FoodItem> foodItem) {
        this.foodItem = foodItem;
    }

}
