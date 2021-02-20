package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

public class Deliveryaddress {

    @SerializedName("address")
    private String address;
    @SerializedName("address_direction")
    private String addressDirection;
    @SerializedName("address_title")
    private String addressTitle;
    @SerializedName("city_name")
    private String cityName;
    @SerializedName("company_street")
    private String companyStreet;
    @SerializedName("company_streetNo")
    private String companyStreetNo;
    @SerializedName("customer_address_lat")
    private String customerAddressLat;
    @SerializedName("customer_address_long")
    private String customerAddressLong;
    @SerializedName("customerCountry")
    private Object customerCountry;
    @SerializedName("customerFlat_Name")
    private String customerFlatName;
    @SerializedName("customerFloor_House_Number")
    private Object customerFloorHouseNumber;
    @SerializedName("customerState")
    private Object customerState;
    @SerializedName("id")
    private Long id;
    @SerializedName("user_locality")
    private Object userLocality;
    @SerializedName("user_phone")
    private String userPhone;
    @SerializedName("zipcode")
    private String zipcode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDirection() {
        return addressDirection;
    }

    public void setAddressDirection(String addressDirection) {
        this.addressDirection = addressDirection;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCompanyStreet() {
        return companyStreet;
    }

    public void setCompanyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
    }

    public String getCompanyStreetNo() {
        return companyStreetNo;
    }

    public void setCompanyStreetNo(String companyStreetNo) {
        this.companyStreetNo = companyStreetNo;
    }

    public String getCustomerAddressLat() {
        return customerAddressLat;
    }

    public void setCustomerAddressLat(String customerAddressLat) {
        this.customerAddressLat = customerAddressLat;
    }

    public String getCustomerAddressLong() {
        return customerAddressLong;
    }

    public void setCustomerAddressLong(String customerAddressLong) {
        this.customerAddressLong = customerAddressLong;
    }

    public Object getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(Object customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerFlatName() {
        return customerFlatName;
    }

    public void setCustomerFlatName(String customerFlatName) {
        this.customerFlatName = customerFlatName;
    }

    public Object getCustomerFloorHouseNumber() {
        return customerFloorHouseNumber;
    }

    public void setCustomerFloorHouseNumber(Object customerFloorHouseNumber) {
        this.customerFloorHouseNumber = customerFloorHouseNumber;
    }

    public Object getCustomerState() {
        return customerState;
    }

    public void setCustomerState(Object customerState) {
        this.customerState = customerState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getUserLocality() {
        return userLocality;
    }

    public void setUserLocality(Object userLocality) {
        this.userLocality = userLocality;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
