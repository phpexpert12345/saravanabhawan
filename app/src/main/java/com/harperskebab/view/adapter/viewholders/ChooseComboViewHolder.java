package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.ChooseComboItemBinding;

import org.jetbrains.annotations.NotNull;

public class ChooseComboViewHolder extends RecyclerView.ViewHolder {

    private ChooseComboItemBinding binding;

    public ChooseComboViewHolder(@NotNull ChooseComboItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ChooseComboItemBinding getBinding() {
        return binding;
    }
}
