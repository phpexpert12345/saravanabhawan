package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodItemListItemRowBinding;

public class FoodItemViewHolder extends RecyclerView.ViewHolder {

    private FoodItemListItemRowBinding binding;

    public FoodItemViewHolder(FoodItemListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public FoodItemListItemRowBinding getBinding() {
        return binding;
    }
}
