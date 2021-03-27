package com.harperskebab.view.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.R;
import com.harperskebab.databinding.FragmentBranchBinding;
import com.harperskebab.model.RestaurantBranch;
import com.harperskebab.model.RestaurantBranchAfterShort;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.BranchAdapter;
import com.harperskebab.view.ui.activities.HomeActivity;
import com.harperskebab.view.ui.activities.MainActivity;
import com.harperskebab.view.ui.activities.NewBranchActivity;
import com.harperskebab.viewmodel.BranchViewModel;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BranchFragment extends BaseFragment {
    private static final String TAG = "BranchFragment";

    private FragmentBranchBinding binding;

    private CartViewModel cartViewModel;
    private BranchViewModel branchViewModel;
    private BranchAdapter branchAdapter;
    private String bramchId;
    private String branch_address;
    private String branch_name;
    double strLong, strLat,currentLat,currentLong;
    private int containerID;
    boolean isFromHome=false;
    ArrayList<RestaurantBranchAfterShort> restaurantBranchAfterShorts;
String address;
    public BranchFragment() {
    }

    public static BranchFragment newInstance(int containerID,String address, double Lat, double Long,double crntLat, double crntLong,boolean isFromHome) {

        BranchFragment fragment = new BranchFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putDouble("LATITUDE", Lat);
        args.putDouble("LONGITUDE", Long);
        args.putDouble("CURRENT_LAT", crntLat);
        args.putDouble("CURRENT_LONG", crntLong);
        args.putString("ADDRESS",address);
        args.putBoolean("isFromHome",isFromHome);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            strLat = getArguments().getDouble("LATITUDE");
            strLong = getArguments().getDouble("LONGITUDE");
            currentLat = getArguments().getDouble("CURRENT_LAT");
            currentLong = getArguments().getDouble("CURRENT_LONG");
            address=getArguments().getString("ADDRESS");
            isFromHome=getArguments().getBoolean("isFromHome");
        }
        cartViewModel = ViewModelFactory.getInstance(getActivity()).create(CartViewModel.class);
        branchViewModel = ViewModelFactory.getInstance(getActivity()).create(BranchViewModel.class);
        branchViewModel.getBranch(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.branch));
//        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getBranches());
        binding = FragmentBranchBinding.inflate(inflater, container, false);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("BranchId", " ").apply();
        binding.recyclerViewBranchLocation.setLayoutManager(new LinearLayoutManager(getActivity()));

        branchViewModel.getRestaurantBranches().observe(this, restaurantBranches -> {
            if (restaurantBranches != null) {
                bramchId = String.valueOf(restaurantBranches.get(0).getId());
                branch_address=restaurantBranches.get(0).getRestaurantBranchAddress();
                branch_name=restaurantBranches.get(0).getRestaurantBranchName();
                restaurantBranchAfterShorts = new ArrayList<>();
                for (int i = 0; i < restaurantBranches.size(); i++) {
//                    RestaurantBranchAfterShort restaurantBranchAfterShort = new RestaurantBranchAfterShort();
//                    restaurantBranchAfterShort.setRestaurantBranchName(restaurantBranches.get(i).getRestaurantBranchName());
//                    restaurantBranchAfterShort.setRestaurantBranchName(restaurantBranches.get(i).getBranchMobile());
                    double branchLat=Double.parseDouble(restaurantBranches.get(i).getBranchLatitude());
                    double branchLong=Double.parseDouble(restaurantBranches.get(i).getBranchLongitude());
                    double distance=distance(strLat,strLong,branchLat,branchLong);
                    String str=String.valueOf(distance);
                    float miles = convertKmsToMiles(Float.parseFloat(str));
                    restaurantBranches.get(i).setDistance(miles);
                }
                Collections.sort(restaurantBranches, new Comparator<RestaurantBranch>() {
                    @Override
                    public int compare(RestaurantBranch lhs, RestaurantBranch rhs) {
                        return Double.compare( lhs.getDistance(),rhs.getDistance());
                    }
                });

                branchAdapter = new BranchAdapter(getActivity(), strLat, strLong,currentLat,currentLong, restaurantBranches, onLocationPinClick);
                binding.recyclerViewBranchLocation.setAdapter(branchAdapter);
            }

        });
        binding.buttonConfirmLocation.setOnClickListener(this::onClick);

        return binding.getRoot();
    }
    public float convertKmsToMiles(float kms){
        float miles = (float) (0.621371 * kms);
        return miles;
    }
    private BranchAdapter.OnLocationPinClick onLocationPinClick = (position, restaurantBranch) -> {

        bramchId = restaurantBranch.getId().toString();
        branch_address=restaurantBranch.getRestaurantBranchAddress();
        branch_name=restaurantBranch.getRestaurantBranchName();
    };

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

        if (v.getId() == binding.buttonConfirmLocation.getId()) {

//            cartViewModel.setSelectedRestaurantBranch(branchAdapter.getselectedRestaurantBranch());
            String orderType = cartViewModel.getOrderType().getValue();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("BRANCH_NAME",branch_name).apply();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("BRANCH_ADDRESS", branch_address).apply();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("BranchId", bramchId).apply();

            if (isFromHome){
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }else {
                startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("BRANCH_ID", bramchId).putExtra("BRANCH_NAME", bramchId));
                getActivity().finish();
            }
//            if (orderType.equalsIgnoreCase("delivery")) {
//                initiateAddressFragment();
//            } else if (orderType.equalsIgnoreCase("pickup") || orderType.equalsIgnoreCase("eat-in")) {
//                initiatePaymentFragment();
//            }
        }

    }

    private void initiateAddressFragment() {
        AddressFragment addressFragment = AddressFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, addressFragment);
        transaction.addToBackStack(addressFragment.getTag());
        transaction.commit();
    }

    private void initiatePaymentFragment() {
        PaymentFragment paymentFragment = PaymentFragment.newInstance(containerID);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, paymentFragment);
        transaction.addToBackStack(paymentFragment.getTag());
        transaction.commit();
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


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
