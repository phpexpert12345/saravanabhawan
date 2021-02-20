
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.RestaurantReview;

import java.util.List;

public class RmGetRestaurantReviewResponse {

    @SerializedName("review")
    private Review review;
    @SerializedName("success")
    private Long success;

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public class Review {

        @SerializedName("RestaurantReviews")
        private List<RestaurantReview> restaurantReviews;

        public List<RestaurantReview> getRestaurantReviews() {
            return restaurantReviews;
        }

        public void setRestaurantReviews(List<RestaurantReview> restaurantReviews) {
            this.restaurantReviews = restaurantReviews;
        }

    }

}
