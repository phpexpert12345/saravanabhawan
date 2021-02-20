package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.OrderListItemRowBinding;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private OrderListItemRowBinding binding;

    public OrderViewHolder(OrderListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public OrderListItemRowBinding getBinding() {
        return binding;
    }
}
