package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.ActivityPayLaterOrderCompleteBinding;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.harperskebab.viewmodel.paylater.PayLaterCartViewModel;
import com.harperskebab.viewmodel.paylater.PayLaterFoodViewModel;

import java.util.ArrayList;
import java.util.HashSet;

public class PayLaterOrderCompleteActivity extends BaseActivity {

    private ActivityPayLaterOrderCompleteBinding binding;

    private PayLaterFoodViewModel payLaterFoodViewModel;
    private PayLaterCartViewModel payLaterCartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayLaterOrderCompleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.textViewThankYou.setText(languageViewModel.getLanguageResponse().getValue().getThankYou());

        payLaterFoodViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(PayLaterFoodViewModel.class);
        payLaterCartViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(PayLaterCartViewModel.class);

        payLaterCartViewModel.getPlaceOrderResponse().observe(this, placeOrderResponse -> {

            if (placeOrderResponse != null) {
                if (placeOrderResponse.getSuccess().equals(1L)) {
                    binding.textViewOrderNumberValue.setText(placeOrderResponse.getOrderIdentifyno());
                } else {

                }

                binding.buttonGoToHome.setOnClickListener(this::onClick);

                payLaterCartViewModel.getPlaceOrderResponse().removeObservers(this);

                payLaterCartViewModel.getPlaceOrderResponse().setValue(null);
                payLaterCartViewModel.getSubTotal().setValue(null);
                payLaterCartViewModel.getToPay().setValue(null);
            }

        });

        payLaterFoodViewModel.getFoods().setValue(new ArrayList<>());
        payLaterFoodViewModel.setSelectedFoods(new HashSet<>());


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goHome(PayLaterOrderCompleteActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClick(View v) {

        if (v.getId() == binding.buttonGoToHome.getId()) {
            goHome(PayLaterOrderCompleteActivity.this);
        }
    }


    @Override
    public void onBackPressed() {

    }

}