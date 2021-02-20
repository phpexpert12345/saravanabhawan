package com.harperskebab.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityHomeBinding;
import com.harperskebab.databinding.ActivityProductDetailBinding;
import com.harperskebab.model.Food;
import com.harperskebab.view.ui.fragments.FoodItemExtraToppingFragment;
import com.harperskebab.view.ui.fragments.FoodItemFragment;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.Set;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    String strProductName, strProductDiscription, strProductPrice;
    int strProductCount;
    Food food;
    private FoodViewModel foodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
//        intent.putExtra("ProductName",food.getRestaurantPizzaItemName());
//        intent.putExtra("ProductDiscription",food.getResPizzaDescription());
//        intent.putExtra("ProductPrice",food.getRestaurantPizzaItemPrice());
        foodViewModel = ViewModelFactory.getInstance(this).create(FoodViewModel.class);
        strProductName = getIntent().getStringExtra("ProductName");
        strProductDiscription = getIntent().getStringExtra("ProductDiscription");
        strProductPrice = getIntent().getStringExtra("ProductPrice");
        strProductCount = getIntent().getIntExtra("ProductCount", 0);
        binding.textViewFood.setText(strProductName);
        binding.textViewTotalFoodPackage.setText(strProductDiscription);
        binding.textViewFoodPrice.setText(strProductPrice);
        if (strProductCount == 0) {
            binding.lLinearLayoutCountDetails.setVisibility(View.GONE);

        } else {
            binding.textViewAdd.setVisibility(View.GONE);
            binding.lLinearLayoutCountDetails.setVisibility(View.VISIBLE);
        }
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.layAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onPlusFood();
            }
        });
//        binding.textViewMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (strProductCount > food.getFoodCountLimit()) {
//                    food.setFoodCount(food.getFoodCount() - 1);
//                    foodAdapter.notifyDataSetChanged();
//
//                    Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
//                    if (food.getFoodCount() == 0) {
//                        selectedFoods.remove(food);
//                    }
//                    if (selectedFoods.size() == 0) {
//                        binding.constraintLayoutCart.setVisibility(View.GONE);
//                    }
//                    foodViewModel.setSelectedFoods(selectedFoods);
//                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("ITEM_COUNT", String.valueOf(foodViewModel.getSelectedFoods().getValue().size()));
//                    editor.apply();
//            }
//        });
////        binding.textViewPlus.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                onPlusFood();
////            }
////        });
//    }

//    private void onPlusFood() {
//        if (foodViewModel.getCurrentSelectedFood().getValue().getFoodCount() == 0
//                && (foodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES")
//                || foodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES"))) {
//            if (foodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES")) {
//                binding.rellayoutMain.setVisibility(View.GONE);
//                initiateFoodItemFragment();
//            } else if (foodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES")) {
//                initiateFoodItemExtraToppingFragment();
//            }
//        } else {
//            foodViewModel.getCurrentSelectedFood().getValue().setFoodCount(foodViewModel.getCurrentSelectedFood().getValue().getFoodCount() + 1);
//            Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
//            if (!selectedFoods.contains(foodViewModel.getCurrentSelectedFood().getValue())) {
//                selectedFoods.add(foodViewModel.getCurrentSelectedFood().getValue());
//            }
//            foodViewModel.setSelectedFoods(selectedFoods);
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("ITEM_COUNT", String.valueOf(foodViewModel.getSelectedFoods().getValue().size()));
//            editor.apply();
//        }
//    }

//    private void initiateFoodItemFragment() {
//        FoodItemFragment foodItemFragment = FoodItemFragment.newInstance(binding.linearLayoutContent.getId());
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(binding.linearLayoutContent.getId(), foodItemFragment);
//        transaction.addToBackStack(FoodItemFragment.getTAG());
//        transaction.commit();
//    }

//    private void initiateFoodItemExtraToppingFragment() {
//        FoodItemExtraToppingFragment foodItemExtraToppingFragment = FoodItemExtraToppingFragment.newInstance(binding.linearLayoutContent.getId(), null);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(binding.linearLayoutContent.getId(), foodItemExtraToppingFragment);
//        transaction.addToBackStack(FoodItemExtraToppingFragment.getTAG());
//        transaction.commit();
//    }
    }
}