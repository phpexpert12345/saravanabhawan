package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Address {

    @SerializedName("current_location")
    private List<CurrentLocation> currentLocation;
    @SerializedName("deliveryaddress")
    private List<Deliveryaddress> deliveryaddress;

    public List<CurrentLocation> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(List<CurrentLocation> currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<Deliveryaddress> getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(List<Deliveryaddress> deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

}
