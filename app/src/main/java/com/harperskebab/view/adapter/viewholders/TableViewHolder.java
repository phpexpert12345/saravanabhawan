package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.TableListItemRowBinding;

public class TableViewHolder extends RecyclerView.ViewHolder {

    private TableListItemRowBinding binding;

    public TableViewHolder(TableListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public TableListItemRowBinding getBinding() {
        return binding;
    }
}
