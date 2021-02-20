package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComboSectionValue {

    @SerializedName("id")
    private String id;
    @SerializedName("comboslot_id")
    private String comboslot_id;
    @SerializedName("Slot_Option_Name")
    private String Slot_Option_Name;
    @SerializedName("Free_allowed")
    private String Free_allowed;
    @SerializedName("Free_Topping_Selection_allowed")
    private String Free_Topping_Selection_allowed;
    @SerializedName("ComboSectionValueItems")
    private List<ComboSectionValueItems> ComboSectionValueItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComboslot_id() {
        return comboslot_id;
    }

    public void setComboslot_id(String comboslot_id) {
        this.comboslot_id = comboslot_id;
    }

    public String getSlot_Option_Name() {
        return Slot_Option_Name;
    }

    public void setSlot_Option_Name(String slot_Option_Name) {
        Slot_Option_Name = slot_Option_Name;
    }

    public String getFree_allowed() {
        return Free_allowed;
    }

    public void setFree_allowed(String free_allowed) {
        Free_allowed = free_allowed;
    }

    public String getFree_Topping_Selection_allowed() {
        return Free_Topping_Selection_allowed;
    }

    public void setFree_Topping_Selection_allowed(String free_Topping_Selection_allowed) {
        Free_Topping_Selection_allowed = free_Topping_Selection_allowed;
    }

    public List<ComboSectionValueItems> getComboSectionValueItems() {
        return ComboSectionValueItems;
    }

    public void setComboSectionValueItems(List<ComboSectionValueItems> comboSectionValueItems) {
        ComboSectionValueItems = comboSectionValueItems;
    }
}
