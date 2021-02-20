package com.harperskebab.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.model.FoodItemExtraTopping;

import java.util.List;

public class SelectedExtratingAdapter extends RecyclerView.Adapter<SelectedExtratingAdapter.ViewHolder> {
    List<FoodItemExtraTopping> foodItemExtraToppings;
    Context context;
String currencySymbol;

    public SelectedExtratingAdapter(Context context, String currencySymbol, List<FoodItemExtraTopping> foodItemExtraToppings) {
        this.context = context;
        this.currencySymbol=currencySymbol;
        this.foodItemExtraToppings = foodItemExtraToppings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cell_extrating, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FoodItemExtraTopping foodItemExtraTopping = foodItemExtraToppings.get(position);
        holder.txtExtrName.setText("+"+foodItemExtraTopping.getFoodAddonsName());
        if (foodItemExtraTopping.getFoodPriceAddons()==null){
            holder.txtPrice.setVisibility(View.GONE);
        }else {
            holder.txtPrice.setVisibility(View.VISIBLE);
            holder.txtPrice.setText(currencySymbol + " " + foodItemExtraTopping.getFoodPriceAddons());
        }

    }


    @Override
    public int getItemCount() {
        return foodItemExtraToppings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtExtrName,txtPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtExtrName = (TextView) itemView.findViewById(R.id.txtExtrName);
            this.txtPrice=(TextView)itemView.findViewById(R.id.txtPrice);
        }
    }
}
