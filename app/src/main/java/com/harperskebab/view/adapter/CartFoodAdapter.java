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
import com.harperskebab.databinding.OldFoodListItemRowBinding;
import com.harperskebab.model.Food;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.viewholders.OldFoodViewHolder;
import com.harperskebab.view.ui.activities.MapActivity;

import java.text.DecimalFormat;
import java.util.List;

public class CartFoodAdapter extends RecyclerView.Adapter<OldFoodViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<Food> foods;
    private OnPlusClick onPlusClick;
    private OnMinusClick onMinusClick;
    private String currencySymbol;

    public CartFoodAdapter(Context context, List<Food> foods, OnPlusClick onPlusClick, OnMinusClick onMinusClick, String currencySymbol) {
        this.context = context;
        this.foods = foods;
        this.onPlusClick = onPlusClick;
        this.onMinusClick = onMinusClick;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public OldFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OldFoodViewHolder(OldFoodListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OldFoodViewHolder holder, int position) {
        Food food = foods.get(position);

        try {
            Glide.with(context).load(food.getFoodIcon()).into(holder.getBinding().imgshopImage);
            holder.getBinding().textViewFood.setText(food.getRestaurantPizzaItemName());
            holder.getBinding().textViewFoodDescription.setText(food.getResPizzaDescription());
            holder.getBinding().textViewFoodCount.setText("" + food.getFoodCount());
            if (food.getFoodItem()==null){
                holder.getBinding().txtSize.setVisibility(View.GONE);
            }else {
                holder.getBinding().txtSize.setVisibility(View.VISIBLE);
                holder.getBinding().txtSize.setText(food.getFoodItem().getRestaurantPizzaItemName());
            }
            if (food.getFoodCount() == 0) {

                holder.getBinding().textViewTotalFoodPackage.setVisibility(View.GONE);

                holder.getBinding().textViewAdd.setVisibility(View.VISIBLE);
                holder.getBinding().lLinearLayoutCountDetails.setVisibility(View.GONE);

            } else {

                holder.getBinding().textViewTotalFoodPackage.setVisibility(View.GONE);
                holder.getBinding().textViewTotalFoodPackage.setText(food.getFoodCount() + " item added");

                holder.getBinding().textViewAdd.setVisibility(View.GONE);
                holder.getBinding().lLinearLayoutCountDetails.setVisibility(View.VISIBLE);
            }
            if (food.getFoodItemExtraToppings()==null){
                holder.getBinding().recyExtraTopping.setVisibility(View.GONE);
            }else {
                holder.getBinding().recyExtraTopping.setVisibility(View.VISIBLE);
                SelectedExtratingAdapter adapter = new SelectedExtratingAdapter(context, currencySymbol, food.getFoodItemExtraToppings());
                holder.getBinding().recyExtraTopping.setHasFixedSize(true);
                holder.getBinding().recyExtraTopping.setLayoutManager(new LinearLayoutManager(context));
                holder.getBinding().recyExtraTopping.setAdapter(adapter);
            }
            if (food.getFoodCount()==0){
//                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
                holder.getBinding().textViewFoodPrice.setText(currencySymbol + " " + food.getRestaurantPizzaItemPrice());
            }else {
                Double price=0.00;
                if (food.getFoodItemExtraToppings()!=null){
                    for (int i=0;i<food.getFoodItemExtraToppings().size();i++){
                        String strPrice=food.getFoodItemExtraToppings().get(i).getFoodPriceAddons();
                        price=price+Double.parseDouble(strPrice);
                    }}
//                double val=Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice())+price;
                if (food.getFoodItem()!=null){
                    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
//                binding.txtTotalPrice.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + REAL_FORMATTER.format(total));
                    double str =price+food.getFoodCount() * Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice());
                    holder.getBinding().textViewFoodPrice.setText(currencySymbol + " " + Utility.DECIMAL_FORMAT.format(str));
                }else {
                    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
//                binding.txtTotalPrice.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + REAL_FORMATTER.format(total));
                    double str = price + food.getFoodCount() * Double.parseDouble(food.getRestaurantPizzaItemPrice());
                    holder.getBinding().textViewFoodPrice.setText(currencySymbol + " " + Utility.DECIMAL_FORMAT.format(str));
                }
            }
            holder.getBinding().textViewAdd.setOnClickListener(v ->
                    onPlusClick.onClick(position, food));
            holder.getBinding().textViewPlus.setOnClickListener(v -> onPlusClick.onClick(position, food));
            holder.getBinding().textViewMinus.setOnClickListener(v -> onMinusClick.onClick(position, food));

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void remove(Food food) {
        foods.remove(food);
    }

    public interface OnPlusClick {
        void onClick(int position, Food food);
    }

    public interface OnMinusClick {
        void onClick(int position, Food food);
    }

}
