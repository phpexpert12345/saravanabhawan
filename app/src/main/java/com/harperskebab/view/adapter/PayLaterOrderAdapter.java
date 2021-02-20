package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.PayLaterOrderListItemRowBinding;
import com.harperskebab.model.Order;
import com.harperskebab.view.adapter.viewholders.PayLaterOrderViewHolder;

import java.util.List;

public class PayLaterOrderAdapter extends RecyclerView.Adapter<PayLaterOrderViewHolder> {
    private static final String TAG = "PayLaterOrderAdapter";

    private Context context;
    private List<Order> orders;
    private OnItemClick onItemClick;
    private OnContinueOrder onContinueOrder;
    private OnPayNow onPayNow;
    private String currencySymbol;

    public PayLaterOrderAdapter(Context context, List<Order> orders, OnItemClick onItemClick, OnContinueOrder onContinueOrder, OnPayNow onPayNow, String currencySymbol) {
        this.context = context;
        this.orders = orders;
        this.onItemClick = onItemClick;
        this.onContinueOrder = onContinueOrder;
        this.onPayNow = onPayNow;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public PayLaterOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PayLaterOrderViewHolder(PayLaterOrderListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PayLaterOrderViewHolder holder, int position) {
        Order order = orders.get(position);

        try {

            holder.getBinding().textViewOrder.setText(String.format("Order ID: %s", order.getOrderIdentifyno()));
            holder.getBinding().textViewOrderDate.setText(String.format("%s %s", order.getOrderDate(), order.getOrderTime()));
            holder.getBinding().textViewOrderPrice.setText(String.format("%s %s", currencySymbol, order.getOrdPrice()));

            if (order.getOrderStatusMsg().equalsIgnoreCase("pending")) {
                holder.getBinding().buttonContinueOrder.setVisibility(View.VISIBLE);
                holder.getBinding().buttonPayNow.setVisibility(View.VISIBLE);

                holder.getBinding().buttonContinueOrder.setOnClickListener(v -> onContinueOrder.onClick(position, order));
                holder.getBinding().buttonPayNow.setOnClickListener(v -> onPayNow.onClick(position, order));
            } else {
                holder.getBinding().buttonContinueOrder.setVisibility(View.GONE);
                holder.getBinding().buttonPayNow.setVisibility(View.GONE);

                holder.getBinding().buttonContinueOrder.setOnClickListener(null);
                holder.getBinding().buttonPayNow.setOnClickListener(null);
            }

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

    public interface OnContinueOrder {
        void onClick(int position, Order order);
    }

    public interface OnPayNow {
        void onClick(int position, Order order);
    }

}
