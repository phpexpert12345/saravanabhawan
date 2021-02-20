package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.BranchViewListItemRowBinding;
import com.harperskebab.model.RestaurantBranch;
import com.harperskebab.view.adapter.viewholders.BranchViewViewHolder;

import java.util.List;

public class BranchViewAdapter extends RecyclerView.Adapter<BranchViewViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<RestaurantBranch> branches;
    private int selectedFoodItemPosition = 0;

    public BranchViewAdapter(Context context, List<RestaurantBranch> branches) {
        this.context = context;
        this.branches = branches;
    }

    @NonNull
    @Override
    public BranchViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BranchViewViewHolder(BranchViewListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewViewHolder holder, int position) {
        RestaurantBranch restaurantBranch = branches.get(position);

        try {
            holder.getBinding().radioButtonBranchTitle.setText(restaurantBranch.getRestaurantBranchName());
            holder.getBinding().textViewAddress.setText(restaurantBranch.getRestaurantBranchAddress());

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

}
