package com.harperskebab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuItemExtraGroup {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("Group_ID")
    @Expose
    private Integer groupID;
    @SerializedName("Food_Group_Name")
    @Expose
    private String foodGroupName;
    @SerializedName("Food_addons_selection_Type")
    @Expose
    private String foodAddonsSelectionType;
    @SerializedName("subExtraItemsRecord")
    @Expose
    private List<SubExtraItemsRecord> subExtraItemsRecord = null;
    private final static long serialVersionUID = 4178274163808780544L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getFoodGroupName() {
        return foodGroupName;
    }

    public void setFoodGroupName(String foodGroupName) {
        this.foodGroupName = foodGroupName;
    }

    public String getFoodAddonsSelectionType() {
        return foodAddonsSelectionType;
    }

    public void setFoodAddonsSelectionType(String foodAddonsSelectionType) {
        this.foodAddonsSelectionType = foodAddonsSelectionType;
    }

    public List<SubExtraItemsRecord> getSubExtraItemsRecord() {
        return subExtraItemsRecord;
    }

    public void setSubExtraItemsRecord(List<SubExtraItemsRecord> subExtraItemsRecord) {
        this.subExtraItemsRecord = subExtraItemsRecord;
    }

}
