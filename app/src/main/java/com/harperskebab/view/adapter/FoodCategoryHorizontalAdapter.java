package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.R;
import com.harperskebab.databinding.FoodCategoryHorizontalItemRowBinding;
import com.harperskebab.model.FoodCategory;
import com.harperskebab.view.adapter.viewholders.FoodCategorHorizontalViewHolder;

import java.util.List;

public class FoodCategoryHorizontalAdapter extends RecyclerView.Adapter<FoodCategorHorizontalViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<FoodCategory> foodCategories;
    private OnItemClick onItemClick;
    private int selectedFoodCategoryPosition;

    public FoodCategoryHorizontalAdapter(Context context, List<FoodCategory> foodCategories, int selectedFoodCategoryPosition, OnItemClick onItemClick) {
        this.context = context;
        this.foodCategories = foodCategories;
        this.selectedFoodCategoryPosition = selectedFoodCategoryPosition;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public FoodCategorHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodCategorHorizontalViewHolder(FoodCategoryHorizontalItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategorHorizontalViewHolder holder, int position) {
        FoodCategory foodCategory = foodCategories.get(position);

        try {
            holder.getBinding().textViewFoodCategory.setText(foodCategory.getCategoryName());

            if (selectedFoodCategoryPosition == position) {
                holder.getBinding().textViewFoodCategory.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                holder.getBinding().textViewFoodCategory.setBackground(context.getDrawable(R.drawable.new_white_bg));

            } else {
                holder.getBinding().textViewFoodCategory.setTextColor(ContextCompat.getColor(context, R.color.newcolorGreendark));
                holder.getBinding().textViewFoodCategory.setBackground(context.getDrawable(R.drawable.catgry_bg));
//                holder.getBinding().textViewFoodCategory.setBackground(null);
            }

            holder.itemView.setOnClickListener(v -> {
                selectedFoodCategoryPosition = position;
                notifyDataSetChanged();
                onItemClick.onClick(position, foodCategory);
            });
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
