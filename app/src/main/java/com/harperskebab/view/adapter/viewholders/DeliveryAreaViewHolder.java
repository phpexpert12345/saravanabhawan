package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.DeliveryAreaListItemRowBinding;

public class DeliveryAreaViewHolder extends RecyclerView.ViewHolder {

    private DeliveryAreaListItemRowBinding binding;

    public DeliveryAreaViewHolder(DeliveryAreaListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public DeliveryAreaListItemRowBinding getBinding() {
        return binding;
    }
}
