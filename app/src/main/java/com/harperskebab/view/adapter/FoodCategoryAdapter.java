package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FoodCategoryListItemRowBinding;
import com.harperskebab.model.FoodCategory;
import com.harperskebab.view.adapter.viewholders.FoodCategoryViewHolder;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<FoodCategory> foodCategories;
    private OnItemClick onItemClick;

    public FoodCategoryAdapter(Context context, List<FoodCategory> foodCategories, OnItemClick onItemClick) {
        this.context = context;
        this.foodCategories = foodCategories;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public FoodCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodCategoryViewHolder(FoodCategoryListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategoryViewHolder holder, int position) {
        FoodCategory foodCategory = foodCategories.get(position);

        try {

            holder.getBinding().textViewFoodCategory.setText(foodCategory.getCategoryName());

            Glide.with(context)
                    .load(foodCategory.getCategoryImg())
                    .placeholder(R.drawable.app_logo)
                    .centerCrop()
                    .into(holder.getBinding().imageViewFood);


            holder.itemView.setOnClickListener(v -> onItemClick.onClick(position, foodCategory));
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return foodCategories.size();
    }

    public interface OnItemClick {
        void onClick(int position, FoodCategory foodCategory);
    }
}
