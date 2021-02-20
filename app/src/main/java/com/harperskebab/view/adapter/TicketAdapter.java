package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.TicketListItemRowBinding;
import com.harperskebab.model.ComplaintsHistory;
import com.harperskebab.view.adapter.viewholders.TicketViewHolder;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<ComplaintsHistory> complaintsHistories;

    public TicketAdapter(Context context, List<ComplaintsHistory> complaintsHistories) {
        this.context = context;
        this.complaintsHistories = complaintsHistories;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicketViewHolder(TicketListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        ComplaintsHistory complaintsHistory = complaintsHistories.get(position);

        try {
            holder.getBinding().textViewTicketNo.setText("TicketvNo: " + complaintsHistory.getComplaintId());
            holder.getBinding().textViewOrderNo.setText("Order No:" + complaintsHistory.getOrderIDNumber());
            holder.getBinding().textViewTicketStatus.setText(complaintsHistory.getOrderStatusMessage());

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return complaintsHistories.size();
    }

}
