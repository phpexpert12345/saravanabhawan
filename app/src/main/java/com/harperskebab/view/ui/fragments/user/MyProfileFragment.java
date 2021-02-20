package com.harperskebab.view.ui.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FragmentMyProfileBinding;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.ui.activities.AddressActivity;
import com.harperskebab.view.ui.activities.LoyaltyActivity;
import com.harperskebab.view.ui.activities.OrderActivity;
import com.harperskebab.view.ui.activities.TicketActivity;
import com.harperskebab.view.ui.activities.user.ChangePasswordActivity;
import com.harperskebab.view.ui.activities.user.MyProfileEditActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class MyProfileFragment extends BaseFragment {
    private static final String TAG = "MyProfileFragment";

    private FragmentMyProfileBinding binding;

    private UserViewModel userViewModel;
    private int containerID;

    public MyProfileFragment() {

    }

    public static MyProfileFragment newInstance(int containerID) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
        }
        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getMyProfile());
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
binding.txtMyProfilw.setText(languageViewModel.getLanguageResponse().getValue().getMyProfile());
binding.txtMyOrder.setText(languageViewModel.getLanguageResponse().getValue().getMyOrder());
binding.txtPasswordSecurity.setText(languageViewModel.getLanguageResponse().getValue().getPasswordSecurity());
binding.txtMyAddress.setText(languageViewModel.getLanguageResponse().getValue().getMyAddress());
binding.txtLoyalityPoint.setText(languageViewModel.getLanguageResponse().getValue().getLoyaltyPoints());
binding.txtMyTicket.setText(languageViewModel.getLanguageResponse().getValue().getMyTicket());
binding.txtLogout.setText(languageViewModel.getLanguageResponse().getValue().getLogout());
        userViewModel.getSignInResponse().observe(this, signInResponse -> {

            if (signInResponse != null && signInResponse.getSuccess() == 0) {

                Glide.with(getActivity())
                        .load(signInResponse.getUserPhoto())
                        .placeholder(R.drawable.user)
                        .into(binding.imageViewUser);

                binding.textViewUserName.setText(String.format("%s %s", signInResponse.getFirstName(), signInResponse.getLastName()).replace("null", "").replace("  ", " "));
            }

        });


        binding.cardViewMyProfile.setOnClickListener(this::onClick);
        binding.cardViewYourOrder.setOnClickListener(this::onClick);
        binding.cardViewPasswordAndSecurity.setOnClickListener(this::onClick);
        binding.cardViewMyAddress.setOnClickListener(this::onClick);
        binding.cardViewLoyaltyPoints.setOnClickListener(this::onClick);
        binding.cardViewMyTickets.setOnClickListener(this::onClick);
        binding.cardViewLogout.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    private void onClick(View v) {

        if (v.getId() == binding.cardViewMyProfile.getId()) {
            initiateMyProfileEditActivity();
        } else if (v.getId() == binding.cardViewYourOrder.getId()) {
            startActivity(
                    new Intent(getActivity(), OrderActivity.class)
            );
        } else if (v.getId() == binding.cardViewPasswordAndSecurity.getId()) {
            startActivity(
                    new Intent(getActivity(), ChangePasswordActivity.class)
            );
        } else if (v.getId() == binding.cardViewMyAddress.getId()) {
            startActivity(
                    new Intent(getActivity(), AddressActivity.class)
            );
        } else if (v.getId() == binding.cardViewLoyaltyPoints.getId()) {
            startActivity(
                    new Intent(getActivity(), LoyaltyActivity.class)
            );
        } else if (v.getId() == binding.cardViewMyTickets.getId()) {
            startActivity(
                    new Intent(getActivity(), TicketActivity.class)
            );
        } else if (v.getId() == binding.cardViewLogout.getId()) {
            userViewModel.setSignInResponse(null);
            getActivity().finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateMyProfileEditActivity() {
        startActivity(
                new Intent(getActivity(), MyProfileEditActivity.class)
        );
    }

    @Override
    public boolean onBackPressed(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                goBack();
                return true;
            }
        }
        return false;
    }
}
