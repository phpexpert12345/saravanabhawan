package com.harperskebab.view.ui.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.databinding.FragmentFoodItemBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItem;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodItemAdapter;
import com.harperskebab.viewmodel.FoodItemViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.Collections;
import java.util.Set;

public class FoodItemFragment extends BaseFragment {
    private static final String TAG = "FoodItemFragment";

    private FragmentFoodItemBinding binding;

    private FoodViewModel foodViewModel;
    private FoodItemViewModel foodItemViewModel;
    private int containerID;

    public FoodItemFragment() {
    }

    public static FoodItemFragment newInstance(int containerID) {

        FoodItemFragment fragment = new FoodItemFragment();
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
        foodViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodViewModel.class);
        foodItemViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodItemViewModel.class);
        foodItemViewModel.getFoodItem(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodViewModel.getCurrentSelectedFood().getValue().getItemID(), new NetworkOperations(true));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoodItemBinding.inflate(inflater, container, false);

        binding.textViewFood.setText(foodViewModel.getCurrentSelectedFood().getValue().getRestaurantPizzaItemName());
        binding.textViewFoodDescription.setText(foodViewModel.getCurrentSelectedFood().getValue().getResPizzaDescription());

        binding.recyclerViewFoodItem.setLayoutManager(new LinearLayoutManager(getActivity()));

        foodItemViewModel.getFoodItems().observe(this, foodItems -> {

            Collections.sort(foodItems, new FoodItem.PriceComparator());

            FoodItemAdapter foodItemAdapter = new FoodItemAdapter(getActivity(), foodItems, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()),FoodItemFragment.this, false);
            binding.recyclerViewFoodItem.setAdapter(foodItemAdapter);

        });

        binding.buttonAddToCart.setOnClickListener(this::onClick);

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

        if (v.getId() == binding.buttonAddToCart.getId()) {

            FoodItemAdapter adapter = (FoodItemAdapter) binding.recyclerViewFoodItem.getAdapter();
            FoodItem selectedFoodItem = adapter.getselectedFoodItem();

            if (selectedFoodItem.getExtraavailable().equalsIgnoreCase("YES")) {
                goBack();
                initiateFoodItemExtraToppingFragment(selectedFoodItem);
            } else {
                SetFoodtoDatabase(2);
//                Food selectedFood = foodViewModel.getCurrentSelectedFood().getValue();
//                selectedFood.setFoodItem(selectedFoodItem);
//
//                selectedFood.setFoodCount(1);
//                Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
//                selectedFoods.add(selectedFood);
//                foodViewModel.setSelectedFoods(selectedFoods);

                goBack();
            }

        }

    }

    private void initiateFoodItemExtraToppingFragment(FoodItem foodItem) {
        FoodItemExtraToppingFragment foodItemExtraToppingFragment = FoodItemExtraToppingFragment.newInstance(containerID, foodItem);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, foodItemExtraToppingFragment);
        transaction.addToBackStack(FoodItemExtraToppingFragment.getTAG());
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
        binding.textViewTotalPrice.setText(s);
    }

    private void SetFoodtoDatabase(Integer type){
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao=customerAppDatabase.cartDao();
        Food food= foodViewModel.getCurrentSelectedFood().getValue();
        if(food!=null) {
            CartItem cart = cartDao.getOrderItemids(food.getRestaurantPizzaItemName(), "");
            if (cart != null) {
                Integer quantity = cart.quantity;
                if(quantity==0){
                    cartDao.DeleteCartitem(food.getRestaurantPizzaItemName(), "");
                }
                else {
                    if (type == 0) {


                        quantity -= 1;

                    } else {
                        quantity += 1;
                    }
                    cart.quantity = quantity;
                    cartDao.Update(cart);
                }

            } else {
                CartItem cartItem = new CartItem();
                cartItem.item_id = food.getItemID();
                cartItem.item_name = food.getRestaurantPizzaItemName();
                cartItem.item_size_type = food.getRestaurantPizzaItemSizeName();
                cartItem.item_price = food.getRestaurantPizzaItemPrice();
                cartItem.total_price = food.getRestaurantPizzaItemPrice();
                cartItem.deal_id=food.getItemID();
                cartItem.quantity = 1;
                cartItem.com = false;
                cartItem.top_ids = "";
                cartItem.top_name="";
                cartItem.top_price="";
                cartItem.item_size_id = "0";
                cartItem.icon=food.getFoodIcon();
                cartItem.desc = food.getResPizzaDescription();
                cartDao.Insert(cartItem);
            }

        }


    }

}
