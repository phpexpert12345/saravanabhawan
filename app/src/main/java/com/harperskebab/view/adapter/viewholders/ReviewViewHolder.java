package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.ReviewListItemRowBinding;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private ReviewListItemRowBinding binding;

    public ReviewViewHolder(ReviewListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ReviewListItemRowBinding getBinding() {
        return binding;
    }
}
