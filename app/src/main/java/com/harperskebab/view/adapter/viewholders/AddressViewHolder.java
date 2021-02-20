package com.harperskebab.view.adapter.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.AddressListItemRowBinding;

public class AddressViewHolder extends RecyclerView.ViewHolder {

    private AddressListItemRowBinding binding;

    public AddressViewHolder(AddressListItemRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public AddressListItemRowBinding getBinding() {
        return binding;
    }
}
