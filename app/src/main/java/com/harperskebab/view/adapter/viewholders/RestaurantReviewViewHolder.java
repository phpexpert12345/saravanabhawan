package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.RestaurantReviewListItemRowBinding;

public class RestaurantReviewViewHolder extends RecyclerView.ViewHolder {

    private RestaurantReviewListItemRowBinding binding;

    public RestaurantReviewViewHolder(RestaurantReviewListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public RestaurantReviewListItemRowBinding getBinding() {
        return binding;
    }
}
