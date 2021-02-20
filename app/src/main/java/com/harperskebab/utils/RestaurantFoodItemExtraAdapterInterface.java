package com.harperskebab.utils;

import com.harperskebab.model.FoodItemExtraTopping;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantFoodItemExtraAdapterInterface {
    void getTop(int pos,FoodItemExtraTopping foodItemExtraTopping);
    void getCheckedItem(String item, String price, long id, List<FoodItemExtraTopping> list);
}
