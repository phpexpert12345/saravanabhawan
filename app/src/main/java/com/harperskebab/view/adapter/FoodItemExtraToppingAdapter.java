package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodItemExtraToppingListItemRowBinding;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.utils.RestaurantFoodItemExtraAdapterInterface;
import com.harperskebab.view.adapter.viewholders.FoodItemExtraToppingViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FoodItemExtraToppingAdapter extends RecyclerView.Adapter<FoodItemExtraToppingViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<FoodItemExtraTopping> foodItemExtraToppings;
    private OnItemClick onItemClick;
    private String currencySymbol;
    RestaurantFoodItemExtraAdapterInterface restaurantFoodItemExtraAdapterInterface;
    public FoodItemExtraToppingAdapter(Context context, List<FoodItemExtraTopping> foodItemExtraToppings, OnItemClick onItemClick, String currencySymbol) {
        this.context = context;
        this.foodItemExtraToppings = foodItemExtraToppings;
        this.onItemClick = onItemClick;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public FoodItemExtraToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodItemExtraToppingViewHolder(FoodItemExtraToppingListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemExtraToppingViewHolder holder, int position) {
        FoodItemExtraTopping foodItemExtraTopping = foodItemExtraToppings.get(position);

        try {
            holder.getBinding().checkBoxFoodItemExtraTopping.setText(foodItemExtraTopping.getFoodAddonsName());
            holder.getBinding().textViewFoodItemExtraToppingPrice.setText(currencySymbol + " " + foodItemExtraTopping.getFoodPriceAddons());

            holder.getBinding().checkBoxFoodItemExtraTopping.setChecked(foodItemExtraTopping.isChecked());

            holder.itemView.setOnClickListener(v -> {
                foodItemExtraTopping.setChecked(!foodItemExtraTopping.isChecked());
                notifyDataSetChanged();
                onItemClick.onClick(position, foodItemExtraTopping);
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return foodItemExtraToppings.size();
    }

    public List<FoodItemExtraTopping> getSelectedFoodItemExtraToppings() {
        List<FoodItemExtraTopping> foodItemExtraToppings = new ArrayList<>();

        for (FoodItemExtraTopping foodItemExtraTopping : this.foodItemExtraToppings) {
            if (foodItemExtraTopping.isChecked()) {
                foodItemExtraToppings.add(foodItemExtraTopping);
            }
        }

        return foodItemExtraToppings;
    }

    public interface OnItemClick {
        void onClick(int position, FoodItemExtraTopping foodItemExtraTopping);
    }

}
