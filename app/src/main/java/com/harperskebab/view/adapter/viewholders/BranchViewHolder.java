package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.BranchListItemRowBinding;

public class BranchViewHolder extends RecyclerView.ViewHolder {

    private BranchListItemRowBinding binding;

    public BranchViewHolder(BranchListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public BranchListItemRowBinding getBinding() {
        return binding;
    }
}
