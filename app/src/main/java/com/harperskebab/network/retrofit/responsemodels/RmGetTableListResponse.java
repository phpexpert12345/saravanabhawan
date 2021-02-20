package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.Table;

import java.util.List;

public class RmGetTableListResponse {

    @SerializedName("TableListList")
    private List<Table> table;

    public List<Table> getTable() {
        return table;
    }

    public void setTable(List<Table> table) {
        this.table = table;
    }

}
