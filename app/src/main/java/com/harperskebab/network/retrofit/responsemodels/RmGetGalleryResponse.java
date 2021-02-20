
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.FoodGallery;

import java.util.List;

public class RmGetGalleryResponse {

    @SerializedName("FoodGalleryList")
    private List<List<FoodGallery>> foodGalleryList;

    public List<List<FoodGallery>> getFoodGalleryList() {
        return foodGalleryList;
    }

    public void setFoodGalleryList(List<List<FoodGallery>> foodGalleryList) {
        this.foodGalleryList = foodGalleryList;
    }

}
