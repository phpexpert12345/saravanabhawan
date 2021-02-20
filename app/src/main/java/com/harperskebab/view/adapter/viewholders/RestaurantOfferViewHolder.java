package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.OfferListItemRowBinding;

public class RestaurantOfferViewHolder extends RecyclerView.ViewHolder {

    private OfferListItemRowBinding binding;

    public RestaurantOfferViewHolder(OfferListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public OfferListItemRowBinding getBinding() {
        return binding;
    }
}
