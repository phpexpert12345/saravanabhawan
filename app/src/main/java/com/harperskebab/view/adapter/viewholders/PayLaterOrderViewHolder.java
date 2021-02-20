package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.PayLaterOrderListItemRowBinding;

public class PayLaterOrderViewHolder extends RecyclerView.ViewHolder {

    private PayLaterOrderListItemRowBinding binding;

    public PayLaterOrderViewHolder(PayLaterOrderListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public PayLaterOrderListItemRowBinding getBinding() {
        return binding;
    }
}
