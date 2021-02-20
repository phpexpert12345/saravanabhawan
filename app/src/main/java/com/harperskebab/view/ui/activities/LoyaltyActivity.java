package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.ActivityLoyaltyBinding;
import com.harperskebab.view.ui.fragments.LoyaltyFragment;

public class LoyaltyActivity extends BaseActivity {
    private static final String TAG = "LoyaltyActivity";

    private ActivityLoyaltyBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoyaltyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        initiateLoyaltyFragment();

    }

    private void initiateLoyaltyFragment() {
        LoyaltyFragment loyaltyFragment = LoyaltyFragment.newInstance(binding.frameContainerLoyalty.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerLoyalty.getId(), loyaltyFragment);
        transaction.addToBackStack(LoyaltyFragment.getTAG());
        transaction.commit();
    }

}