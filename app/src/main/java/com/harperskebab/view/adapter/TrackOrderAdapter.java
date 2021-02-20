package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.OrderTrackListItemRowBinding;
import com.harperskebab.model.OrderTrackHistory;
import com.harperskebab.view.adapter.viewholders.TrackOrderViewHolder;

import java.util.List;

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderViewHolder> {
    private static final String TAG = "TrackOrderAdapter";

    private Context context;
    private List<OrderTrackHistory> orderTrackHistories;

    public TrackOrderAdapter(Context context, List<OrderTrackHistory> orderTrackHistories) {
        this.context = context;
        this.orderTrackHistories = orderTrackHistories;
    }

    @NonNull
    @Override
    public TrackOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackOrderViewHolder(OrderTrackListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackOrderViewHolder holder, int position) {
        OrderTrackHistory orderTrackHistory = orderTrackHistories.get(position);
        try {
            holder.getBinding().textViewStatus.setText(orderTrackHistory.getOrderStatus());
            holder.getBinding().textViewDateTime.setText(orderTrackHistory.getOrderStatusDate() + "/ " + orderTrackHistory.getOrderStatusTime());

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return orderTrackHistories.size();
    }

}
