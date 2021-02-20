package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodListItemRowBinding;
import com.harperskebab.databinding.OldFoodListItemRowBinding;

public class OldFoodViewHolder extends RecyclerView.ViewHolder {

    private OldFoodListItemRowBinding binding;

    public OldFoodViewHolder(OldFoodListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public OldFoodListItemRowBinding getBinding() {
        return binding;
    }
}
