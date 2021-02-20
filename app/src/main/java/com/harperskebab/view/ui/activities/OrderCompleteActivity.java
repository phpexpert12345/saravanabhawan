package com.harperskebab.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.ActivityOrderCompleteBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.ToppingDao;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.SharedPrefrenceObj;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashSet;

public class OrderCompleteActivity extends BaseActivity {

    private ActivityOrderCompleteBinding binding;

    private FoodViewModel foodViewModel;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderCompleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.textViewThankYou.setText(languageViewModel.getLanguageResponse().getValue().getThankYou());
binding.textViewOrderNumber.setText(languageViewModel.getLanguageResponse().getValue().getYouReOrderNumber());
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constant.Data.IS_CONTINUE_ORDER_VISIBLE)) {
                if (savedInstanceState.getBoolean(Constant.Data.IS_CONTINUE_ORDER_VISIBLE)) {
                    binding.buttonContinueOrder.setVisibility(View.VISIBLE);
                } else {
                    binding.buttonContinueOrder.setVisibility(View.GONE);
                }
            }
        }

        foodViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(FoodViewModel.class);
        cartViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(CartViewModel.class);

        cartViewModel.getPlaceOrderResponse().observe(this, placeOrderResponse -> {

            if (placeOrderResponse != null) {
                if (placeOrderResponse.getSuccess().equals(1L)) {
                    binding.textViewOrderNumberValue.setText(placeOrderResponse.getOrderNo());
                }

                binding.buttonGoToHome.setOnClickListener(this::onClick);
                binding.buttonContinueOrder.setOnClickListener(this::onClick);

                cartViewModel.getPlaceOrderResponse().removeObservers(this);

                cartViewModel.getPlaceOrderResponse().setValue(null);
                cartViewModel.setCouponCode(null);
                cartViewModel.setCouponCodeDiscount(null);
                cartViewModel.setLoyaltyCodeDiscount(null);
                cartViewModel.setRestaurantDiscount(null);
                cartViewModel.setRiderTip(null);
                cartViewModel.setSubTotal(null);
                cartViewModel.setToPay(null);
                cartViewModel.setSelectedRestaurantBranch(null);
                cartViewModel.setSelectedDeliveryAddress(null);
                cartViewModel.setOrderSendToKitchenResponse(null);
                cartViewModel.setFoods(null);

                SharedPrefrenceObj.setSharedValue(OrderCompleteActivity.this, Constant.SharedPreference.EAT_IN_ORDER_ID, null);
            }

        });
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(this);
        CartDao cartDao=customerAppDatabase.cartDao();
        ToppingDao toppingDao=customerAppDatabase.toppingDao();
        cartDao.DeleteAll();
        toppingDao.DeleteAll();

        foodViewModel.getFoods().setValue(new ArrayList<>());
        foodViewModel.setSelectedFoods(new HashSet<>());


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                goHome(OrderCompleteActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClick(View v) {

        if (v.getId() == binding.buttonGoToHome.getId()) {
            goHome(OrderCompleteActivity.this);
        } else if (v.getId() == binding.buttonContinueOrder.getId()) {
            goHome(OrderCompleteActivity.this);
            startActivity(
                    new Intent(getApplicationContext(), PayLaterOrderActivity.class)
            );
        }
    }


    @Override
    public void onBackPressed() {

    }

}