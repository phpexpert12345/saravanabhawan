package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodItemListItemRowBinding;
import com.harperskebab.model.FoodItem;
import com.harperskebab.view.adapter.viewholders.FoodItemViewHolder;
import com.harperskebab.view.ui.fragments.FoodItemFragment;
import com.harperskebab.view.ui.fragments.paylater.PayLaterAddFoodItemFragment;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<FoodItem> foodItems;
    private int selectedFoodItemPosition = 0;
    private String currencySymbol;
    private Fragment fragment;
    boolean isFrom=false;
    public FoodItemAdapter(Context context, List<FoodItem> foodItems, String currencySymbol, Fragment fragment, boolean isFrom) {
        this.context = context;
        this.foodItems = foodItems;
        this.currencySymbol = currencySymbol;
        this.fragment = fragment;
        this.isFrom=isFrom;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodItemViewHolder(FoodItemListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);

        try {
            holder.getBinding().radioButtonFoodItem.setText(foodItem.getRestaurantPizzaItemName());
            holder.getBinding().textViewFoodItemPrice.setText(currencySymbol + " " + foodItem.getRestaurantPizzaItemPrice());

            if (selectedFoodItemPosition == position) {
                if (isFrom){
                    ((PayLaterAddFoodItemFragment) fragment).fragmentMethod(currencySymbol + " " + foodItem.getRestaurantPizzaItemPrice());
                }else {
                    ((FoodItemFragment) fragment).fragmentMethod(currencySymbol + " " + foodItem.getRestaurantPizzaItemPrice());
                }
                    holder.getBinding().radioButtonFoodItem.setChecked(true);

            } else {
                holder.getBinding().radioButtonFoodItem.setChecked(false);
            }

            holder.itemView.setOnClickListener(v -> {
                selectedFoodItemPosition = position;
                notifyDataSetChanged();
            });

            holder.getBinding().radioButtonFoodItem.setOnClickListener(v -> {
                selectedFoodItemPosition = position;
                if (isFrom){
                    ((PayLaterAddFoodItemFragment) fragment).fragmentMethod(currencySymbol + " " + foodItem.getRestaurantPizzaItemPrice());
                }else {
                    ((FoodItemFragment) fragment).fragmentMethod(currencySymbol + " " + foodItem.getRestaurantPizzaItemPrice());
                }
                notifyDataSetChanged();
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public FoodItem getselectedFoodItem() {
        return foodItems.get(selectedFoodItemPosition);
    }

}
