package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComboList {

    @SerializedName("id")
    private String id;
    @SerializedName("maximum_allow")
    private String maximum_allow;
    @SerializedName("RestaurantPizzaItemOldPrice")
    private String restaurantPizzaItemOldPrice;
    @SerializedName("RestaurantPizzaItemPrice")
    private String restaurantPizzaItemPrice;
    @SerializedName("restaurant_id")
    private String restaurant_id;
    @SerializedName("dealID")
    private String dealID;
    @SerializedName("combo_dealid")
    private String combo_dealid;
    @SerializedName("Combo_NameNo")
    private String Combo_NameNo;
    @SerializedName("deal_name")
    private String deal_name;
    @SerializedName("deal_description")
    private String deal_description;
    @SerializedName("ComboSectionList")
    private List<ComboSectionList> ComboSectionList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaximum_allow() {
        return maximum_allow;
    }

    public void setMaximum_allow(String maximum_allow) {
        this.maximum_allow = maximum_allow;
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

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getDealID() {
        return dealID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }

    public String getCombo_dealid() {
        return combo_dealid;
    }

    public void setCombo_dealid(String combo_dealid) {
        this.combo_dealid = combo_dealid;
    }

    public String getCombo_NameNo() {
        return Combo_NameNo;
    }

    public void setCombo_NameNo(String combo_NameNo) {
        Combo_NameNo = combo_NameNo;
    }

    public String getDeal_name() {
        return deal_name;
    }

    public void setDeal_name(String deal_name) {
        this.deal_name = deal_name;
    }

    public String getDeal_description() {
        return deal_description;
    }

    public void setDeal_description(String deal_description) {
        this.deal_description = deal_description;
    }

    public List<ComboSectionList> getComboSectionList() {
        return ComboSectionList;
    }

    public void setComboSectionList(List<ComboSectionList> comboSectionList) {
        ComboSectionList = comboSectionList;
    }
}
