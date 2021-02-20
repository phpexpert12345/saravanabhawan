package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodCategoryHorizontalItemRowBinding;

public class FoodCategorHorizontalViewHolder extends RecyclerView.ViewHolder {

    private FoodCategoryHorizontalItemRowBinding binding;

    public FoodCategorHorizontalViewHolder(FoodCategoryHorizontalItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public FoodCategoryHorizontalItemRowBinding getBinding() {
        return binding;
    }
}
