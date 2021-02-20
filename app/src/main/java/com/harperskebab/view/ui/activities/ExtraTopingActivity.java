package com.harperskebab.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityExtraTopingBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.ComboExtraToppingAdapter;
import com.harperskebab.view.adapter.ComboFoodAdapter;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class ExtraTopingActivity extends BaseActivity {
    private ActivityExtraTopingBinding binding;
    private FoodViewModel foodViewModel;
    String strTitleName, strTitleDiscription, strFreeAllowedValue, itemId, comboSlotItemId, foodItemSizeId;
    int itemPosition;
private ComboExtraToppingAdapter comboExtraToppingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExtraTopingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        foodViewModel = ViewModelFactory.getInstance(this).create(FoodViewModel.class);
        setTitle("Extra Topping");
        strTitleName = getIntent().getStringExtra("ITEM_NAME");
        strTitleDiscription = getIntent().getStringExtra("ITEM_DISCRIPTION");
        itemPosition = getIntent().getIntExtra("ITEM_POSITION", -1);
        strFreeAllowedValue = getIntent().getStringExtra("FREE_ALLOWED_VALUE");
        itemId = getIntent().getStringExtra("ItemID");
        comboSlotItemId = getIntent().getStringExtra("COMBO_SLOT_ITEMID");
        foodItemSizeId = getIntent().getStringExtra("FOOD_ITEM_SIZEID");


        binding.txtExtraTopingTitle.setText(strTitleName);
        binding.txtExtraTopingDescription.setText(strTitleDiscription);
        binding.txtExtraTopingCount.setText("Choose Any " + strFreeAllowedValue + " Topping Free");
        binding.recExtraTopingItem.setLayoutManager(new LinearLayoutManager(this));
        binding.recExtraTopingItem.setNestedScrollingEnabled(false);
        foodViewModel.getComboExtraTopping(this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, itemId, comboSlotItemId, foodItemSizeId, new NetworkOperations(true));
        foodViewModel.getGetComboExtraToppingListResponse().observe(this, comboExtraToppingListResponse -> {
            if (comboExtraToppingListResponse != null) {
                binding.txtExtraToppingName.setText(comboExtraToppingListResponse.getMenuItemExtraGroup().get(0).get(0).getFoodGroupName());
                comboExtraToppingAdapter = new ComboExtraToppingAdapter(this,comboExtraToppingListResponse.getMenuItemExtraGroup().get(0).get(0).getSubExtraItemsRecord());
                binding.recExtraTopingItem.setAdapter(comboExtraToppingAdapter);
            }
        });

    }
}