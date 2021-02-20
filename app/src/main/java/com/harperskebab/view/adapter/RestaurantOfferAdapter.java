package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.OfferListItemRowBinding;
import com.harperskebab.model.RestaurantDiscountCoupon;
import com.harperskebab.view.adapter.viewholders.RestaurantOfferViewHolder;

import java.util.List;

public class RestaurantOfferAdapter extends RecyclerView.Adapter<RestaurantOfferViewHolder> {
    private static final String TAG = "FoodCategoryAdapter";

    private Context context;
    private List<RestaurantDiscountCoupon> deliveryAreas;
    private String currencySymbol;


    public RestaurantOfferAdapter(Context context, List<RestaurantDiscountCoupon> deliveryAreas) {
        this.context = context;
        this.deliveryAreas = deliveryAreas;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    @Override
    public RestaurantOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantOfferViewHolder(OfferListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantOfferViewHolder holder, int position) {
        RestaurantDiscountCoupon restaurantDiscountCoupon = deliveryAreas.get(position);

        try {
            holder.getBinding().textViewCouponTitle.setText(restaurantDiscountCoupon.getCouponTitle());
            holder.getBinding().textViewCouponDescription.setText(restaurantDiscountCoupon.getCouponDescription());
            holder.getBinding().textViewCouponCode.setText(restaurantDiscountCoupon.getCouponCode());
            holder.getBinding().textViewValidTill.setText("Valid Till: " + restaurantDiscountCoupon.getCouponValidTill());

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return deliveryAreas.size();
    }

}
