package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

public class CurrentLocation {

    @SerializedName("customer_city")
    private String customerCity;
    @SerializedName("customer_country")
    private Object customerCountry;
    @SerializedName("customer_full_address")
    private Object customerFullAddress;
    @SerializedName("customer_lat")
    private String customerLat;
    @SerializedName("customer_locality")
    private String customerLocality;
    @SerializedName("customer_long")
    private String customerLong;
    @SerializedName("customer_postcode")
    private String customerPostcode;

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public Object getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(Object customerCountry) {
        this.customerCountry = customerCountry;
    }

    public Object getCustomerFullAddress() {
        return customerFullAddress;
    }

    public void setCustomerFullAddress(Object customerFullAddress) {
        this.customerFullAddress = customerFullAddress;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerLocality() {
        return customerLocality;
    }

    public void setCustomerLocality(String customerLocality) {
        this.customerLocality = customerLocality;
    }

    public String getCustomerLong() {
        return customerLong;
    }

    public void setCustomerLong(String customerLong) {
        this.customerLong = customerLong;
    }

    public String getCustomerPostcode() {
        return customerPostcode;
    }

    public void setCustomerPostcode(String customerPostcode) {
        this.customerPostcode = customerPostcode;
    }

}
