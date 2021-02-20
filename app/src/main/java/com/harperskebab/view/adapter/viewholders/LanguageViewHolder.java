package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.LanguageListItemRowBinding;

public class LanguageViewHolder extends RecyclerView.ViewHolder {

    private LanguageListItemRowBinding binding;

    public LanguageViewHolder(LanguageListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public LanguageListItemRowBinding getBinding() {
        return binding;
    }
}
