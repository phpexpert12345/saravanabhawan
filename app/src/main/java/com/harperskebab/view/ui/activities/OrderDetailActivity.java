package com.harperskebab.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityNewBranchBinding;
import com.harperskebab.databinding.ActivityOrderDetailBinding;
import com.harperskebab.view.ui.fragments.BranchFragment;
import com.harperskebab.view.ui.fragments.cart.CartFragment;
import com.harperskebab.view.ui.fragments.order.MyOrderDetailFragment;

public class OrderDetailActivity extends BaseActivity {
    private ActivityOrderDetailBinding binding;
    private FragmentManager fragmentManager = null;
    String orderId;
    int containerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_detail);

        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("");
        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        intent.putExtra("CONTAINER_ID",containerID);
//        intent.putExtra("ORDER_ID",orderID);
        containerId=getIntent().getIntExtra("CONTAINER_ID",0);
        orderId=getIntent().getStringExtra("ORDER_ID");
        fragmentManager = getSupportFragmentManager();
        MyOrderDetailFragment myOrderDetailFragment = MyOrderDetailFragment.newInstance(containerId,orderId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(binding.frameContainerOdetail.getId(), myOrderDetailFragment);
        transaction.addToBackStack(MyOrderDetailFragment.getTAG());
        transaction.commit();
    }
}