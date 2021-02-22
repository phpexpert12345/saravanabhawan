package com.harperskebab.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.OrderListItemRowBinding;
import com.harperskebab.model.Order;
import com.harperskebab.view.adapter.viewholders.OrderViewHolder;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    private static final String TAG = "OrderAdapter";

    private Context context;
    private List<Order> orders;
    private OnItemClick onItemClick;
    private OnTrackClick ontrackClick;
    private OnCancelClick onCancelClick;
    private OnReorderClick onReorderClick;
    private String currencySymbol;

    public OrderAdapter(Context context, List<Order> orders, OnItemClick onItemClick, OnTrackClick ontrackClick, OnCancelClick onCancelClick, OnReorderClick onReorderClick, String currencySymbol) {
        this.context = context;
        this.orders = orders;
        this.onItemClick = onItemClick;
        this.ontrackClick = ontrackClick;
        this.onCancelClick = onCancelClick;
        this.onReorderClick = onReorderClick;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(OrderListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        try {

            holder.getBinding().textViewOrder.setText(String.format("Order ID: %s", order.getOrderIdentifyno()));
            holder.getBinding().textViewOrderDate.setText(String.format("%s %s", order.getOrderDate(), order.getOrderTime()));
            holder.getBinding().textViewOrderPrice.setText(String.format("%s %s", currencySymbol, order.getOrdPrice()));
            holder.getBinding().textViewOrderStatus.setTextColor(Color.parseColor(order.getOrderStatusColorCode()));
            holder.getBinding().textViewOrderStatus.setText(order.getOrderStatusMsg());
            if (order.getOrder_pick().equalsIgnoreCase("0")){
                holder.getBinding().buttonCancel.setVisibility(View.VISIBLE);
                holder.getBinding().buttonCancel.setOnClickListener(v -> onCancelClick.onClick(position, order));
            }else {
                holder.getBinding().buttonCancel.setVisibility(View.GONE);
            }
            if (order.getStatus().equalsIgnoreCase("2")){
                holder.getBinding().buttonReorder.setVisibility(View.VISIBLE);
                holder.getBinding().buttonReorder.setOnClickListener(v -> onReorderClick.onClick(position, order));
            }else {
                holder.getBinding().buttonReorder.setVisibility(View.GONE);
            }
            if (order.getOrderStatusMsg().equalsIgnoreCase("pending")) {
                holder.getBinding().buttonOrderTrack.setVisibility(View.VISIBLE);

            }else if (order.getOrderStatusMsg().equalsIgnoreCase("delivered")){
                holder.getBinding().buttonOrderTrack.setVisibility(View.VISIBLE);
            } else {
                holder.getBinding().buttonOrderTrack.setVisibility(View.GONE);
            }
            holder.getBinding().buttonOrderTrack.setOnClickListener(v -> ontrackClick.onClick(position, order));
            holder.getBinding().getRoot().setOnClickListener(v -> onItemClick.onClick(position, order));

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public interface OnItemClick {
        void onClick(int position, Order order);
    }

    public interface OnTrackClick {
        void onClick(int position, Order order);
    }

    public interface OnCancelClick {
        void onClick(int position, Order order);
    }

    public interface OnReorderClick {
        void onClick(int position, Order order);
    }
}
