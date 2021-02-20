package com.harperskebab.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FoodItemRowBinding;
import com.harperskebab.databinding.FoodListItemRowBinding;
import com.harperskebab.model.Food;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.viewholders.FoodViewHolder;
import com.harperskebab.view.ui.activities.ProductDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";
    int count=0;
    private Context context;
    public List<Food> foods;
    private OnPlusClick onPlusClick;
    private OnMinusClick onMinusClick;
    private String currencySymbol;

    public FoodAdapter(Context context, List<Food> foods, OnPlusClick onPlusClick, OnMinusClick onMinusClick, String currencySymbol) {
        this.context = context;
        this.foods = foods;
        this.onPlusClick = onPlusClick;
        this.onMinusClick = onMinusClick;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(FoodListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foods.get(position);

        try {
            Glide.with(context)
                    .load(food.getFoodIcon())
                    .placeholder(R.drawable.app_logo)
                    .into(holder.getBinding().imgshopImage);
            holder.getBinding().textViewFood.setText(food.getRestaurantPizzaItemName());
            holder.getBinding().textViewFoodDescription.setText(food.getResPizzaDescription());
            holder.getBinding().textViewFoodCount.setText("" + food.getFoodCount());
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

            if (food.getFoodCount()==0){
//                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
                holder.getBinding().textViewFoodPrice.setText(currencySymbol + " " + food.getRestaurantPizzaItemPrice());
            }else {
                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
//                binding.txtTotalPrice.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + REAL_FORMATTER.format(total));
                float str = food.getFoodCount() * Float.parseFloat(food.getRestaurantPizzaItemPrice());
                holder.getBinding().textViewFoodPrice.setText(currencySymbol + " " + REAL_FORMATTER.format(str));
            }
            if (food.getFoodType().equalsIgnoreCase("")){
                holder.getBinding().imgFoodType.setVisibility(View.GONE);
            }else {
                holder.getBinding().imgFoodType.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(food.getFoodType())
                        .placeholder(R.drawable.loading)
                        .into(holder.getBinding().imgFoodType);
            }
            if (food.getFoodTypeNon().equalsIgnoreCase("")){
                holder.getBinding().imgFoodTypeNon.setVisibility(View.GONE);
            }else {
                holder.getBinding().imgFoodTypeNon.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(food.getFoodTypeNon())
                        .placeholder(R.drawable.loading)
                        .into(holder.getBinding().imgFoodTypeNon);
            }
            if (food.getFoodPopular().equalsIgnoreCase("")){
                holder.getBinding().imgFoodPopuler.setVisibility(View.GONE);
            }else {
                holder.getBinding().imgFoodPopuler.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(food.getFoodPopular())
                        .placeholder(R.drawable.loading)
                        .into(holder.getBinding().imgFoodPopuler);
            }
            if (food.getFoodSpicy().equalsIgnoreCase("")){
                holder.getBinding().imgFoodSpicy.setVisibility(View.GONE);
            }else {
                holder.getBinding().imgFoodSpicy.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(food.getFoodSpicy())
                        .placeholder(R.drawable.loading)
                        .into(holder.getBinding().imgFoodSpicy);
            }
            if (food.getMidFoodSpicy().equalsIgnoreCase("")){
                holder.getBinding().imgMidFoodSpicy.setVisibility(View.GONE);
            }else {
                holder.getBinding().imgMidFoodSpicy.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(food.getMidFoodSpicy())
                        .placeholder(R.drawable.loading)
                        .into(holder.getBinding().imgMidFoodSpicy);
            }
            if (food.getVeryFoodSpicy().equalsIgnoreCase("")){
                holder.getBinding().imgVeryFoodSpicy.setVisibility(View.GONE);
            }else {
                holder.getBinding().imgVeryFoodSpicy.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(food.getVeryFoodSpicy())
                        .placeholder(R.drawable.loading)
                        .into(holder.getBinding().imgVeryFoodSpicy);
            }
            if (food.getGreenFoodSpicy().equalsIgnoreCase("")){
                holder.getBinding().imgGreenFoodSpicy.setVisibility(View.GONE);
            }else {
                holder.getBinding().imgGreenFoodSpicy.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(food.getGreenFoodSpicy())
                        .placeholder(R.drawable.loading)
                        .into(holder.getBinding().imgGreenFoodSpicy);
            }
            holder.getBinding().textViewAdd.setOnClickListener(v ->
                    onPlusClick.onClick(position, food));
            holder.getBinding().textViewPlus.setOnClickListener(v -> onPlusClick.onClick(position, food));
            holder.getBinding().textViewMinus.setOnClickListener(v -> onMinusClick.onClick(position, food));
//            holder.getBinding().layParent.setOnClickListener(v -> OnDetailClick.ondetailClick(position, food));
holder.getBinding().layParent.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detail);
        TextView textViewFood=dialog.findViewById(R.id.textViewFood);
        ImageView imgBack=dialog.findViewById(R.id.imgBack);
        ImageView imgProduct=dialog.findViewById(R.id.imgProduct);
        TextView textViewTotalFoodPackage=dialog.findViewById(R.id.textViewTotalFoodPackage);
        TextView textViewFoodPrice=dialog.findViewById(R.id.textViewFoodPrice);
        TextView textViewAdd=dialog.findViewById(R.id.textViewAdd);
        TextView textViewMinus=dialog.findViewById(R.id.textViewMinus);
        TextView textViewPlus=dialog.findViewById(R.id.textViewPlus);
        TextView textViewFoodCount=dialog.findViewById(R.id.textViewFoodCount);
        LinearLayout lLinearLayoutCountDetails=dialog.findViewById(R.id.lLinearLayoutCountDetails);
        textViewFood.setText(food.getRestaurantPizzaItemName());
        textViewTotalFoodPackage.setText(food.getResPizzaDescription());
        textViewFoodPrice.setText(currencySymbol + " " +food.getRestaurantPizzaItemPrice());
        textViewFoodCount.setText("" + food.getFoodCount());
        if(food.getFoodCount()>0){
            textViewAdd.setVisibility(View.GONE);
        }
        else{
            textViewAdd.setVisibility(View.VISIBLE);
        }
        ImageView imgFoodType=dialog.findViewById(R.id.imgFoodType);
        ImageView imgFoodTypeNon=dialog.findViewById(R.id.imgFoodTypeNon);
        ImageView imgFoodPopuler=dialog.findViewById(R.id.imgFoodPopuler);
        ImageView imgFoodSpicy=dialog.findViewById(R.id.imgFoodSpicy);
        ImageView imgMidFoodSpicy=dialog.findViewById(R.id.imgMidFoodSpicy);
        ImageView imgVeryFoodSpicy=dialog.findViewById(R.id.imgVeryFoodSpicy);
        ImageView imgGreenFoodSpicy=dialog.findViewById(R.id.imgGreenFoodSpicy);
        if (food.getFoodType().equalsIgnoreCase("")){
            imgFoodType.setVisibility(View.GONE);
        }else {
            imgFoodType.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(food.getFoodType())
                    .placeholder(R.drawable.loading)
                    .into(imgFoodType);
        }
        if (food.getFoodTypeNon().equalsIgnoreCase("")){
            imgFoodTypeNon.setVisibility(View.GONE);
        }else {
            imgFoodTypeNon.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(food.getFoodTypeNon())
                    .placeholder(R.drawable.loading)
                    .into(imgFoodTypeNon);
        }
        if (food.getFoodPopular().equalsIgnoreCase("")){
            imgFoodPopuler.setVisibility(View.GONE);
        }else {
            imgFoodPopuler.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(food.getFoodPopular())
                    .placeholder(R.drawable.loading)
                    .into(imgFoodPopuler);
        }
        if (food.getFoodSpicy().equalsIgnoreCase("")){
            imgFoodSpicy.setVisibility(View.GONE);
        }else {
            imgFoodSpicy.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(food.getFoodSpicy())
                    .placeholder(R.drawable.loading)
                    .into(imgFoodSpicy);
        }
        if (food.getMidFoodSpicy().equalsIgnoreCase("")){
            imgMidFoodSpicy.setVisibility(View.GONE);
        }else {
            imgMidFoodSpicy.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(food.getMidFoodSpicy())
                    .placeholder(R.drawable.loading)
                    .into(imgMidFoodSpicy);
        }
        if (food.getVeryFoodSpicy().equalsIgnoreCase("")){
            imgVeryFoodSpicy.setVisibility(View.GONE);
        }else {
            imgVeryFoodSpicy.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(food.getVeryFoodSpicy())
                    .placeholder(R.drawable.loading)
                    .into(imgVeryFoodSpicy);
        }
        if (food.getGreenFoodSpicy().equalsIgnoreCase("")){
            imgGreenFoodSpicy.setVisibility(View.GONE);
        }else {
            imgGreenFoodSpicy.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(food.getGreenFoodSpicy())
                    .placeholder(R.drawable.loading)
                    .into(imgGreenFoodSpicy);
        }
        Glide.with(context)
                .load(food.getFoodIcon())
                .placeholder(R.drawable.loading)
                .into(imgProduct);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
         count = food.getFoodCount();
        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count ++;
                if (count == 0) {
                    textViewAdd.setVisibility(View.VISIBLE);
                    lLinearLayoutCountDetails.setVisibility(View.GONE);
                } else {
                    textViewAdd.setVisibility(View.GONE);
                    lLinearLayoutCountDetails.setVisibility(View.VISIBLE);
                }
                textViewFoodCount.setText(String.valueOf(count));
                onPlusClick.onClick(position, food);
            }
        });
        textViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if (count == 0) {
                    textViewAdd.setVisibility(View.VISIBLE);
                    lLinearLayoutCountDetails.setVisibility(View.GONE);
                }
                textViewFoodCount.setText(String.valueOf(count));
//                else {
//                    textViewAdd.setVisibility(View.GONE);
//                    lLinearLayoutCountDetails.setVisibility(View.VISIBLE);
//                }
                onMinusClick.onClick(position, food);
            }
        });
        textViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                textViewFoodCount.setText(String.valueOf(count));
                onPlusClick.onClick(position, food);
            }
        });
        if (food.getFoodCount() == 0) {
            textViewAdd.setVisibility(View.VISIBLE);
            lLinearLayoutCountDetails.setVisibility(View.GONE);
        } else {
            textViewAdd.setVisibility(View.GONE);
            lLinearLayoutCountDetails.setVisibility(View.VISIBLE);
        }
        dialog.show();
    }
});
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
    public interface OnDetailClick {
        void ondetailClick(int position, Food food);
    }
    public interface OnMinusClick {
        void onClick(int position, Food food);
    }

}
