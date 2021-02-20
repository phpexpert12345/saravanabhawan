package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.BranchViewListItemRowBinding;

public class BranchViewViewHolder extends RecyclerView.ViewHolder {

    private BranchViewListItemRowBinding binding;

    public BranchViewViewHolder(BranchViewListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public BranchViewListItemRowBinding getBinding() {
        return binding;
    }
}
