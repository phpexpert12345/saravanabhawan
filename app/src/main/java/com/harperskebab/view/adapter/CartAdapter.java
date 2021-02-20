package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FoodListItemRowBinding;
import com.harperskebab.databinding.LayoutCartItemBinding;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.ToppingDao;
import com.harperskebab.model.ToppingItems;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<CartItem>cartItems;
    private String currencySymbol;
    Context context;
    public interface CartClicked{
        void Clicked(int pos,CartItem cartItem,Integer type);
    }
    CartClicked cartClicked;
    public CartAdapter(List<CartItem>cartItems,CartClicked cartClicked,String currencySymbol){
        this.cartItems=cartItems;
        this.cartClicked=cartClicked;
        this.currencySymbol=currencySymbol;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new CartViewHolder(LayoutCartItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem=cartItems.get(position);
        try {
            holder.getBinding().textViewFood.setText(cartItem.item_name);
            holder.getBinding().textViewFoodDescription.setText(cartItem.desc);
            holder.getBinding().textViewFoodCount.setText("" + cartItem.quantity);
            holder.getBinding().textViewTotalFoodPackage.setVisibility(View.GONE);
            holder.getBinding().lLinearLayoutCountDetails.setVisibility(View.VISIBLE);
            Glide.with(context).load(cartItem.icon).placeholder(R.drawable.app_logo).into(holder.getBinding().imgshopImage);
//            if(cartItem.quantity==0){
//                holder.getBinding().textViewTotalFoodPackage.setVisibility(View.INVISIBLE);
//
//
//                holder.getBinding().lLinearLayoutCountDetails.setVisibility(View.GONE);
//            }
//            else{
//                holder.getBinding().textViewTotalFoodPackage.setVisibility(View.VISIBLE);
//                holder.getBinding().textViewTotalFoodPackage.setText(cartItem.quantity + " item added");
//
//
//                holder.getBinding().lLinearLayoutCountDetails.setVisibility(View.VISIBLE);
//            }
            DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
            double price=0.0;
            if(cartItem.total_price!=null){
                price=Double.parseDouble(cartItem.total_price)*cartItem.quantity;
            }
            else{
                price=Double.parseDouble(cartItem.item_price)*cartItem.quantity;
            }
            holder.getBinding().textViewFoodPrice.setText(currencySymbol + " " + Utility.DECIMAL_FORMAT.format(price));

            holder.getBinding().textViewPlus.setOnClickListener(v ->  cartClicked.Clicked(position, cartItem,1));
            holder.getBinding().textViewMinus.setOnClickListener(v ->  cartClicked.Clicked(position, cartItem,0));
            String extras=getTopfromData(cartItem);
            if(!extras.equalsIgnoreCase("")) {
                if(!extras.equalsIgnoreCase("+")) {
                    holder.getBinding().textViewTop.setVisibility(View.VISIBLE);
                    holder.getBinding().textViewTop.setText(extras);
                }
                else{
                    holder.getBinding().textViewTop.setVisibility(View.GONE);
                }
            }
            else{
                holder.getBinding().textViewTop.setVisibility(View.GONE);
            }


        } catch (Exception e) {
//            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }
    private String getTopfromData(CartItem item){

        String top="";
        StringBuilder stringBuilder=new StringBuilder();
        if(item.top_name.contains(",")){
            String[] topp=item.top_name.split(",");
            for(String extra:topp){
                if(stringBuilder.length()>0){
                    stringBuilder.append("\n");
                }
                stringBuilder.append("+");
                stringBuilder.append(extra);
            }
        }
        else{
            stringBuilder.append("+").append(item.top_name);
        }

        if(stringBuilder.length()>0){
            top=stringBuilder.toString();
        }
        return top;

    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private LayoutCartItemBinding binding;
        public CartViewHolder(LayoutCartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public LayoutCartItemBinding getBinding() {
            return binding;
        }
    }
}
