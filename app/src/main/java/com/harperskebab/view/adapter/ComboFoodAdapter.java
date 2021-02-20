package com.harperskebab.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FoodItemRowBinding;
import com.harperskebab.databinding.FoodListItemRowBinding;
import com.harperskebab.model.ComboList;
import com.harperskebab.view.adapter.viewholders.FoodViewHolder;
import com.harperskebab.view.ui.activities.ComboSelectionActivity;

import java.io.Serializable;
import java.util.List;

public class ComboFoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";
    int count=0;
    private Context context;
    private List<ComboList> foods;
    private String currencySymbol;

    public ComboFoodAdapter(Context context, List<ComboList> foods, String currencySymbol) {
        this.context = context;
        this.foods = foods;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(FoodListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        ComboList food = foods.get(position);

        try {

            holder.getBinding().textViewFood.setText(food.getDeal_name());
            holder.getBinding().textViewFoodDescription.setText(food.getDeal_description());
            holder.getBinding().textViewFoodCount.setText("" + food.getRestaurantPizzaItemPrice());


            holder.getBinding().textViewFoodPrice.setText(currencySymbol + " " + food.getRestaurantPizzaItemPrice());
            holder.getBinding().lLinearLayoutCountDetails.setVisibility(View.GONE);
            holder.getBinding().textViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ComboSelectionActivity.class);
                    intent.putExtra("ITEM_NAME",food.getDeal_name());
                    intent.putExtra("ITEM_DISCRIPTION",food.getDeal_description());
                    intent.putExtra("ITEM_POSITION",  position);
                    context.startActivity(intent);
                }
            });
            holder.getBinding().textViewPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ComboSelectionActivity.class);
                    intent.putExtra("ITEM_NAME",food.getDeal_name());
                    intent.putExtra("ITEM_DISCRIPTION",food.getDeal_description());
                    intent.putExtra("ITEM_POSITION",  position);
                    context.startActivity(intent);
                }
            });
holder.getBinding().horz.setVisibility(View.GONE);
            holder.getBinding().layParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//        Intent intent=new Intent(context, ProductDetailActivity.class);
//        intent.putExtra("Product",food);
//        intent.putExtra("ProductName",food.getRestaurantPizzaItemName());
//        intent.putExtra("ProductDiscription",food.getResPizzaDescription());
//        intent.putExtra("ProductCount",food.getFoodCount());
//        intent.putExtra("ProductPrice",currencySymbol + " " +food.getRestaurantPizzaItemPrice());
//        context.startActivity(intent);
                    Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.detail);
                    TextView textViewFood=dialog.findViewById(R.id.textViewFood);
                    ImageView imgBack=dialog.findViewById(R.id.imgBack);
                    HorizontalScrollView horx=dialog.findViewById(R.id.horx);
                    ImageView imgProduct=dialog.findViewById(R.id.imgProduct);
                    TextView textViewTotalFoodPackage=dialog.findViewById(R.id.textViewTotalFoodPackage);
                    TextView textViewFoodPrice=dialog.findViewById(R.id.textViewFoodPrice);
                    TextView textViewAdd=dialog.findViewById(R.id.textViewAdd);
                    TextView textViewMinus=dialog.findViewById(R.id.textViewMinus);
                    TextView textViewPlus=dialog.findViewById(R.id.textViewPlus);
                    TextView textViewFoodCount=dialog.findViewById(R.id.textViewFoodCount);
                    LinearLayout lLinearLayoutCountDetails=dialog.findViewById(R.id.lLinearLayoutCountDetails);
                    textViewFood.setText(food.getDeal_name());
                    horx.setVisibility(View.GONE);
                    textViewTotalFoodPackage.setText(food.getDeal_description());
                    textViewFoodPrice.setText(currencySymbol + " " +food.getRestaurantPizzaItemPrice());
//                    textViewFoodCount.setText("" + food.get());
//                    Glide.with(context)
//                            .load(food.get)
//                            .placeholder(R.drawable.loading)
//                            .into(imgProduct);
                    imgBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
//                    count = food.getFoodCount();
                    textViewAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count ++;
                            Intent intent = new Intent(context, ComboSelectionActivity.class);
                            intent.putExtra("ITEM_NAME",food.getDeal_name());
                            intent.putExtra("ITEM_DISCRIPTION",food.getDeal_description());
                            intent.putExtra("ITEM_POSITION",  position);
                            context.startActivity(intent);
                        }
                    });
                    lLinearLayoutCountDetails.setVisibility(View.GONE);
                    textViewPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ComboSelectionActivity.class);
                            intent.putExtra("ITEM_NAME",food.getDeal_name());
                            intent.putExtra("ITEM_DISCRIPTION",food.getDeal_description());
                            intent.putExtra("ITEM_POSITION",  position);
                            context.startActivity(intent);
                        }
                    });

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


}
