package com.harperskebab.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityCartBinding;
import com.harperskebab.databinding.ActivityNewBranchBinding;
import com.harperskebab.view.ui.fragments.BranchFragment;
import com.harperskebab.view.ui.fragments.cart.CartFragment;

public class NewBranchActivity extends BaseActivity {
    private ActivityNewBranchBinding binding;
    private FragmentManager fragmentManager = null;
    double strLong=0.0,strLat=0.0,currentLat,currentLong;
    String address;
    boolean isType=false;
    boolean isFromHome=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewBranchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");
        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        binding.header.textViewTitle.setText("Branches");
        isType=getIntent().getBooleanExtra("TYPE",false);
        isFromHome=getIntent().getBooleanExtra("isFromHome",false);
        if (isType) {
            strLat = getIntent().getDoubleExtra("LATITUDE", 0.0);
            strLong = getIntent().getDoubleExtra("LONGITUDE", 0.0);
            currentLat = getIntent().getDoubleExtra("CURRENT_LAT", 0.0);
            currentLong = getIntent().getDoubleExtra("CURRENT_LONG", 0.0);
            address = getIntent().getStringExtra("ADDRESS");
            binding.textViewAddresss.setText(address);
        }else {
            binding.textViewAddresss.setVisibility(View.GONE);
        }
        binding.textViewAddresss.setOnClickListener(this::onClick);
        fragmentManager = getSupportFragmentManager();

//        Bundle bundle = new Bundle();
//        bundle.putDouble("LATITUDE", strLat);
//        bundle.putDouble("LONGITUDE", strLat);
        BranchFragment branchFragment = BranchFragment.newInstance(binding.frameContainerCart.getId(),address,strLat,strLong,currentLat,currentLong,isFromHome);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerCart.getId(), branchFragment);
        transaction.addToBackStack(CartFragment.getTAG());
        transaction.commit();
    }

    private void onClick(View v) {
        if (v.getId() == binding.textViewAddresss.getId()) {
            Intent intent=new Intent(NewBranchActivity.this,MapActivity.class);
            startActivity(intent);
            finish();
        }
    }
}