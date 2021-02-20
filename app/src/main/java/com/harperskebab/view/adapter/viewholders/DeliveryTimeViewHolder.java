package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.DeliveryTimeListItemRowBinding;

public class DeliveryTimeViewHolder extends RecyclerView.ViewHolder {

    private DeliveryTimeListItemRowBinding binding;

    public DeliveryTimeViewHolder(DeliveryTimeListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public DeliveryTimeListItemRowBinding getBinding() {
        return binding;
    }
}
