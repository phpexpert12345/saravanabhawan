package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodCategoryListItemRowBinding;

public class FoodCategoryViewHolder extends RecyclerView.ViewHolder {

    private FoodCategoryListItemRowBinding binding;

    public FoodCategoryViewHolder(FoodCategoryListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public FoodCategoryListItemRowBinding getBinding() {
        return binding;
    }
}
