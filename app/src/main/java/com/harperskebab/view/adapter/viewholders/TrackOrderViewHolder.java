package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.OrderTrackListItemRowBinding;

public class TrackOrderViewHolder extends RecyclerView.ViewHolder {

    private OrderTrackListItemRowBinding binding;

    public TrackOrderViewHolder(OrderTrackListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public OrderTrackListItemRowBinding getBinding() {
        return binding;
    }
}
