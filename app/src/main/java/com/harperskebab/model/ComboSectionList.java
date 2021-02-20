package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComboSectionList {

    @SerializedName("id")
    private String id;
    @SerializedName("deal_slot_name")
    private String deal_slot_name;
    @SerializedName("ComboSectionValue")
    private List<ComboSectionValue> ComboSectionValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeal_slot_name() {
        return deal_slot_name;
    }

    public void setDeal_slot_name(String deal_slot_name) {
        this.deal_slot_name = deal_slot_name;
    }

    public List<ComboSectionValue> getComboSectionValue() {
        return ComboSectionValue;
    }

    public void setComboSectionValue(List<ComboSectionValue> comboSectionValue) {
        ComboSectionValue = comboSectionValue;
    }
}
