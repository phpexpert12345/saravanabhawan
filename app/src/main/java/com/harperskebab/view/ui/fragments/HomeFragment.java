package com.harperskebab.view.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.harperskebab.databinding.FragmentHomeBinding;
import com.harperskebab.model.FoodCategory;
import com.harperskebab.model.FrontBanner;
import com.harperskebab.model.Table;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.FoodCategoryAdapter;
import com.harperskebab.view.adapter.TableAdapter;
import com.harperskebab.view.adapter.ViewPagerSlideAdapter;
import com.harperskebab.view.ui.activities.AddressFromPostCodeActivity;
import com.harperskebab.view.ui.activities.HomeActivity;
import com.harperskebab.view.ui.activities.MenuActivity;
import com.harperskebab.view.ui.activities.NewBranchActivity;
import com.harperskebab.viewmodel.FoodCategoryViewModel;
import com.harperskebab.viewmodel.FrontBannerViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private FragmentHomeBinding binding;

    private FoodCategoryViewModel foodCategoryViewModel;
    private FrontBannerViewModel frontBannerViewModel;
    private int containerID;
    private String branchId, branchName;
    List<FoodCategory> arrFoodCategories;
    List<FoodCategory> arrCategoryFilter;
    public HomeFragment() {
    }

    public static HomeFragment newInstance(int containerID, String branchId) {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putString("branchId", branchId);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            branchId = getArguments().getString("branchId");
        }
        frontBannerViewModel = ViewModelFactory.getInstance(getActivity()).create(FrontBannerViewModel.class);
        foodCategoryViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodCategoryViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
//        branchName  = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BRANCH_NAME", "");
//       String branchAddress  = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BRANCH_ADDRESS", "");
//        binding.txtBranchId.setText(branchName);
//        binding.txtBranchName.setText(branchAddress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        binding.recyclerViewFoodCategory.setLayoutManager(manager);
        frontBannerViewModel.getFrontBanner().observe(this, frontBanners -> {
            if (frontBanners != null) {
                setViewPager(frontBanners);
            }
        });
        binding.changeBranch.setOnClickListener(v->{
            Intent intent=new Intent(getActivity(), NewBranchActivity.class);
            intent.putExtra("isFromHome",true);
          getActivity() .startActivityForResult(intent,34);
        });

        binding.edtCatSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                arrCategoryFilter= new ArrayList<>();
                if (value.length() > 0) {
                    for (int i = 0; i < arrFoodCategories.size(); i++) {
                        if (arrFoodCategories.get(i).getCategoryName().toLowerCase().contains(value)) {
                            FoodCategory foodCategory=new FoodCategory();
                            foodCategory.setCategoryID(arrFoodCategories.get(i).getCategoryID());
                            foodCategory.setCategoryDescription(arrFoodCategories.get(i).getCategoryDescription());
                            foodCategory.setCategoryImg(arrFoodCategories.get(i).getCategoryImg());
                            foodCategory.setCategoryName(arrFoodCategories.get(i).getCategoryName());
                            foodCategory.setCombo_Available(arrFoodCategories.get(i).getCombo_Available());
                            foodCategory.setFavoritesDisplay(arrFoodCategories.get(i).getFavoritesDisplay());
                            foodCategory.setError(arrFoodCategories.get(i).getError());
                            foodCategory.setId(arrFoodCategories.get(i).getId());
                            foodCategory.setRestaurantId(arrFoodCategories.get(i).getRestaurantId());
                            foodCategory.setScObjId(arrFoodCategories.get(i).getScObjId());
                            arrCategoryFilter.add(foodCategory);
                        }
                    }
                    if (arrCategoryFilter.size()>0) {
                        FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(getContext(), arrCategoryFilter, (position, foodCategory) -> {

                            Intent intent = new Intent(getActivity(), MenuActivity.class);
                            intent.putExtra(Constant.Data.FOOD_CATEGORY_POSITION, position);
                            intent.putExtra("FOOD_CATEGORY_COMBO", foodCategory.getCombo_Available());
                            startActivity(intent);

                        });
                        binding.recyclerViewFoodCategory.setAdapter(foodCategoryAdapter);
                        binding.recyclerViewFoodCategory.setVisibility(View.VISIBLE);
                    }else {
//                        binding.layNoItemFound.setVisibility(View.VISIBLE);
                        binding.recyclerViewFoodCategory.setVisibility(View.GONE);
                    }
                } else {
                    FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(getContext(), arrFoodCategories, (position, foodCategory) -> {

                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        intent.putExtra(Constant.Data.FOOD_CATEGORY_POSITION, position);
                        intent.putExtra("FOOD_CATEGORY_COMBO", foodCategory.getCombo_Available());
                        startActivity(intent);

                    });

                    binding.recyclerViewFoodCategory.setAdapter(foodCategoryAdapter);
                    binding.recyclerViewFoodCategory.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

        });
        binding.imgChangePostCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), NewBranchActivity.class);
                intent.putExtra("isFromHome",true);
                startActivity(intent);
            }
        });
        foodCategoryViewModel.getFoodCategories().observe(this, foodCategories -> {
            if (foodCategories != null) {
                if(foodCategories.size()>0){
                    if(foodCategories.get(0).getError()==1){
                        binding.txtNoCategories.setVisibility(View.VISIBLE);
                        binding.imgNoCategories.setVisibility(View.VISIBLE);
                        binding.changeBranch.setVisibility(View.VISIBLE);
                        binding.txtNoCategories.setText(foodCategories.get(0).getError_msg());
                        binding.recyclerViewFoodCategory.setVisibility(View.GONE);
                    }
                    else{
                        binding.recyclerViewFoodCategory.setVisibility(View.VISIBLE);
                        binding.txtNoCategories.setVisibility(View.GONE);
                        binding.changeBranch.setVisibility(View.GONE);
                        binding.imgNoCategories.setVisibility(View.GONE);
                        arrFoodCategories=foodCategories;
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("RestaurentId", foodCategories.get(0).getRestaurantId()).apply();
                        FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(getContext(), arrFoodCategories, (position, foodCategory) -> {
                            Intent intent = new Intent(getActivity(), MenuActivity.class);
                            intent.putExtra(Constant.Data.FOOD_CATEGORY_POSITION, position);
                            intent.putExtra("FOOD_CATEGORY_COMBO", foodCategory.getCombo_Available());
                            startActivity(intent);

                        });

                        binding.recyclerViewFoodCategory.setAdapter(foodCategoryAdapter);
                    }
                }

            }
            else{
                binding.txtNoCategories.setVisibility(View.VISIBLE);
                binding.recyclerViewFoodCategory.setVisibility(View.GONE);
                binding.changeBranch.setVisibility(View.VISIBLE);
                binding.imgNoCategories.setVisibility(View.VISIBLE);
            }
        });


        return binding.getRoot();
    }

    public void onClick(View v) {

    }

    private void setViewPager(List<FrontBanner> frontBanners) {
        ViewPagerSlideAdapter viewPagerAdapter = new ViewPagerSlideAdapter(getContext(), frontBanners);
        binding.viewPagerBestFood.setAdapter(viewPagerAdapter);
        binding.wormDotsIndicatorBestFood.setViewPager(binding.viewPagerBestFood);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == frontBanners.size()) {
                    currentPage = 0;
                }
                binding.viewPagerBestFood.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
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
