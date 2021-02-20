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

import com.harperskebab.databinding.FragmentOfferBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.RestaurantOfferAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.RestaurantOfferViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class OfferFragment extends BaseFragment {
    private static final String TAG = "OfferFragment";

    private FragmentOfferBinding binding;
    private RestaurantOfferViewModel restaurantOfferViewModel;

    private int containerID;
    String BranchId;

    public OfferFragment() {
    }

    public static OfferFragment newInstance(int containerID) {

        OfferFragment fragment = new OfferFragment();
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
        restaurantOfferViewModel = ViewModelFactory.getInstance(getActivity()).create(RestaurantOfferViewModel.class);
        restaurantOfferViewModel.getRestaurantOffers(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true),BranchId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getOffer());
        binding = FragmentOfferBinding.inflate(inflater, container, false);

        binding.recyclerViewOffer.setLayoutManager(new LinearLayoutManager(getActivity()));
        restaurantOfferViewModel.getRestaurantDiscountCoupon().observe(this, restaurantDiscountCoupons -> {

            if (restaurantDiscountCoupons != null) {
                //todo
                RestaurantOfferAdapter restaurantOfferAdapter = new RestaurantOfferAdapter(getActivity(), restaurantDiscountCoupons);
                binding.recyclerViewOffer.setAdapter(restaurantOfferAdapter);

                restaurantOfferViewModel.getRestaurantDiscountCoupon().removeObservers(this);
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