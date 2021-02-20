package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.ActivityOrderBinding;
import com.harperskebab.view.ui.fragments.order.MyOrdersListFragment;

public class OrderActivity extends BaseActivity {

    private ActivityOrderBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
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

        initiateMyOrdersListFragment();

    }

    private void initiateMyOrdersListFragment() {
        MyOrdersListFragment myOrdersListFragment = MyOrdersListFragment.newInstance(binding.frameContainerOrder.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerOrder.getId(), myOrdersListFragment);
        transaction.addToBackStack(MyOrdersListFragment.getTAG());
        transaction.commit();
    }

}