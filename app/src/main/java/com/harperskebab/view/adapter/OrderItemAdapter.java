package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.databinding.OrderItemListItemRowBinding;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.model.OrderFoodItem;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.viewholders.OrderItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemViewHolder> {
    private static final String TAG = "OrderItemAdapter";

    private Context context;
    private List<OrderFoodItem> orderFoodItems;

    public OrderItemAdapter(Context context, List<OrderFoodItem> orderFoodItems) {
        this.context = context;
        this.orderFoodItems = orderFoodItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(OrderItemListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderFoodItem orderFoodItem = orderFoodItems.get(position);

        try {
            if(orderFoodItem.getFood_Icon()!=null){
                if(!orderFoodItem.getFood_Icon().equalsIgnoreCase("")){
                    holder.getBinding().imgCard.setVisibility(View.VISIBLE);
                    Glide.with(context).load(orderFoodItem.getFood_Icon()).into(holder.getBinding().imgshopImage);
                }
                else{
                    holder.getBinding().imgCard.setVisibility(View.GONE);
                }
            }
            else{
                holder.getBinding().imgCard.setVisibility(View.GONE);
            }

//            Glide.with(context).load(orderFoodItem.get()).into(holder.getBinding().imgshopImage);
            List<FoodItemExtraTopping> foodExtraToppings=new ArrayList<>();
            String data = orderFoodItem.getExtraTopping();
            if(!data.equalsIgnoreCase("")) {
                String[] items = data.split(",");


                for (int i = 0; i < items.length; i++) {
                    FoodItemExtraTopping foodItemExtraTopping = new FoodItemExtraTopping();
                    foodItemExtraTopping.setFoodAddonsName(items[i]);
                    foodExtraToppings.add(foodItemExtraTopping);
                }
            }
            if(!orderFoodItem.getItemSize().equalsIgnoreCase("")){
                holder.getBinding().txtSize.setVisibility(View.VISIBLE);
                holder.getBinding().txtSize.setText(orderFoodItem.getItemSize());
            }
            else{
                holder.getBinding().txtSize.setVisibility(View.GONE);
            }
            holder.getBinding().textViewFood.setText(orderFoodItem.getItemsName());
            holder.getBinding().textViewFoodCount.setText(orderFoodItem.getQuantity().toString());
            holder.getBinding().textViewFoodPrice.setText(String.format(Utility.getCurrencySymbol(orderFoodItem.getCurrency())+""+orderFoodItem.getMenuprice()));
            if (orderFoodItem.getExtraTopping().equalsIgnoreCase("0")){
                holder.getBinding().recyExtraTopping.setVisibility(View.GONE);
            }else {
                if(foodExtraToppings.size()>0) {
                    holder.getBinding().recyExtraTopping.setVisibility(View.VISIBLE);
                    SelectedExtratingAdapter adapter = new SelectedExtratingAdapter(context, orderFoodItem.getCurrency(), foodExtraToppings);
                    holder.getBinding().recyExtraTopping.setHasFixedSize(true);
                    holder.getBinding().recyExtraTopping.setLayoutManager(new LinearLayoutManager(context));
                    holder.getBinding().recyExtraTopping.setAdapter(adapter);
                }
                else{
                    holder.getBinding().recyExtraTopping.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return orderFoodItems.size();
    }

}
