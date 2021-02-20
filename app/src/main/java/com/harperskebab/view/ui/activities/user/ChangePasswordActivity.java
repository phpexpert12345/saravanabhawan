package com.harperskebab.view.ui.activities.user;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.ActivityChangePasswordBinding;
import com.harperskebab.view.ui.activities.BaseActivity;
import com.harperskebab.view.ui.fragments.user.ChangePasswordFragment;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
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

        ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance(binding.frameContainerChangePassword.getId());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerChangePassword.getId(), changePasswordFragment);
        transaction.addToBackStack(ChangePasswordFragment.getTAG());
        transaction.commit();
    }

}