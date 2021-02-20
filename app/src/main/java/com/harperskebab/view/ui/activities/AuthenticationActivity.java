package com.harperskebab.view.ui.activities;

import android.os.Bundle;

import com.harperskebab.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends BaseActivity {

    private ActivityAuthenticationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


}