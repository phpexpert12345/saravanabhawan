package com.harperskebab.view.ui.fragments.more;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.FragmentOpeningHourBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.OpeningHourViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class OpeningHourFragment extends BaseFragment {
    private static final String TAG = "OpeningHourFragment";

    private FragmentOpeningHourBinding binding;
    private OpeningHourViewModel openingHourViewModel;
    String BranchId;

    private int containerID;

    public OpeningHourFragment() {
    }

    public static OpeningHourFragment newInstance(int containerID) {

        OpeningHourFragment fragment = new OpeningHourFragment();
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
        BranchId=  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId","2");
        openingHourViewModel = ViewModelFactory.getInstance(getActivity()).create(OpeningHourViewModel.class);
        openingHourViewModel.getOpeningHours(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true),BranchId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getOpeningHours());
        binding = FragmentOpeningHourBinding.inflate(inflater, container, false);

        openingHourViewModel.getOpeningHourResponse().observe(this, openingHourResponse -> {

            if (openingHourResponse != null) {
                binding.constraintOpeningHour.setVisibility(View.VISIBLE);
                if (openingHourResponse.getSuccess().equals("0")) {
                    binding.textViewMonday.setText(openingHourResponse.getMondayText());
                    binding.textViewTuesday.setText(openingHourResponse.getTuesdayText());
                    binding.textViewWednesday.setText(openingHourResponse.getWednesdayText());
                    binding.textViewThursday.setText(openingHourResponse.getThursdayText());
                    binding.textViewFriday.setText(openingHourResponse.getFridayText());
                    binding.textViewSaturday.setText(openingHourResponse.getSaturdayText());
                    binding.textViewSunday.setText(openingHourResponse.getSundayText());

                    openingHourViewModel.getOpeningHourResponse().removeObservers(this);
                }
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