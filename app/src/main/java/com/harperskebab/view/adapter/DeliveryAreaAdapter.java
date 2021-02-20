package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.DeliveryAreaListItemRowBinding;
import com.harperskebab.model.DeliveryArea;
import com.harperskebab.view.adapter.viewholders.DeliveryAreaViewHolder;

import java.util.List;

public class DeliveryAreaAdapter extends RecyclerView.Adapter<DeliveryAreaViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<DeliveryArea> deliveryAreas;
    private String currencySymbol;


    public DeliveryAreaAdapter(Context context, List<DeliveryArea> deliveryAreas, String currencySymbol) {
        this.context = context;
        this.deliveryAreas = deliveryAreas;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public DeliveryAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeliveryAreaViewHolder(DeliveryAreaListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryAreaViewHolder holder, int position) {
        DeliveryArea deliveryArea = deliveryAreas.get(position);

        try {
            holder.getBinding().textViewAreaName.setText(deliveryArea.getAdminDistrict());
            holder.getBinding().textViewPostalCode.setText(deliveryArea.getPostcode());
            holder.getBinding().textViewMinAmount.setText("Min. Amount: " + currencySymbol + " " + deliveryArea.getMinimumOrder());
            holder.getBinding().textViewDeliveryCharge.setText("Delivery Charge: " + currencySymbol + " " + deliveryArea.getDeliveryCharge());

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return deliveryAreas.size();
    }

}
