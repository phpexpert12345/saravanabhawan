package com.harperskebab.view.ui.fragments.paylater;

import android.content.SharedPreferences;
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

import com.harperskebab.databinding.FragmentPayLaterAddFoodItemBinding;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItem;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodItemAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.view.ui.fragments.FoodItemFragment;
import com.harperskebab.viewmodel.FoodItemViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.harperskebab.viewmodel.paylater.PayLaterFoodViewModel;

import java.util.Collections;
import java.util.Set;

public class PayLaterAddFoodItemFragment extends BaseFragment {
    private static final String TAG = "PayLaterFoodItemFragmen";

    private FragmentPayLaterAddFoodItemBinding binding;

    private PayLaterFoodViewModel payLaterFoodViewModel;
    private FoodItemViewModel foodItemViewModel;
    private int containerID;

    public PayLaterAddFoodItemFragment() {
    }

    public static PayLaterAddFoodItemFragment newInstance(int containerID) {

        PayLaterAddFoodItemFragment fragment = new PayLaterAddFoodItemFragment();
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
        payLaterFoodViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterFoodViewModel.class);
        foodItemViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodItemViewModel.class);
        foodItemViewModel.getFoodItem(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + payLaterFoodViewModel.getCurrentSelectedFood().getValue().getItemID(), new NetworkOperations(true));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPayLaterAddFoodItemBinding.inflate(inflater, container, false);

        binding.textViewFood.setText(payLaterFoodViewModel.getCurrentSelectedFood().getValue().getRestaurantPizzaItemName());
        binding.textViewFoodDescription.setText(payLaterFoodViewModel.getCurrentSelectedFood().getValue().getResPizzaDescription());

        binding.recyclerViewFoodItem.setLayoutManager(new LinearLayoutManager(getActivity()));

        foodItemViewModel.getFoodItems().observe(this, foodItems -> {

            Collections.sort(foodItems, new FoodItem.PriceComparator());

            FoodItemAdapter foodItemAdapter = new FoodItemAdapter(getActivity(), foodItems, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()), PayLaterAddFoodItemFragment.this,true);
            binding.recyclerViewFoodItem.setAdapter(foodItemAdapter);

        });

        binding.buttonAdd.setOnClickListener(this::onClick);

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

        if (v.getId() == binding.buttonAdd.getId()) {

            FoodItemAdapter adapter = (FoodItemAdapter) binding.recyclerViewFoodItem.getAdapter();
            FoodItem selectedFoodItem = adapter.getselectedFoodItem();

            if (selectedFoodItem.getExtraavailable().equalsIgnoreCase("YES")) {
                goBack();
                initiateFoodItemExtraToppingFragment(selectedFoodItem);
            } else {

                Food selectedFood = payLaterFoodViewModel.getCurrentSelectedFood().getValue();
                selectedFood.setFoodItem(selectedFoodItem);

                selectedFood.setFoodCount(1);
                Set<Food> selectedFoods = payLaterFoodViewModel.getSelectedFoods().getValue();
                selectedFoods.add(selectedFood);
                payLaterFoodViewModel.setSelectedFoods(selectedFoods);
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("ITEM_COUNT", String.valueOf(payLaterFoodViewModel.getSelectedFoods().getValue().size()));
//                editor.apply();
                goBack();
            }

        }

    }

    private void initiateFoodItemExtraToppingFragment(FoodItem foodItem) {
        PayLaterAddFoodItemExtraToppingFragment payLaterAddFoodItemExtraToppingFragment = PayLaterAddFoodItemExtraToppingFragment.newInstance(containerID, foodItem);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, payLaterAddFoodItemExtraToppingFragment);
        transaction.addToBackStack(PayLaterAddFoodItemExtraToppingFragment.getTAG());
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
    public void fragmentMethod(String s) {
        binding.textTotalPrice.setText(s);
    }
}
