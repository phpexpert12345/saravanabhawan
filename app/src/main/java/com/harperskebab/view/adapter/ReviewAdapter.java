package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.ReviewListItemRowBinding;
import com.harperskebab.model.RestaurantReview;
import com.harperskebab.view.adapter.viewholders.ReviewViewHolder;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private static final String TAG = "ReviewAdapter";

    private Context context;
    private List<RestaurantReview> restaurantReviews;

    public ReviewAdapter(Context context, List<RestaurantReview> restaurantReviews) {
        this.context = context;
        this.restaurantReviews = restaurantReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(ReviewListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        RestaurantReview restaurantReview = restaurantReviews.get(position);

        try {

            holder.getBinding().textViewOrderID.setText("Your Order ID: " + restaurantReview.getOrderNumber());
            holder.getBinding().textViewReviewTime.setText(restaurantReview.getReviewpostedDate());
            holder.getBinding().ratingBar.setProgress(Integer.parseInt(restaurantReview.getRating()));
            holder.getBinding().textViewReviewComment.setText(restaurantReview.getCustomerReviewComment());

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return restaurantReviews.size();
    }

}
