package com.harperskebab.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FoodListItemRowBinding;
import com.harperskebab.model.SearchFoodCategory;
import com.harperskebab.view.adapter.viewholders.FoodViewHolder;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    List<SearchFoodCategory>searchFoodCategories;
    Context context;
    CategoryClicked categoryClicked;
    public interface CategoryClicked{
        void Clicked(int pos);
    }
    public SearchAdapter(List<SearchFoodCategory>searchFoodCategories,CategoryClicked categoryClicked){
        this.searchFoodCategories=searchFoodCategories;
        this.categoryClicked=categoryClicked;
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new FoodViewHolder(FoodListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
SearchFoodCategory searchFoodCategory=searchFoodCategories.get(position);
holder.getBinding().textViewFood.setText(searchFoodCategory.categoryName);
        holder.getBinding().textViewFoodDescription.setText(searchFoodCategory.categoryDescription);
        holder.getBinding().textViewFoodCount.setVisibility(View.VISIBLE);
        holder.getBinding().textViewTotalFoodPackage.setVisibility(View.GONE);
        holder.getBinding().textViewAdd.setVisibility(View.GONE);
        holder.getBinding().lLinearLayoutCountDetails.setVisibility(View.GONE);
        holder.getBinding().horz.setVisibility(View.INVISIBLE);
        holder.getBinding().textViewFoodPrice.setVisibility(View.GONE);
        Glide.with(context)
                .load(searchFoodCategory.categoryImg)
                .placeholder(R.drawable.app_logo)
                .into(holder.getBinding().imgshopImage);
        holder.getBinding().textViewPlus.setVisibility(View.GONE);
        holder.getBinding().layParent.setTag(position);
        holder.getBinding().layParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                categoryClicked.Clicked(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchFoodCategories.size();
    }
}
