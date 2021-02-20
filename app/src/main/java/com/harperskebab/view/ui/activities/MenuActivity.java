package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityMenuBinding;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.ui.fragments.FoodFragment;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.PostalCodeViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class MenuActivity extends BaseActivity {

    private ActivityMenuBinding binding;
    private FragmentManager fragmentManager = null;
    private MenuItem menuItemOrderType;

    private int foodCategoryPosition = 0;
private String foodCombo;
    private FoodViewModel foodViewModel;
    private PostalCodeViewModel postalCodeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorGreenDark));
        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
binding.header.imgLanguage.setVisibility(View.GONE);
        foodViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(FoodViewModel.class);
        postalCodeViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(PostalCodeViewModel.class);

        fragmentManager = getSupportFragmentManager();

        savedInstanceState = getIntent().getExtras();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constant.Data.FOOD_CATEGORY_POSITION)) {
                foodCategoryPosition = savedInstanceState.getInt(Constant.Data.FOOD_CATEGORY_POSITION, 0);
                foodCombo=savedInstanceState.getString("FOOD_CATEGORY_COMBO", "");
                initiateFoodFragment(foodCategoryPosition,foodCombo);
            }
        }else {
            foodCombo="";
            initiateFoodFragment(foodCategoryPosition,foodCombo);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateFoodFragment(int foodCategoryPosition,String foodCombo) {

        FoodFragment foodFragment = (FoodFragment) fragmentManager.findFragmentByTag(FoodFragment.TAG);
        foodFragment = foodFragment == null ? FoodFragment.newInstance(binding.frameContainerMenu.getId(), foodCategoryPosition,foodCombo) : foodFragment;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerMenu.getId(), foodFragment);
        transaction.addToBackStack(FoodFragment.TAG);
        transaction.commit();
    }

}