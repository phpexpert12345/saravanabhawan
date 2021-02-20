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
import com.harperskebab.databinding.ActivityPayLaterOrderBinding;
import com.harperskebab.view.ui.fragments.paylater.PayLaterOrdersListFragment;

public class PayLaterOrderActivity extends BaseActivity {
    private static final String TAG = "PayLaterOrderActivity";

    private ActivityPayLaterOrderBinding binding;

    private FragmentManager fragmentManager = null;
    private String foodCombo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayLaterOrderBinding.inflate(getLayoutInflater());
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
//        savedInstanceState = getIntent().getExtras();
//        foodCombo=savedInstanceState.getString("FOOD_CATEGORY_COMBO", "");
        fragmentManager = getSupportFragmentManager();

        setupView();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        initiatePayLaterOrdersListFragment();
    }

    private void initiatePayLaterOrdersListFragment() {
        PayLaterOrdersListFragment payLaterOrdersListFragment = PayLaterOrdersListFragment.newInstance(binding.frameContainerOrder.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerOrder.getId(), payLaterOrdersListFragment);
        transaction.addToBackStack(PayLaterOrdersListFragment.getTAG());
        transaction.commit();
    }

}