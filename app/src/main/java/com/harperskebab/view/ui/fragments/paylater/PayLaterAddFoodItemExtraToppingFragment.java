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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.databinding.FragmentPayLaterAddFoodItemExtraToppingBinding;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItem;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodItemExtraToppingAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.FoodItemExtraToppingViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.harperskebab.viewmodel.paylater.PayLaterFoodViewModel;

import java.util.List;
import java.util.Set;

public class PayLaterAddFoodItemExtraToppingFragment extends BaseFragment {
    private static final String TAG = "PayLaterAddFoodItemExtr";

    private FragmentPayLaterAddFoodItemExtraToppingBinding binding;

    private PayLaterFoodViewModel payLaterFoodViewModel;
    private FoodItemExtraToppingViewModel foodItemExtraToppingViewModel;
    private Food selectedfood;
    private FoodItem selectedFoodItem;
    private int containerID;
    private int freeToppingSelectionAllowed;

    public PayLaterAddFoodItemExtraToppingFragment() {
    }

    public static PayLaterAddFoodItemExtraToppingFragment newInstance(int containerID, FoodItem foodItem) {

        PayLaterAddFoodItemExtraToppingFragment fragment = new PayLaterAddFoodItemExtraToppingFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putSerializable(Constant.Data.FOOD_ITEM, foodItem);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            selectedFoodItem = (FoodItem) getArguments().getSerializable(Constant.Data.FOOD_ITEM);
        }
        payLaterFoodViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterFoodViewModel.class);
        foodItemExtraToppingViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodItemExtraToppingViewModel.class);


        selectedfood = payLaterFoodViewModel.getCurrentSelectedFood().getValue();

        if (selectedfood.getFoodItem() == null) {
            foodItemExtraToppingViewModel.getFoodItemExtraTopping(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                    "" + selectedfood.getItemID(), "",
                    new NetworkOperations(true));
        } else {
            foodItemExtraToppingViewModel.getFoodItemExtraTopping(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                    "" + selectedfood.getItemID(), "" + selectedfood.getFoodItem().getId(),
                    new NetworkOperations(true));
        }


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPayLaterAddFoodItemExtraToppingBinding.inflate(inflater, container, false);

        binding.textViewFood.setText(selectedfood.getRestaurantPizzaItemName());
        binding.textViewFoodDescription.setText(selectedfood.getResPizzaDescription());

        binding.recyclerViewFoodItemExtraTopping.setLayoutManager(new LinearLayoutManager(getActivity()));

        foodItemExtraToppingViewModel.getFoodItemExtraTopping().observe(this, foodItemExtraToppings -> {

            if (foodItemExtraToppings != null) {

                if (foodItemExtraToppings.get(0).getFreeToppingSelectionAllowed().equalsIgnoreCase("")) {
                    freeToppingSelectionAllowed = 0;
                } else {
                    freeToppingSelectionAllowed = Integer.parseInt(foodItemExtraToppings.get(0).getFreeToppingSelectionAllowed());
                }
                binding.textViewExtraToppingOffer.setText("Choose Any " + freeToppingSelectionAllowed + " Topping Free");

                FoodItemExtraToppingAdapter foodItemExtraToppingAdapter = new FoodItemExtraToppingAdapter(
                        getActivity(), foodItemExtraToppings, onItemClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                );
                binding.recyclerViewFoodItemExtraTopping.setAdapter(foodItemExtraToppingAdapter);
            }

        });

        binding.buttonAdd.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    private FoodItemExtraToppingAdapter.OnItemClick onItemClick = (position, foodItemExtraTopping) -> {

        FoodItemExtraToppingAdapter foodItemExtraToppingAdapter = (FoodItemExtraToppingAdapter) binding.recyclerViewFoodItemExtraTopping.getAdapter();

        List<FoodItemExtraTopping> selectedFoodItemExtraToppings = foodItemExtraToppingAdapter.getSelectedFoodItemExtraToppings();

        if (selectedFoodItemExtraToppings.size() > freeToppingSelectionAllowed) {

            Double price = 0.0;
            for (int i = freeToppingSelectionAllowed; i < selectedFoodItemExtraToppings.size(); i++) {
                FoodItemExtraTopping extraTopping = selectedFoodItemExtraToppings.get(i);
                price += Double.parseDouble(extraTopping.getFoodPriceAddons());
            }

            binding.textViewTotalPrice.setText(
                    Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                            + " " +
                            Utility.DECIMAL_FORMAT.format(price)
            );
        } else {
            binding.textViewTotalPrice.setText(
                    Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                            + " " +
                            Utility.DECIMAL_FORMAT.format(0.0)
            );
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

        if (v.getId() == binding.buttonAdd.getId()) {

//            FoodItemExtraToppingAdapter adapter = (FoodItemExtraToppingAdapter) binding.recyclerViewFoodItemExtraTopping.getAdapter();
//            payLaterFoodViewModel.getCurrentSelectedFood().getValue().setFoodItem(selectedFoodItem);
//            payLaterFoodViewModel.getCurrentSelectedFood().getValue().setFoodItemExtraToppings(adapter.getSelectedFoodItemExtraToppings());
//
//            payLaterFoodViewModel.getCurrentSelectedFood().getValue().setFoodCount(1);
//            Set<Food> selectedFoods = payLaterFoodViewModel.getSelectedFoods().getValue();
//            selectedFoods.add(payLaterFoodViewModel.getCurrentSelectedFood().getValue());
//            payLaterFoodViewModel.setSelectedFoods(selectedFoods);
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("ITEM_COUNT", String.valueOf(payLaterFoodViewModel.getSelectedFoods().getValue().size()));
//            editor.apply();
            goBack();

        }

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
