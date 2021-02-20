package com.harperskebab.view.ui.activities.user;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.ActivityMyProfileBinding;
import com.harperskebab.view.ui.activities.BaseActivity;
import com.harperskebab.view.ui.fragments.user.MyProfileFragment;

public class MyProfileActivity extends BaseActivity {

    private ActivityMyProfileBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
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

        initiateMyProfileFragment();

    }

    private void initiateMyProfileFragment() {

        MyProfileFragment myProfileFragment = MyProfileFragment.newInstance(binding.frameContainerProfile.getId());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerProfile.getId(), myProfileFragment);
        transaction.addToBackStack(MyProfileFragment.getTAG());
        transaction.commit();
    }

}