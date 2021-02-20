package com.harperskebab.view.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.FragmentLoyaltyBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.ui.activities.MapActivity;
import com.harperskebab.viewmodel.LoyaltyPointListViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class LoyaltyFragment extends BaseFragment {
    private static final String TAG = "ContactUsFragment";

    private FragmentLoyaltyBinding binding;

    private LoyaltyPointListViewModel loyaltyPointListViewModel;

    private int containerID;

    public LoyaltyFragment() {
    }

    public static LoyaltyFragment newInstance(int containerID) {

        LoyaltyFragment fragment = new LoyaltyFragment();
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
        loyaltyPointListViewModel = ViewModelFactory.getInstance(getActivity()).create(LoyaltyPointListViewModel.class);
        loyaltyPointListViewModel.getTotalLoyaltyPoint(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getLoyaltyPoints());
        binding = FragmentLoyaltyBinding.inflate(inflater, container, false);

        loyaltyPointListViewModel.getLoyaltyPointListResponse().observe(this, loyaltyPointListResponse -> {

            if (loyaltyPointListResponse != null) {
                binding.textViewSignUpLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getSignPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewPlaceFirstOrderLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getPlaceFirstOrdersPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewPostingReviewLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getPostReviewPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewGroupOrderingLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getPlaceGroupOrderingPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewBirthdayCelebrationsLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getBirthdayCelebrationsPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewSocialSharingLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getSocialMediaSharingPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewSpendEarnLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getSpendMoreThanPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewReferFriendLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getReferFriendsPoints(), languageViewModel.getLanguageResponse().getValue().getPoints()));
                binding.textViewPerOrderLoyaltyPoint.setText(String.format("%d %s", loyaltyPointListResponse.getPerOrderLoyalityPoint(), languageViewModel.getLanguageResponse().getValue().getPoints()));

                loyaltyPointListViewModel.getLoyaltyPointListResponse().removeObservers(this);
            }

        });

        return binding.getRoot();
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

    public void onClick(View v) {

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

    public static String getTAG() {
        return TAG;
    }

}