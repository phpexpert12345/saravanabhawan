package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.ActivityForgetPasswordBinding;
import com.harperskebab.view.ui.fragments.auth.signin.ForgetPasswordFragment;

public class ForgetPasswordActivity extends BaseActivity {
    private static final String TAG = "ForgetPasswordActivity";

    private ActivityForgetPasswordBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
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

        initiateForgetPasswordFragment();

    }

    private void initiateForgetPasswordFragment() {
        ForgetPasswordFragment forgetPasswordFragment = ForgetPasswordFragment.newInstance(binding.frameContainerForgetPassword.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerForgetPassword.getId(), forgetPasswordFragment);
        transaction.addToBackStack(ForgetPasswordFragment.getTAG());
        transaction.commit();
    }

}