package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.TicketListItemRowBinding;

public class TicketViewHolder extends RecyclerView.ViewHolder {

    private TicketListItemRowBinding binding;

    public TicketViewHolder(TicketListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public TicketListItemRowBinding getBinding() {
        return binding;
    }
}
