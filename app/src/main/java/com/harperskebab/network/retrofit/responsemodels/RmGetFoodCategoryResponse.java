package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.FoodCategory;

import java.util.List;

public class RmGetFoodCategoryResponse {

    @SerializedName("RestaurantMencategory")
    private List<FoodCategory> foodCategory;

    public List<FoodCategory> getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(List<FoodCategory> foodCategory) {
        this.foodCategory = foodCategory;
    }

}
