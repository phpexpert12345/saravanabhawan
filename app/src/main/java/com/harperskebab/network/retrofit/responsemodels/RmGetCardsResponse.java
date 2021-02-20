package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.Address;
import com.harperskebab.model.CardList;
import com.harperskebab.model.CurrentLocation;
import com.harperskebab.model.Deliveryaddress;

import java.util.List;

public class RmGetCardsResponse {
    @SerializedName("CreditCardList")
    private List<CardList> creditCardList;


    public List<CardList> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<CardList> creditCardList) {
        this.creditCardList = creditCardList;
    }
}
