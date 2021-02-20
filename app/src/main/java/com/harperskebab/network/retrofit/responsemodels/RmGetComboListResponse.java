package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.CardList;
import com.harperskebab.model.ComboList;

import java.util.List;

public class RmGetComboListResponse {
    @SerializedName("ComboList")
    private List<ComboList> comboLists;


    public List<ComboList> getComboList() {
        return comboLists;
    }

    public void setComboList(List<ComboList> comboList) {
        this.comboLists = comboList;
    }
}
