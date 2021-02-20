package com.harperskebab.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Food implements Serializable, Parcelable {

    @SerializedName("additives_Description")
    private String additivesDescription;
    @SerializedName("Dish_Ingredients")
    private String dishIngredients;
    @SerializedName("error")
    private String error;
    @SerializedName("error_msg")
    private String errorMsg;
    @SerializedName("extraavailable")
    private String extraavailable;
    @SerializedName("food_Icon")
    private String foodIcon;
    @SerializedName("Food_NameNo")
    private String foodNameNo;
    @SerializedName("Food_Popular")
    private String foodPopular;
    @SerializedName("Food_Spicy")
    private String foodSpicy;
    @SerializedName("Food_Type")
    private String foodType;
    @SerializedName("Food_Type_Non")
    private String foodTypeNon;
    @SerializedName("GreenFood_Spicy")
    private String greenFoodSpicy;
    @SerializedName("ItemID")
    private Long itemID;
    @SerializedName("MidFood_Spicy")
    private String midFoodSpicy;
    @SerializedName("ResPizzaDescription")
    private String resPizzaDescription;
    @SerializedName("RestaurantCategoryID")
    private String restaurantCategoryID;
    @SerializedName("RestaurantPizzaID")
    private Long restaurantPizzaID;
    @SerializedName("RestaurantPizzaItemName")
    private String restaurantPizzaItemName;
    @SerializedName("RestaurantPizzaItemOldPrice")
    private String restaurantPizzaItemOldPrice;
    @SerializedName("RestaurantPizzaItemPrice")
    private String restaurantPizzaItemPrice;
    @SerializedName("RestaurantPizzaItemSizeName")
    private String restaurantPizzaItemSizeName;
    @SerializedName("sizeavailable")
    private String sizeavailable;
    @SerializedName("VeryFood_Spicy")
    private String veryFoodSpicy;
    @SerializedName("food_tax_applicable")
    private String foodTaxApplicable;

    private int foodCount = 0;
    private FoodItem foodItem;
    private List<FoodItemExtraTopping> foodItemExtraToppings;
    private boolean isOrderSendToKitchen = false;
    private int foodCountLimit=0;
public Food(){

}
    protected Food(Parcel in) {
        additivesDescription = in.readString();
        dishIngredients = in.readString();
        error = in.readString();
        errorMsg = in.readString();
        extraavailable = in.readString();
        foodIcon = in.readString();
        foodNameNo = in.readString();
        foodPopular = in.readString();
        foodSpicy = in.readString();
        foodType = in.readString();
        foodTypeNon = in.readString();
        greenFoodSpicy = in.readString();
        if (in.readByte() == 0) {
            itemID = null;
        } else {
            itemID = in.readLong();
        }
        midFoodSpicy = in.readString();
        resPizzaDescription = in.readString();
        restaurantCategoryID = in.readString();
        if (in.readByte() == 0) {
            restaurantPizzaID = null;
        } else {
            restaurantPizzaID = in.readLong();
        }
        restaurantPizzaItemName = in.readString();
        restaurantPizzaItemOldPrice = in.readString();
        restaurantPizzaItemPrice = in.readString();
        restaurantPizzaItemSizeName = in.readString();
        sizeavailable = in.readString();
        veryFoodSpicy = in.readString();
        foodTaxApplicable = in.readString();
        foodCount = in.readInt();
        isOrderSendToKitchen = in.readByte() != 0;
        foodCountLimit = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getAdditivesDescription() {
        return additivesDescription;
    }

    public void setAdditivesDescription(String additivesDescription) {
        this.additivesDescription = additivesDescription;
    }

    public String getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(String dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

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

    public String getExtraavailable() {
        return extraavailable;
    }

    public void setExtraavailable(String extraavailable) {
        this.extraavailable = extraavailable;
    }

    public String getFoodIcon() {
        return foodIcon;
    }

    public void setFoodIcon(String foodIcon) {
        this.foodIcon = foodIcon;
    }

    public String getFoodNameNo() {
        return foodNameNo;
    }

    public void setFoodNameNo(String foodNameNo) {
        this.foodNameNo = foodNameNo;
    }

    public String getFoodPopular() {
        return foodPopular;
    }

    public void setFoodPopular(String foodPopular) {
        this.foodPopular = foodPopular;
    }

    public String getFoodSpicy() {
        return foodSpicy;
    }

    public void setFoodSpicy(String foodSpicy) {
        this.foodSpicy = foodSpicy;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodTypeNon() {
        return foodTypeNon;
    }

    public void setFoodTypeNon(String foodTypeNon) {
        this.foodTypeNon = foodTypeNon;
    }

    public String getGreenFoodSpicy() {
        return greenFoodSpicy;
    }

    public void setGreenFoodSpicy(String greenFoodSpicy) {
        this.greenFoodSpicy = greenFoodSpicy;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public String getMidFoodSpicy() {
        return midFoodSpicy;
    }

    public void setMidFoodSpicy(String midFoodSpicy) {
        this.midFoodSpicy = midFoodSpicy;
    }

    public String getResPizzaDescription() {
        return resPizzaDescription;
    }

    public void setResPizzaDescription(String resPizzaDescription) {
        this.resPizzaDescription = resPizzaDescription;
    }

    public String getRestaurantCategoryID() {
        return restaurantCategoryID;
    }

    public void setRestaurantCategoryID(String restaurantCategoryID) {
        this.restaurantCategoryID = restaurantCategoryID;
    }

    public Long getRestaurantPizzaID() {
        return restaurantPizzaID;
    }

    public void setRestaurantPizzaID(Long restaurantPizzaID) {
        this.restaurantPizzaID = restaurantPizzaID;
    }

    public String getRestaurantPizzaItemName() {
        return restaurantPizzaItemName;
    }

    public void setRestaurantPizzaItemName(String restaurantPizzaItemName) {
        this.restaurantPizzaItemName = restaurantPizzaItemName;
    }

    public String getRestaurantPizzaItemOldPrice() {
        return restaurantPizzaItemOldPrice;
    }

    public void setRestaurantPizzaItemOldPrice(String restaurantPizzaItemOldPrice) {
        this.restaurantPizzaItemOldPrice = restaurantPizzaItemOldPrice;
    }

    public String getRestaurantPizzaItemPrice() {
        return restaurantPizzaItemPrice;
    }

    public void setRestaurantPizzaItemPrice(String restaurantPizzaItemPrice) {
        this.restaurantPizzaItemPrice = restaurantPizzaItemPrice;
    }

    public String getRestaurantPizzaItemSizeName() {
        return restaurantPizzaItemSizeName;
    }

    public void setRestaurantPizzaItemSizeName(String restaurantPizzaItemSizeName) {
        this.restaurantPizzaItemSizeName = restaurantPizzaItemSizeName;
    }

    public String getSizeavailable() {
        return sizeavailable;
    }

    public void setSizeavailable(String sizeavailable) {
        this.sizeavailable = sizeavailable;
    }

    public String getVeryFoodSpicy() {
        return veryFoodSpicy;
    }

    public void setVeryFoodSpicy(String veryFoodSpicy) {
        this.veryFoodSpicy = veryFoodSpicy;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public List<FoodItemExtraTopping> getFoodItemExtraToppings() {
        return foodItemExtraToppings;
    }

    public void setFoodItemExtraToppings(List<FoodItemExtraTopping> foodItemExtraToppings) {
        this.foodItemExtraToppings = foodItemExtraToppings;
    }

    public String getFoodTaxApplicable() {
        return foodTaxApplicable;
    }

    public void setFoodTaxApplicable(String foodTaxApplicable) {
        this.foodTaxApplicable = foodTaxApplicable;
    }

    public boolean isOrderSendToKitchen() {
        return isOrderSendToKitchen;
    }

    public void setOrderSendToKitchen(boolean orderSendToKitchen) {
        isOrderSendToKitchen = orderSendToKitchen;
    }

    public int getFoodCountLimit() {
        return foodCountLimit;
    }

    public void setFoodCountLimit(int foodCountLimit) {
        this.foodCountLimit = foodCountLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return getItemID().equals(food.getItemID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemID());
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(additivesDescription);
        dest.writeString(dishIngredients);
        dest.writeString(error);
        dest.writeString(errorMsg);
        dest.writeString(extraavailable);
        dest.writeString(foodIcon);
        dest.writeString(foodNameNo);
        dest.writeString(foodPopular);
        dest.writeString(foodSpicy);
        dest.writeString(foodType);
        dest.writeString(foodTypeNon);
        dest.writeString(greenFoodSpicy);
        if (itemID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(itemID);
        }
        dest.writeString(midFoodSpicy);
        dest.writeString(resPizzaDescription);
        dest.writeString(restaurantCategoryID);
        if (restaurantPizzaID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(restaurantPizzaID);
        }
        dest.writeString(restaurantPizzaItemName);
        dest.writeString(restaurantPizzaItemOldPrice);
        dest.writeString(restaurantPizzaItemPrice);
        dest.writeString(restaurantPizzaItemSizeName);
        dest.writeString(sizeavailable);
        dest.writeString(veryFoodSpicy);
        dest.writeString(foodTaxApplicable);
        dest.writeInt(foodCount);
        dest.writeByte((byte) (isOrderSendToKitchen ? 1 : 0));
        dest.writeInt(foodCountLimit);
    }
}
