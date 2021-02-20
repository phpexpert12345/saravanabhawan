package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.R;
import com.harperskebab.databinding.DeliveryTimeListItemRowBinding;
import com.harperskebab.model.DeliveryTime;
import com.harperskebab.view.adapter.viewholders.DeliveryTimeViewHolder;

import java.util.List;

public class DeliveryTimeAdapter extends RecyclerView.Adapter<DeliveryTimeViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<DeliveryTime> deliveryTimes;
    private int selectedDeliveryTimePosition = 0;

    public DeliveryTimeAdapter(Context context, List<DeliveryTime> deliveryTimes) {
        this.context = context;
        this.deliveryTimes = deliveryTimes;
    }

    @NonNull
    @Override
    public DeliveryTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeliveryTimeViewHolder(DeliveryTimeListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryTimeViewHolder holder, int position) {
        DeliveryTime deliveryTime = deliveryTimes.get(position);

        try {
            holder.getBinding().textViewDeliveryTime.setText(deliveryTime.getGetTime());

            if (selectedDeliveryTimePosition == position) {
                holder.getBinding().getRoot().setBackground(ContextCompat.getDrawable(context, R.drawable.delivery_time_background));
            } else {
                holder.getBinding().getRoot().setBackground(null);
            }

            holder.itemView.setOnClickListener(v -> {
                selectedDeliveryTimePosition = position;
                notifyDataSetChanged();
            });

            holder.getBinding().getRoot().setOnClickListener(v -> {
                selectedDeliveryTimePosition = position;
                notifyDataSetChanged();
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return deliveryTimes.size();
    }

    public DeliveryTime getSelectedDeliveryTime() {
        return deliveryTimes.get(selectedDeliveryTimePosition);
    }

}
