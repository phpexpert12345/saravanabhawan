package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.ActivityAddressBinding;
import com.harperskebab.view.ui.fragments.AddressFragment;

public class AddressActivity extends BaseActivity {

    private ActivityAddressBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
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

        initiateAddressFragment();

    }

    private void initiateAddressFragment() {
        AddressFragment addressFragment = AddressFragment.newInstance(binding.frameContainerAddress.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerAddress.getId(), addressFragment);
        transaction.addToBackStack(AddressFragment.getTAG());
        transaction.commit();
    }

}