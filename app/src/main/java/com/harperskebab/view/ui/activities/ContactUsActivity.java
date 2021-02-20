package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.ActivityContactUsBinding;
import com.harperskebab.view.ui.fragments.more.ContactUsFragment;

public class ContactUsActivity extends BaseActivity {
    private static final String TAG = "ContactUsActivity";

    private ActivityContactUsBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
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

        initiateContactUsFragment();

    }

    private void initiateContactUsFragment() {
        ContactUsFragment contactUsFragment = ContactUsFragment.newInstance(binding.frameContainerContactUs.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerContactUs.getId(), contactUsFragment);
        transaction.addToBackStack(ContactUsFragment.getTAG());
        transaction.commit();
    }

}