package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodItemExtraToppingListItemRowBinding;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.network.retrofit.responsemodels.RmGetFoodItemExtraToppingResponse;
import com.harperskebab.utils.RestaurantFoodItemExtraAdapterInterface;
import com.harperskebab.view.adapter.viewholders.FoodItemExtraToppingViewHolder;

import java.util.List;

public class FoodItemExtraAdapter extends RecyclerView.Adapter<FoodItemExtraToppingViewHolder> {
List<RmGetFoodItemExtraToppingResponse.MenuItemExtraGroup>menuItemExtraGroups;
List<FoodItemExtraTopping>foodItemExtraToppings;
RestaurantFoodItemExtraAdapterInterface restaurantFoodItemExtraAdapterInterface;
    private String currencySymbol;
    Context context;
public FoodItemExtraAdapter(List<RmGetFoodItemExtraToppingResponse.MenuItemExtraGroup>menuItemExtraGroups,List<FoodItemExtraTopping>foodItemExtraToppings,RestaurantFoodItemExtraAdapterInterface restaurantFoodItemExtraAdapterInterface, String currencySymbol){
    this.menuItemExtraGroups=menuItemExtraGroups;
    this.foodItemExtraToppings=foodItemExtraToppings;
    this.restaurantFoodItemExtraAdapterInterface=restaurantFoodItemExtraAdapterInterface;
    this.currencySymbol=currencySymbol;
}
    @NonNull
    @Override
    public FoodItemExtraToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    this.context=parent.getContext();
        return new FoodItemExtraToppingViewHolder(FoodItemExtraToppingListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemExtraToppingViewHolder holder, int position) {
if(menuItemExtraGroups.get(0).getFoodAddonsSelectionType().equalsIgnoreCase("Checkbox")){
    FoodItemExtraTopping foodItemExtraTopping = foodItemExtraToppings.get(position);
    holder.getBinding().checkBoxFoodItemExtraTopping.setVisibility(View.VISIBLE);
    holder.getBinding().textViewFoodItemExtraToppingPrice.setVisibility(View.VISIBLE);
    holder.getBinding().txtGroup.setVisibility(View.GONE);
    holder.getBinding().recylerExtras.setVisibility(View.GONE);
    try {

        holder.getBinding().checkBoxFoodItemExtraTopping.setText(foodItemExtraTopping.getFoodAddonsName());
        holder.getBinding().textViewFoodItemExtraToppingPrice.setText(currencySymbol + " " + foodItemExtraTopping.getFoodPriceAddons());

        holder.getBinding().checkBoxFoodItemExtraTopping.setChecked(foodItemExtraTopping.isChecked());
holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(v -> {
            int pos= (int) v.getTag();
            foodItemExtraTopping.setChecked(!foodItemExtraTopping.isChecked());
            restaurantFoodItemExtraAdapterInterface.getTop(pos,foodItemExtraTopping);
            notifyDataSetChanged();

        });
    } catch (Exception e) {
//        Log.e(TAG, "onBindViewHolder: Exception", e);
    }
}
else{
    holder.getBinding().txtGroup.setVisibility(View.VISIBLE);
    holder.getBinding().txtGroup.setText(menuItemExtraGroups.get(position).getFoodGroupName());
    holder.getBinding().checkBoxFoodItemExtraTopping.setVisibility(View.GONE);
    holder.getBinding().textViewFoodItemExtraToppingPrice.setVisibility(View.GONE);
    holder.getBinding().recylerExtras.setVisibility(View.VISIBLE);
    RestroExtraAdapter restroExtraAdapter=new RestroExtraAdapter(menuItemExtraGroups.get(0).getFoodItemExtraTopping(),currencySymbol,restaurantFoodItemExtraAdapterInterface);
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
    holder.getBinding().recylerExtras.setAdapter(restroExtraAdapter);
    holder.getBinding().recylerExtras.setLayoutManager(linearLayoutManager);
}
    }

    @Override
    public int getItemCount() {
        if(menuItemExtraGroups.get(0).getFoodAddonsSelectionType().equalsIgnoreCase("Checkbox")){
            return foodItemExtraToppings.size();
        }
        else{
            return menuItemExtraGroups.size();
        }

    }
}
