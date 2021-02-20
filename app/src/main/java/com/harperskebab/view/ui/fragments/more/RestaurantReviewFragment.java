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

import com.bumptech.glide.Glide;
import com.harperskebab.databinding.FragmentRestaurantReviewBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.RestaurantReviewAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.RestaurantReviewViewModel;
import com.harperskebab.viewmodel.RestaurantViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class RestaurantReviewFragment extends BaseFragment {
    private static final String TAG = "RestaurantReviewFragmen";

    private FragmentRestaurantReviewBinding binding;
    private RestaurantViewModel restaurantViewModel;
    private RestaurantReviewViewModel restaurantReviewViewModel;

    private int containerID;
    String BranchId;

    public RestaurantReviewFragment() {
    }

    public static RestaurantReviewFragment newInstance(int containerID) {

        RestaurantReviewFragment fragment = new RestaurantReviewFragment();
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
        restaurantViewModel = ViewModelFactory.getInstance(getActivity()).create(RestaurantViewModel.class);
        restaurantReviewViewModel = ViewModelFactory.getInstance(getActivity()).create(RestaurantReviewViewModel.class);
        restaurantReviewViewModel.getReviews(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true),BranchId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getRestaurantReviews());
        binding = FragmentRestaurantReviewBinding.inflate(inflater, container, false);


        restaurantViewModel.getRestaurant().observe(this, restaurant -> {

            if (restaurant != null) {

                Glide.with(getActivity()).load(restaurant.getRestaurantCover()).into(binding.imageViewRestaurant);

                binding.textViewRestaurantName.setText(restaurant.getRestaurantName());
                binding.textViewRestaurantAddress.setText(restaurant.getRestaurantAddress());

                restaurantViewModel.getRestaurant().removeObservers(this);
            }

        });

        binding.recyclerViewRestaurantReviews.setLayoutManager(new LinearLayoutManager(getActivity()));

        restaurantReviewViewModel.getReviews().observe(this, restaurantReviews -> {

            if (restaurantReviews != null) {

                RestaurantReviewAdapter restaurantReviewAdapter = new RestaurantReviewAdapter(getActivity(), restaurantReviews);

                binding.recyclerViewRestaurantReviews.setAdapter(restaurantReviewAdapter);

                restaurantReviewViewModel.getReviews().removeObservers(this);
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