package com.harperskebab.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityTrackingBinding;
import com.harperskebab.view.ui.fragments.BranchFragment;
import com.harperskebab.view.ui.fragments.cart.CartFragment;
import com.harperskebab.view.ui.fragments.order.OrderTrackFragment;

public class TrackingActivity extends AppCompatActivity {
    private ActivityTrackingBinding binding;
    private FragmentManager fragmentManager = null;
    double strLong = 0.0, strLat = 0.0, currentLat, currentLong;
    String address, orderId;
    int containerId;
    boolean isType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");
        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragmentManager = getSupportFragmentManager();
//        intent.putExtra("CONTAINER_ID",containerID);
//        intent.putExtra("ORDER_ID",orderID);
        containerId = getIntent().getIntExtra("CONTAINER_ID",0);
        orderId = getIntent().getStringExtra("ORDER_ID");
//        Bundle bundle = new Bundle();
//        bundle.putDouble("LATITUDE", strLat);
//        bundle.putDouble("LONGITUDE", strLat);
        OrderTrackFragment branchFragment = OrderTrackFragment.newInstance(containerId,orderId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameTracking.getId(), branchFragment);
        transaction.addToBackStack(CartFragment.getTAG());
        transaction.commit();
    }
}