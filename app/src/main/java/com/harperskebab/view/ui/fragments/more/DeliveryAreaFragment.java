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

import com.harperskebab.databinding.FragmentDeliveryAreaBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.DeliveryAreaAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.DeliveryAreaViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class DeliveryAreaFragment extends BaseFragment {
    private static final String TAG = "DeliveryAreaFragment";

    private FragmentDeliveryAreaBinding binding;
    private DeliveryAreaViewModel deliveryAreaViewModel;

    private int containerID;
    String BranchId;

    public DeliveryAreaFragment() {
    }

    public static DeliveryAreaFragment newInstance(int containerID) {

        DeliveryAreaFragment fragment = new DeliveryAreaFragment();
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
        deliveryAreaViewModel = ViewModelFactory.getInstance(getActivity()).create(DeliveryAreaViewModel.class);
        deliveryAreaViewModel.getDeliveryArea(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "1", BranchId,new NetworkOperations(true));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getDeliveryArea());
        binding = FragmentDeliveryAreaBinding.inflate(inflater, container, false);

        binding.recyclerViewDeliveryArea.setLayoutManager(new LinearLayoutManager(getActivity()));
        deliveryAreaViewModel.getDeliveryAreas().observe(this, deliveryAreas -> {

            if (deliveryAreas != null) {
                DeliveryAreaAdapter deliveryAreaAdapter = new DeliveryAreaAdapter(getActivity(), deliveryAreas, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));

                binding.recyclerViewDeliveryArea.setAdapter(deliveryAreaAdapter);

                deliveryAreaViewModel.getDeliveryAreas().removeObservers(this);
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