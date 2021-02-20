package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

public class ComboSectionValueItems {

    @SerializedName("id")
    private String id;
    @SerializedName("Combo_Slot_ItemID")
    private String Combo_Slot_ItemID;
    @SerializedName("Combo_Slot_ItemName")
    private String Combo_Slot_ItemName;
    @SerializedName("FoodItemSizeID")
    private String FoodItemSizeID;
    @SerializedName("ItemID")
    private String ItemID;
    @SerializedName("Combo_Topping_Allow")
    private String Combo_Topping_Allow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCombo_Slot_ItemID() {
        return Combo_Slot_ItemID;
    }

    public void setCombo_Slot_ItemID(String combo_Slot_ItemID) {
        Combo_Slot_ItemID = combo_Slot_ItemID;
    }

    public String getCombo_Slot_ItemName() {
        return Combo_Slot_ItemName;
    }

    public void setCombo_Slot_ItemName(String combo_Slot_ItemName) {
        Combo_Slot_ItemName = combo_Slot_ItemName;
    }

    public String getFoodItemSizeID() {
        return FoodItemSizeID;
    }

    public void setFoodItemSizeID(String foodItemSizeID) {
        FoodItemSizeID = foodItemSizeID;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getCombo_Topping_Allow() {
        return Combo_Topping_Allow;
    }

    public void setCombo_Topping_Allow(String combo_Topping_Allow) {
        Combo_Topping_Allow = combo_Topping_Allow;
    }
}
