package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.RestaurantReviewListItemRowBinding;
import com.harperskebab.model.RestaurantReview;
import com.harperskebab.view.adapter.viewholders.RestaurantReviewViewHolder;

import java.util.List;

public class RestaurantReviewAdapter extends RecyclerView.Adapter<RestaurantReviewViewHolder> {
    private static final String TAG = "ReviewAdapter";

    private Context context;
    private List<RestaurantReview> restaurantReviews;

    public RestaurantReviewAdapter(Context context, List<RestaurantReview> restaurantReviews) {
        this.context = context;
        this.restaurantReviews = restaurantReviews;
    }

    @NonNull
    @Override
    public RestaurantReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantReviewViewHolder(RestaurantReviewListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantReviewViewHolder holder, int position) {
        RestaurantReview restaurantReview = restaurantReviews.get(position);

        try {

            Glide.with(context)
                    .load(restaurantReview.getCustomerimage())
                    .placeholder(R.drawable.ic_profile)
                    .into(holder.getBinding().imageViewUser);

            holder.getBinding().textViewUserName.setText(restaurantReview.getCustomerName());
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
