package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodItemRowBinding;
import com.harperskebab.databinding.FoodListItemRowBinding;

public class FoodViewHolder extends RecyclerView.ViewHolder {

    private FoodListItemRowBinding binding;

    public FoodViewHolder(FoodListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public FoodListItemRowBinding getBinding() {
        return binding;
    }
}
