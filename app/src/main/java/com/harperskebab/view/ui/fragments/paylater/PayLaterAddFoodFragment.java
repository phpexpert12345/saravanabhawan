package com.harperskebab.view.ui.fragments.paylater;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FragmentPayLaterAddFoodBinding;
import com.harperskebab.model.Food;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodAdapter;
import com.harperskebab.view.adapter.FoodCategoryHorizontalAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.FoodCategoryViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.harperskebab.viewmodel.paylater.PayLaterFoodViewModel;

import java.util.Set;

public class PayLaterAddFoodFragment extends BaseFragment {
    private static final String TAG = "PayLaterAddFoodFragment";
    String restId;
    private FragmentPayLaterAddFoodBinding binding;

    private FoodCategoryViewModel foodCategoryViewModel;
    private PayLaterFoodViewModel payLaterFoodViewModel;

    private FoodAdapter foodAdapter;
    private int containerID;
    private int foodCategoryPosition = 0;
String branchId;
    public PayLaterAddFoodFragment() {
    }

    public static PayLaterAddFoodFragment newInstance(int containerID, int foodCategoryPosition) {

        PayLaterAddFoodFragment fragment = new PayLaterAddFoodFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putInt(Constant.Data.FOOD_CATEGORY_POSITION, foodCategoryPosition);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            foodCategoryPosition = getArguments().getInt(Constant.Data.FOOD_CATEGORY_POSITION);
        }

        foodCategoryViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodCategoryViewModel.class);
        payLaterFoodViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterFoodViewModel.class);
        branchId= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId", "");
        payLaterFoodViewModel.getFood(getActivity(),branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getId(),restId, new NetworkOperations(true));

        setHasOptionsMenu(true);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPayLaterAddFoodBinding.inflate(inflater, container, false);
         restId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("RestaurentId", "");
        Glide.with(getActivity())
                .load(foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getCategoryImg())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(binding.imageViewPayLatFoodCat);
        binding.layBackPaylater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        foodCategoryViewModel.getFoodCategories().observe(this, foodCategories -> {

            FoodCategoryHorizontalAdapter foodCategoryHorizontalAdapter = new FoodCategoryHorizontalAdapter(
                    getActivity(), foodCategories, foodCategoryPosition,
                    (position, foodCategory) -> {
                        foodCategoryPosition = position;
                        Glide.with(getActivity())
                                .load(foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getCategoryImg())
                                .placeholder(R.drawable.loading)
                                .centerCrop()
                                .into(binding.imageViewPayLatFoodCat);
                        payLaterFoodViewModel.getFood(getActivity(),branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodCategory.getId(),restId, new NetworkOperations(true));
                    });

            binding.recyclerViewFoodCategory.setAdapter(foodCategoryHorizontalAdapter);
            binding.recyclerViewFoodCategory.getLayoutManager().scrollToPosition(foodCategoryPosition);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerViewFood.getContext(), LinearLayoutManager.VERTICAL);
        binding.recyclerViewFood.addItemDecoration(dividerItemDecoration);

        payLaterFoodViewModel.getFoods().observe(this, foods -> {
            if (foods != null) {

                foodAdapter = new FoodAdapter(getContext(), foods, onPlusClick, onMinusClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));

                binding.recyclerViewFood.setAdapter(foodAdapter);
            }
        });

    }

    FoodAdapter.OnPlusClick onPlusClick = new FoodAdapter.OnPlusClick() {
        @Override
        public void onClick(int position, Food food) {

            payLaterFoodViewModel.setCurrentSelectedFood(food);
            onPlusFood();
        }
    };

    private void onPlusFood() {
        if (payLaterFoodViewModel.getCurrentSelectedFood().getValue().getFoodCount() == 0
                && (payLaterFoodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES") || payLaterFoodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES"))) {
            if (payLaterFoodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES")) {
                initiatePayLaterAddFoodItemFragment();
            } else if (payLaterFoodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES")) {
                initiateFoodItemExtraToppingFragment();
            }
        } else {
            payLaterFoodViewModel.getCurrentSelectedFood().getValue().setFoodCount(payLaterFoodViewModel.getCurrentSelectedFood().getValue().getFoodCount() + 1);
            foodAdapter.notifyDataSetChanged();
            Set<Food> selectedFoods = payLaterFoodViewModel.getSelectedFoods().getValue();
            if (!selectedFoods.contains(payLaterFoodViewModel.getCurrentSelectedFood().getValue())) {
                selectedFoods.add(payLaterFoodViewModel.getCurrentSelectedFood().getValue());
            }
            payLaterFoodViewModel.setSelectedFoods(selectedFoods);
        }
    }

    FoodAdapter.OnMinusClick onMinusClick = new FoodAdapter.OnMinusClick() {
        @Override
        public void onClick(int position, Food food) {
            food.setFoodCount(food.getFoodCount() - 1);
            foodAdapter.notifyDataSetChanged();

            Set<Food> selectedFoods = payLaterFoodViewModel.getSelectedFoods().getValue();
            if (food.getFoodCount() == food.getFoodCountLimit()) {
                selectedFoods.remove(food);
            }
            payLaterFoodViewModel.setSelectedFoods(selectedFoods);
        }
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt(Constant.Data.FOOD_CATEGORY_POSITION, foodCategoryPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constant.Data.FOOD_CATEGORY_POSITION)) {
                foodCategoryPosition = savedInstanceState.getInt(Constant.Data.FOOD_CATEGORY_POSITION);
            }
        }
    }

    private void initiatePayLaterAddFoodItemFragment() {
        PayLaterAddFoodItemFragment payLaterAddFoodItemFragment = PayLaterAddFoodItemFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, payLaterAddFoodItemFragment);
        transaction.addToBackStack(PayLaterAddFoodItemFragment.getTAG());
        transaction.commit();
    }

    private void initiateFoodItemExtraToppingFragment() {
        PayLaterAddFoodItemExtraToppingFragment payLaterAddFoodItemExtraToppingFragment = PayLaterAddFoodItemExtraToppingFragment.newInstance(containerID, null);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, payLaterAddFoodItemExtraToppingFragment);
        transaction.addToBackStack(PayLaterAddFoodItemExtraToppingFragment.getTAG());
        transaction.commit();
    }

    public static String getTAG() {
        return TAG;
    }

}
