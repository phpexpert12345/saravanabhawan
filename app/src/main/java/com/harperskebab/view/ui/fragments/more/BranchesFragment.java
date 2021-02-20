package com.harperskebab.view.ui.fragments.more;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.databinding.FragmentBranchesBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.BranchViewAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.BranchViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class BranchesFragment extends BaseFragment {
    private static final String TAG = "BranchesFragment";

    private FragmentBranchesBinding binding;
    private BranchViewModel branchViewModel;

    private int containerID;
    String BranchId;

    public BranchesFragment() {
    }

    public static BranchesFragment newInstance(int containerID) {

        BranchesFragment fragment = new BranchesFragment();
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
        branchViewModel = ViewModelFactory.getInstance(getActivity()).create(BranchViewModel.class);
        branchViewModel.getBranch(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getBranches());
        binding = FragmentBranchesBinding.inflate(inflater, container, false);
        BranchId=  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId","2");
        binding.recyclerViewBranchLocation.setLayoutManager(new LinearLayoutManager(getActivity()));

        branchViewModel.getRestaurantBranches().observe(this, restaurantBranches -> {

            if (restaurantBranches != null) {
                BranchViewAdapter branchAdapter = new BranchViewAdapter(getActivity(), restaurantBranches);
                binding.recyclerViewBranchLocation.setAdapter(branchAdapter);
                branchViewModel.getRestaurantBranches().removeObservers(this);
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