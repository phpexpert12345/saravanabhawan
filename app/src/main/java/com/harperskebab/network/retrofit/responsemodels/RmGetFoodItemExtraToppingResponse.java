
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.FoodItemExtraTopping;

import java.util.List;

public class RmGetFoodItemExtraToppingResponse {

    @SerializedName("Menu_ItemExtraGroup")
    private List<List<MenuItemExtraGroup>> menuItemExtraGroup;

    public List<List<MenuItemExtraGroup>> getMenuItemExtraGroup() {
        return menuItemExtraGroup;
    }

    public void setMenuItemExtraGroup(List<List<MenuItemExtraGroup>> menuItemExtraGroup) {
        this.menuItemExtraGroup = menuItemExtraGroup;
    }

    public class MenuItemExtraGroup {

        @SerializedName("error")
        private String error;
        @SerializedName("error_msg")
        private String errorMsg;
        @SerializedName("Food_Group_Name")
        private String foodGroupName;
        @SerializedName("Group_ID")
        private Long groupID;

        public String getFreeToppingSelectionAllowed() {
            return freeToppingSelectionAllowed;
        }

        public void setFreeToppingSelectionAllowed(String freeToppingSelectionAllowed) {
            this.freeToppingSelectionAllowed = freeToppingSelectionAllowed;
        }

        @SerializedName("Free_Topping_Selection_allowed")
        private String freeToppingSelectionAllowed;

        public String getFoodAddonsSelectionType() {
            return foodAddonsSelectionType;
        }

        public void setFoodAddonsSelectionType(String foodAddonsSelectionType) {
            this.foodAddonsSelectionType = foodAddonsSelectionType;
        }

        @SerializedName("Food_addons_selection_Type")

        private String foodAddonsSelectionType;
        @SerializedName("subExtraItemsRecord")
        private List<FoodItemExtraTopping> foodItemExtraTopping;

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

        public String getFoodGroupName() {
            return foodGroupName;
        }

        public void setFoodGroupName(String foodGroupName) {
            this.foodGroupName = foodGroupName;
        }

        public Long getGroupID() {
            return groupID;
        }

        public void setGroupID(Long groupID) {
            this.groupID = groupID;
        }

        public List<FoodItemExtraTopping> getFoodItemExtraTopping() {
            return foodItemExtraTopping;
        }

        public void setFoodItemExtraTopping(List<FoodItemExtraTopping> foodItemExtraTopping) {
            this.foodItemExtraTopping = foodItemExtraTopping;
        }

    }
}
