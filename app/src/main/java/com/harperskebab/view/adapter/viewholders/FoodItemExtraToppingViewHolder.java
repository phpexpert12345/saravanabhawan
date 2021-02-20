package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodItemExtraToppingListItemRowBinding;

public class FoodItemExtraToppingViewHolder extends RecyclerView.ViewHolder {

    private FoodItemExtraToppingListItemRowBinding binding;

    public FoodItemExtraToppingViewHolder(FoodItemExtraToppingListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public FoodItemExtraToppingListItemRowBinding getBinding() {
        return binding;
    }
}
