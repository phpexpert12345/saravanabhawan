package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.ComboList;
import com.harperskebab.model.MenuItemExtraGroup;

import java.util.List;

public class RmGetComboExtraToppingListResponse {
    @SerializedName("Menu_ItemExtraGroup")
    @Expose
    private List<List<MenuItemExtraGroup>> menuItemExtraGroup = null;
    private final static long serialVersionUID = -4927574459519737887L;

    public List<List<MenuItemExtraGroup>> getMenuItemExtraGroup() {
        return menuItemExtraGroup;
    }

    public void setMenuItemExtraGroup(List<List<MenuItemExtraGroup>> menuItemExtraGroup) {
        this.menuItemExtraGroup = menuItemExtraGroup;
    }
}
