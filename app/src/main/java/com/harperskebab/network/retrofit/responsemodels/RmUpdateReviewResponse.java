
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmUpdateReviewResponse {

    @SerializedName("CustomerId")
    private String customerId;
    @SerializedName("order_identifyno")
    private String orderIdentifyno;
    @SerializedName("Quality_ratingN")
    private String qualityRatingN;
    @SerializedName("ratingAvg")
    private Long ratingAvg;
    @SerializedName("resid")
    private Long resid;
    @SerializedName("RestaurantReviewContent")
    private String restaurantReviewContent;
    @SerializedName("RestaurantReviewName")
    private String restaurantReviewName;
    @SerializedName("RestaurantReviewRating")
    private String restaurantReviewRating;
    @SerializedName("Service_ratingN")
    private String serviceRatingN;
    @SerializedName("success")
    private Long success;
    @SerializedName("success_msg")
    private String successMsg;
    @SerializedName("Time_ratingN")
    private String timeRatingN;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderIdentifyno() {
        return orderIdentifyno;
    }

    public void setOrderIdentifyno(String orderIdentifyno) {
        this.orderIdentifyno = orderIdentifyno;
    }

    public String getQualityRatingN() {
        return qualityRatingN;
    }

    public void setQualityRatingN(String qualityRatingN) {
        this.qualityRatingN = qualityRatingN;
    }

    public Long getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(Long ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public Long getResid() {
        return resid;
    }

    public void setResid(Long resid) {
        this.resid = resid;
    }

    public String getRestaurantReviewContent() {
        return restaurantReviewContent;
    }

    public void setRestaurantReviewContent(String restaurantReviewContent) {
        this.restaurantReviewContent = restaurantReviewContent;
    }

    public String getRestaurantReviewName() {
        return restaurantReviewName;
    }

    public void setRestaurantReviewName(String restaurantReviewName) {
        this.restaurantReviewName = restaurantReviewName;
    }

    public String getRestaurantReviewRating() {
        return restaurantReviewRating;
    }

    public void setRestaurantReviewRating(String restaurantReviewRating) {
        this.restaurantReviewRating = restaurantReviewRating;
    }

    public String getServiceRatingN() {
        return serviceRatingN;
    }

    public void setServiceRatingN(String serviceRatingN) {
        this.serviceRatingN = serviceRatingN;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public String getTimeRatingN() {
        return timeRatingN;
    }

    public void setTimeRatingN(String timeRatingN) {
        this.timeRatingN = timeRatingN;
    }

}
