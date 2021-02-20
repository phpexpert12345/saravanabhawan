package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.AddressListItemRowBinding;
import com.harperskebab.model.Deliveryaddress;
import com.harperskebab.view.adapter.viewholders.AddressViewHolder;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressViewHolder> {
    private static final String TAG = "AddressAdapter";

    private Context context;
    private List<Deliveryaddress> deliveryaddresses;
    private OnDeleteClick onDeleteClick;
    private int selectedFoodItemPosition = 0;

    public AddressAdapter(Context context, List<Deliveryaddress> deliveryaddresses, OnDeleteClick onDeleteClick) {
        this.context = context;
        this.deliveryaddresses = deliveryaddresses;
        this.onDeleteClick = onDeleteClick;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(AddressListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Deliveryaddress deliveryaddress = deliveryaddresses.get(position);

        try {
            holder.getBinding().radioButtonAddressTitle.setText(deliveryaddress.getAddressTitle());
            holder.getBinding().textViewAddress.setText(deliveryaddress.getCompanyStreetNo() + "," + deliveryaddress.getCityName() + "," + deliveryaddress.getZipcode());
            holder.getBinding().textViewContactAddress.setText("Moile Number" +" "+ deliveryaddress.getUserPhone());
            if (selectedFoodItemPosition == position) {
                holder.getBinding().radioButtonAddressTitle.setChecked(true);
            } else {
                holder.getBinding().radioButtonAddressTitle.setChecked(false);
            }

            holder.itemView.setOnClickListener(v -> {
                selectedFoodItemPosition = position;
                notifyDataSetChanged();
            });

            holder.getBinding().radioButtonAddressTitle.setOnClickListener(v -> {
                selectedFoodItemPosition = position;
                notifyDataSetChanged();
            });

            holder.getBinding().imageViewDelete.setOnClickListener(v -> {
                onDeleteClick.onClick(position, deliveryaddress);
            });

            holder.getBinding().getRoot().setOnClickListener(v -> {
                selectedFoodItemPosition = position;
                notifyDataSetChanged();
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return deliveryaddresses.size();
    }

    public Deliveryaddress getselecteddDeliveryaddresses() {
        return deliveryaddresses.get(selectedFoodItemPosition);
    }

    public void remove(Deliveryaddress deliveryAddress) {
        deliveryaddresses.remove(deliveryAddress);
    }

    public interface OnDeleteClick {
        void onClick(int position, Deliveryaddress deliveryaddress);
    }

}
