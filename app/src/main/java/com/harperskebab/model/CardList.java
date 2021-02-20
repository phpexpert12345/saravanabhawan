package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

public class CardList {

    @SerializedName("id")
    private String id;
    @SerializedName("card_number")
    private String card_number;
    @SerializedName("card_type")
    private String card_type;
    @SerializedName("card_expire_month")
    private String card_expire_month;
    @SerializedName("card_expire_year")
    private String card_expire_year;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_expire_month() {
        return card_expire_month;
    }

    public void setCard_expire_month(String card_expire_month) {
        this.card_expire_month = card_expire_month;
    }

    public String getCard_expire_year() {
        return card_expire_year;
    }

    public void setCard_expire_year(String card_expire_year) {
        this.card_expire_year = card_expire_year;
    }
}
