
package com.harperskebab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReOrderFoodItem {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("ItemsName")
    @Expose
    private String itemsName;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("RestaurantCategoryID")
    @Expose
    private String restaurantCategoryID;
    @SerializedName("item_size")
    @Expose
    private String itemSize;
    @SerializedName("FoodItemSizeID")
    @Expose
    private String foodItemSizeID;
    @SerializedName("FoodItemID")
    @Expose
    private String foodItemID;
    @SerializedName("Extraprice")
    @Expose
    private String Extraprice;
    @SerializedName("ExtraTopping")
    @Expose
    private String extraTopping;
    @SerializedName("ExtraToppingID")
    @Expose
    private String extraToppingID;
    @SerializedName("RestaurantPizzaItemName")
    @Expose
    private String restaurantPizzaItemName;
    @SerializedName("RestaurantPizzaItemOldPrice")
    @Expose
    private String restaurantPizzaItemOldPrice;
    @SerializedName("RestaurantPizzaItemPrice")
    @Expose
    private String restaurantPizzaItemPrice;
    @SerializedName("RestaurantPizzaItemSizeName")
    @Expose
    private String restaurantPizzaItemSizeName;
    @SerializedName("sizeavailable")
    @Expose
    private String sizeavailable;
    @SerializedName("food_tax_applicable")
    @Expose
    private String foodTaxApplicable;
    @SerializedName("ResPizzaDescription")
    @Expose
    private String resPizzaDescription;
    @SerializedName("Food_NameNo")
    @Expose
    private String foodNameNo;
    @SerializedName("additives_Description")
    @Expose
    private String additivesDescription;
    @SerializedName("Dish_Ingredients")
    @Expose
    private String dishIngredients;
    @SerializedName("Food_Type")
    @Expose
    private String foodType;
    @SerializedName("Food_Type_Non")
    @Expose
    private String foodTypeNon;
    @SerializedName("Food_Popular")
    @Expose
    private String foodPopular;
    @SerializedName("Food_Spicy")
    @Expose
    private String foodSpicy;
    @SerializedName("MidFood_Spicy")
    @Expose
    private String midFoodSpicy;
    @SerializedName("VeryFood_Spicy")
    @Expose
    private String veryFoodSpicy;
    @SerializedName("GreenFood_Spicy")
    @Expose
    private String greenFoodSpicy;
    @SerializedName("food_Icon")
    @Expose
    private String foodIcon;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("menuprice")
    @Expose
    private String menuprice;

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

    public String getItemsName() {
        return itemsName;
    }

    public void setItemsName(String itemsName) {
        this.itemsName = itemsName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantCategoryID() {
        return restaurantCategoryID;
    }

    public void setRestaurantCategoryID(String restaurantCategoryID) {
        this.restaurantCategoryID = restaurantCategoryID;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getFoodItemSizeID() {
        return foodItemSizeID;
    }

    public void setFoodItemSizeID(String foodItemSizeID) {
        this.foodItemSizeID = foodItemSizeID;
    }

    public String getFoodItemID() {
        return foodItemID;
    }

    public void setFoodItemID(String foodItemID) {
        this.foodItemID = foodItemID;
    }

    public String getExtraTopping() {
        return extraTopping;
    }

    public void setExtraTopping(String extraTopping) {
        this.extraTopping = extraTopping;
    }

    public String getExtraToppingID() {
        return extraToppingID;
    }

    public void setExtraToppingID(String extraToppingID) {
        this.extraToppingID = extraToppingID;
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

    public String getExtraprice() {
        return Extraprice;
    }

    public void setExtraprice(String extraprice) {
        Extraprice = extraprice;
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

    public String getFoodTaxApplicable() {
        return foodTaxApplicable;
    }

    public void setFoodTaxApplicable(String foodTaxApplicable) {
        this.foodTaxApplicable = foodTaxApplicable;
    }

    public String getResPizzaDescription() {
        return resPizzaDescription;
    }

    public void setResPizzaDescription(String resPizzaDescription) {
        this.resPizzaDescription = resPizzaDescription;
    }

    public String getFoodNameNo() {
        return foodNameNo;
    }

    public void setFoodNameNo(String foodNameNo) {
        this.foodNameNo = foodNameNo;
    }

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

    public String getMidFoodSpicy() {
        return midFoodSpicy;
    }

    public void setMidFoodSpicy(String midFoodSpicy) {
        this.midFoodSpicy = midFoodSpicy;
    }

    public String getVeryFoodSpicy() {
        return veryFoodSpicy;
    }

    public void setVeryFoodSpicy(String veryFoodSpicy) {
        this.veryFoodSpicy = veryFoodSpicy;
    }

    public String getGreenFoodSpicy() {
        return greenFoodSpicy;
    }

    public void setGreenFoodSpicy(String greenFoodSpicy) {
        this.greenFoodSpicy = greenFoodSpicy;
    }

    public String getFoodIcon() {
        return foodIcon;
    }

    public void setFoodIcon(String foodIcon) {
        this.foodIcon = foodIcon;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMenuprice() {
        return menuprice;
    }

    public void setMenuprice(String menuprice) {
        this.menuprice = menuprice;
    }

}