package com.harperskebab.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class RestaurantBranchAfterShort {

    @SerializedName("Branch_delivery_distance")
    private String branchDeliveryDistance;
    @SerializedName("branch_latitude")
    private String branchLatitude;
    @SerializedName("branch_longitude")
    private String branchLongitude;
    @SerializedName("Branch_Mobile")
    private String branchMobile;
    @SerializedName("error")
    private String error;
    @SerializedName("id")
    private Long id;
    @SerializedName("RestaurantBranch_Address")
    private String restaurantBranchAddress;
    @SerializedName("RestaurantBranchName")
    private String restaurantBranchName;
    @SerializedName("RestaurantBranchZipCode")
    private String restaurantBranchZipCode;
    @SerializedName("distance")
    private double distance;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBranchDeliveryDistance() {
        return branchDeliveryDistance;
    }

    public void setBranchDeliveryDistance(String branchDeliveryDistance) {
        this.branchDeliveryDistance = branchDeliveryDistance;
    }

    public String getBranchLatitude() {
        return branchLatitude;
    }

    public void setBranchLatitude(String branchLatitude) {
        this.branchLatitude = branchLatitude;
    }

    public String getBranchLongitude() {
        return branchLongitude;
    }

    public void setBranchLongitude(String branchLongitude) {
        this.branchLongitude = branchLongitude;
    }

    public String getBranchMobile() {
        return branchMobile;
    }

    public void setBranchMobile(String branchMobile) {
        this.branchMobile = branchMobile;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurantBranchAddress() {
        return restaurantBranchAddress;
    }

    public void setRestaurantBranchAddress(String restaurantBranchAddress) {
        this.restaurantBranchAddress = restaurantBranchAddress;
    }

    public String getRestaurantBranchName() {
        return restaurantBranchName;
    }

    public void setRestaurantBranchName(String restaurantBranchName) {
        this.restaurantBranchName = restaurantBranchName;
    }

    public String getRestaurantBranchZipCode() {
        return restaurantBranchZipCode;
    }

    public void setRestaurantBranchZipCode(String restaurantBranchZipCode) {
        this.restaurantBranchZipCode = restaurantBranchZipCode;
    }



    public static Comparator<RestaurantBranchAfterShort> shortDistance = new Comparator<RestaurantBranchAfterShort>() {

        public int compare(RestaurantBranchAfterShort s1, RestaurantBranchAfterShort s2) {

            int rollno1 = Integer.parseInt(String.valueOf(s1.getDistance()));
            int rollno2 = Integer.parseInt(String.valueOf(s2.getDistance()));

            /*For ascending order*/
            return rollno1-rollno2;

            /*For descending order*/
            //rollno2-rollno1;
        }};

}
