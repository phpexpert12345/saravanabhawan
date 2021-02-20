package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodGalleryListItemRowBinding;

public class FoodGalleryViewHolder extends RecyclerView.ViewHolder {

    private FoodGalleryListItemRowBinding binding;

    public FoodGalleryViewHolder(FoodGalleryListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public FoodGalleryListItemRowBinding getBinding() {
        return binding;
    }
}
