
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;

public class RmGetLoyaltyPointListResponse {

    @SerializedName("birthday_celebrations_points")
    private Long birthdayCelebrationsPoints;
    @SerializedName("per_order_loyality_point")
    private Long perOrderLoyalityPoint;
    @SerializedName("place_first_orders_points")
    private Long placeFirstOrdersPoints;
    @SerializedName("place_group_ordering_points")
    private Long placeGroupOrderingPoints;
    @SerializedName("post_review_points")
    private Long postReviewPoints;
    @SerializedName("refer_friends_points")
    private Long referFriendsPoints;
    @SerializedName("sign_points")
    private Long signPoints;
    @SerializedName("social_media_sharing_points")
    private Long socialMediaSharingPoints;
    @SerializedName("spend_more_than_points")
    private Long spendMoreThanPoints;

    public Long getBirthdayCelebrationsPoints() {
        return birthdayCelebrationsPoints;
    }

    public void setBirthdayCelebrationsPoints(Long birthdayCelebrationsPoints) {
        this.birthdayCelebrationsPoints = birthdayCelebrationsPoints;
    }

    public Long getPerOrderLoyalityPoint() {
        return perOrderLoyalityPoint;
    }

    public void setPerOrderLoyalityPoint(Long perOrderLoyalityPoint) {
        this.perOrderLoyalityPoint = perOrderLoyalityPoint;
    }

    public Long getPlaceFirstOrdersPoints() {
        return placeFirstOrdersPoints;
    }

    public void setPlaceFirstOrdersPoints(Long placeFirstOrdersPoints) {
        this.placeFirstOrdersPoints = placeFirstOrdersPoints;
    }

    public Long getPlaceGroupOrderingPoints() {
        return placeGroupOrderingPoints;
    }

    public void setPlaceGroupOrderingPoints(Long placeGroupOrderingPoints) {
        this.placeGroupOrderingPoints = placeGroupOrderingPoints;
    }

    public Long getPostReviewPoints() {
        return postReviewPoints;
    }

    public void setPostReviewPoints(Long postReviewPoints) {
        this.postReviewPoints = postReviewPoints;
    }

    public Long getReferFriendsPoints() {
        return referFriendsPoints;
    }

    public void setReferFriendsPoints(Long referFriendsPoints) {
        this.referFriendsPoints = referFriendsPoints;
    }

    public Long getSignPoints() {
        return signPoints;
    }

    public void setSignPoints(Long signPoints) {
        this.signPoints = signPoints;
    }

    public Long getSocialMediaSharingPoints() {
        return socialMediaSharingPoints;
    }

    public void setSocialMediaSharingPoints(Long socialMediaSharingPoints) {
        this.socialMediaSharingPoints = socialMediaSharingPoints;
    }

    public Long getSpendMoreThanPoints() {
        return spendMoreThanPoints;
    }

    public void setSpendMoreThanPoints(Long spendMoreThanPoints) {
        this.spendMoreThanPoints = spendMoreThanPoints;
    }

}
