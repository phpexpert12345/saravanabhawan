package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.MenuCategory;

import java.util.List;

public class RmGetFoodResponse {

    @SerializedName("Menu_Cat")
    private List<List<MenuCategory>> menuCat;

    public List<List<MenuCategory>> getMenuCat() {
        return menuCat;
    }

    public void setMenuCat(List<List<MenuCategory>> menuCat) {
        this.menuCat = menuCat;
    }

}
