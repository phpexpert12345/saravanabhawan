package com.harperskebab.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.FoodItemExtraTopRadioRowBinding;
import com.harperskebab.databinding.FoodItemExtraToppingListItemRowBinding;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.utils.RestaurantFoodItemExtraAdapterInterface;

import java.util.List;

public class RestroExtraAdapter extends RecyclerView.Adapter<RestroExtraAdapter.RestroViewHolder> {
    List<FoodItemExtraTopping>extraToppings;
    private String currencySymbol;
    int select_pos=-1;
    RestaurantFoodItemExtraAdapterInterface restaurantFoodItemExtraAdapterInterface;
    public RestroExtraAdapter(List<FoodItemExtraTopping>extraToppings,String currencySymbol, RestaurantFoodItemExtraAdapterInterface restaurantFoodItemExtraAdapterInterface){
        this.extraToppings=extraToppings;
        this.currencySymbol=currencySymbol;
        this.restaurantFoodItemExtraAdapterInterface=restaurantFoodItemExtraAdapterInterface;
    }
    @NonNull
    @Override
    public RestroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestroViewHolder(FoodItemExtraTopRadioRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestroViewHolder holder, int position) {
        FoodItemExtraTopping foodItemExtraTopping = extraToppings.get(position);
        try {
            holder.getBinding().radioFoodItemExtraTopping.setText(foodItemExtraTopping.getFoodAddonsName());
            holder.getBinding().textViewFoodItemExtraToppingPrice.setText(currencySymbol + " " + foodItemExtraTopping.getFoodPriceAddons());
if(select_pos==position){
    holder.getBinding().radioFoodItemExtraTopping.setChecked(true);
}
else{
    holder.getBinding().radioFoodItemExtraTopping.setChecked(false);
}

holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(v -> {
                int pos= (int) v.getTag();
               select_pos=pos;
                notifyDataSetChanged();
                restaurantFoodItemExtraAdapterInterface.getCheckedItem(extraToppings.get(pos).getFoodAddonsName(),extraToppings.get(pos).getFoodPriceAddons(),extraToppings.get(pos).getExtraID(),extraToppings);
//                onItemClick.onClick(position, foodItemExtraTopping);
            });

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return extraToppings.size();
    }

    public class RestroViewHolder extends RecyclerView.ViewHolder {
        public FoodItemExtraTopRadioRowBinding getBinding() {
            return binding;
        }

        private FoodItemExtraTopRadioRowBinding binding;
        public RestroViewHolder(FoodItemExtraTopRadioRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
