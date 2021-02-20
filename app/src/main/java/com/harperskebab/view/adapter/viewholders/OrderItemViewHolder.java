package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.OrderItemListItemRowBinding;

public class OrderItemViewHolder extends RecyclerView.ViewHolder {

    private OrderItemListItemRowBinding binding;

    public OrderItemViewHolder(OrderItemListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public OrderItemListItemRowBinding getBinding() {
        return binding;
    }
}
