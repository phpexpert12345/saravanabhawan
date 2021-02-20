package com.harperskebab.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityReferBinding;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class ReferActivity extends BaseActivity {
    private static final String TAG = "ReferActivity";

    private ActivityReferBinding binding;
    private UserViewModel userViewModel;

    private String referralSharingMessage;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userViewModel = ViewModelFactory.getInstance(this).create(UserViewModel.class);

        fragmentManager = getSupportFragmentManager();

        setupView();
    }


    private void onClick(View v) {

        if (v.getId() == binding.imageViewWhatsapp.getId()) {
            share();
        } else if (v.getId() == binding.imageViewFacebook.getId()) {
            share();
        } else if (v.getId() == binding.imageViewTwitter.getId()) {
            share();
        } else if (v.getId() == binding.imageViewShare.getId()) {
            share();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {

        userViewModel.getSignInResponse().observe(this, signInResponse -> {
            if (signInResponse != null) {
                if (signInResponse.getSuccess() == 0) {
                    referralSharingMessage = signInResponse.getReferralSharingMessage();
                    binding.textViewReferralMessage.setText(signInResponse.getReferralCodeMessage());
                    binding.buttonReferralCode.setText(signInResponse.getReferralCode());
                }
            }

        });

        binding.imageViewWhatsapp.setOnClickListener(this::onClick);
        binding.imageViewFacebook.setOnClickListener(this::onClick);
        binding.imageViewTwitter.setOnClickListener(this::onClick);
        binding.imageViewShare.setOnClickListener(this::onClick);

    }

    private void share() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, referralSharingMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            Log.e(TAG, "share: Exception: ", e);
        }
    }


}