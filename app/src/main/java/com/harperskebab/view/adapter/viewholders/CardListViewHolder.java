package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.CardListItemRowBinding;

public class CardListViewHolder extends RecyclerView.ViewHolder {

    private CardListItemRowBinding binding;

    public CardListViewHolder(CardListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public CardListItemRowBinding getBinding() {
        return binding;
    }
}
