package com.harperskebab.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.BranchListItemRowBinding;
import com.harperskebab.model.RestaurantBranch;
import com.harperskebab.view.adapter.viewholders.BranchViewHolder;
import com.harperskebab.view.ui.activities.RootActivity;

import java.text.DecimalFormat;
import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<RestaurantBranch> branches;
    private OnLocationPinClick onLocationPinClick;
    private int selectedFoodItemPosition = 0;
    double strLat,strLong,currentLat,currentLong;
    public BranchAdapter(Context context, double strLat, double strLong,double currentLat, double currentLong, List<RestaurantBranch> branches, OnLocationPinClick onLocationPinClick) {
        this.context = context;
        this.branches = branches;
        this.onLocationPinClick = onLocationPinClick;
        this.strLat=strLat;
        this.strLong=strLong;
        this.currentLat=currentLat;
        this.currentLong=currentLong;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BranchViewHolder(BranchListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        RestaurantBranch restaurantBranch = branches.get(position);

        try {

            if (restaurantBranch.getRestaurantBranchName()!=null) {
                holder.getBinding().radioButtonBranchTitle.setText(restaurantBranch.getRestaurantBranchName());
            }else {
                holder.getBinding().radioButtonBranchTitle.setVisibility(View.GONE);
            }
            if (restaurantBranch.getRestaurantBranchAddress()!=null) {
                holder.getBinding().textViewAddress.setText(restaurantBranch.getRestaurantBranchAddress());
            }else {
                holder.getBinding().textViewAddress.setVisibility(View.GONE);
            }

            double branchLat=Double.parseDouble(restaurantBranch.getBranchLatitude());
            double branchLong=Double.parseDouble(restaurantBranch.getBranchLongitude());
//            double distance=distance(strLat,strLong,branchLat,branchLong);
             DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
            holder.getBinding().textViewDistance.setText(REAL_FORMATTER.format(restaurantBranch.getDistance())+" mi");
            if (selectedFoodItemPosition == position) {
                holder.getBinding().radioButtonBranchTitle.setChecked(true);
            } else {
                holder.getBinding().radioButtonBranchTitle.setChecked(false);
            }
holder.getBinding().linearBranch.setTag(position);

            holder.getBinding().linearBranch.setOnClickListener(v -> {
                int pos= (int) v.getTag();
                selectedFoodItemPosition = pos;
                onLocationPinClick.onClick(pos, restaurantBranch);
                notifyDataSetChanged();
            });

            holder.getBinding().imageViewLocationPin.setOnClickListener(v -> {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("www.google.com")
                        .appendPath("maps")
                        .appendPath("dir")
                        .appendPath("")
                        .appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", restaurantBranch.getBranchLatitude() + "," + restaurantBranch.getBranchLongitude());
                String url = builder.build().toString();
                Log.d("Directions", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            });


        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    @Override
    public int getItemCount() {
        return branches.size();
    }

    public RestaurantBranch getselectedRestaurantBranch() {
        return branches.get(selectedFoodItemPosition);
    }

    public interface OnLocationPinClick {
        void onClick(int position, RestaurantBranch restaurantBranch);
    }

}
