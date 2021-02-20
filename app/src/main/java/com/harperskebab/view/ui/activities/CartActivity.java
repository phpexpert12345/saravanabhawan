package com.harperskebab.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.harperskebab.databinding.ActivityCartBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItem;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.view.ui.fragments.cart.CartFragment;
import com.harperskebab.view.ui.fragments.cart.EmptyCartFragment;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ReOrderViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartActivity extends BaseActivity {
    private ReOrderViewModel reOrderViewModel;
    private ActivityCartBinding binding;
    boolean isFromReOrder = false;
    String orderNumber;
    private CartViewModel cartViewModel;
    private FoodViewModel foodViewModel;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(languageViewModel.getLanguageResponse().getValue().getCart());


        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        foodViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(FoodViewModel.class);

        fragmentManager = getSupportFragmentManager();

        cartViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(CartViewModel.class);
        isFromReOrder = getIntent().getBooleanExtra("ISFROMREORDER", false);
        if (isFromReOrder) {
            orderNumber = getIntent().getStringExtra("OrderNumber");
        }
        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(this);
        CartDao cartDao=customerAppDatabase.cartDao();
        List<CartItem>cartItems=cartDao.getCartItems();
        if(cartItems.size()>0){
            initiateCartFragment(isFromReOrder, orderNumber);
        }
        else{
            initiateEmptyCartFragment();
        }
//        foodViewModel.getSelectedFoods().observe(this, foods -> {
//
//            boolean orderedFoodFlag = true;
//
//            if (cartViewModel.getFoods().getValue() == null) {
//                orderedFoodFlag = false;
//            } else if (cartViewModel.getFoods().getValue().size() == 0) {
//                orderedFoodFlag = false;
//            }
//
//            if (foods != null || orderedFoodFlag) {
//                if (foods.size() > 0 || orderedFoodFlag) {
//                    initiateCartFragment(isFromReOrder, orderNumber);
//                } else {
//                    if (isFromReOrder) {
//                        initiateCartFragment(isFromReOrder, orderNumber);
//                    } else {
//                        initiateEmptyCartFragment();
//                    }
//                }
//            } else {
//                if (isFromReOrder) {
//                    initiateCartFragment(isFromReOrder, orderNumber);
//                } else {
//                    initiateEmptyCartFragment();
//                }
//            }
//
//            foodViewModel.getSelectedFoods().removeObservers(this);
//
//        });

    }

    private void initiateEmptyCartFragment() {
        EmptyCartFragment emptyCartFragment = EmptyCartFragment.newInstance(binding.frameContainerCart.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerCart.getId(), emptyCartFragment);
        transaction.addToBackStack(EmptyCartFragment.getTAG());
        transaction.commit();
    }

    private void initiateCartFragment(boolean isFromReOrder, String orderNumber) {
//        if (isFromReOrder) {
//            reOrderViewModel.getReorder(this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, orderNumber, new NetworkOperations(true));
//        }

        reOrderViewModel = ViewModelFactory.getInstance(CartActivity.this).create(ReOrderViewModel.class);
        if (isFromReOrder) {
            reOrderViewModel.getReorder(CartActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, orderNumber, new NetworkOperations(true));

            reOrderViewModel.getOrderDetailResponse().observe(this, orderDetailResponse -> {
                //todo
                if (orderDetailResponse != null) {
                    ArrayList<Food> cartFoods = new ArrayList<>();
                    Set<Food> selectedFoods = new HashSet<>();
                    for (int i = 0; i < orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().size(); i++) {
                        Food food = new Food();
                        food.setRestaurantPizzaItemName(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getItemsName());
                        String strItemId = String.valueOf(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getId());
                        food.setItemID(Long.parseLong(strItemId));
                        food.setFoodCount(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getQuantity());
                        food.setRestaurantPizzaItemPrice(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getRestaurantPizzaItemPrice());
                        food.setRestaurantPizzaItemOldPrice(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getRestaurantPizzaItemOldPrice());
                        food.setRestaurantPizzaItemSizeName(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getRestaurantPizzaItemSizeName());
                        food.setFoodIcon(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getFoodIcon());
                        food.setRestaurantPizzaItemSizeName(orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getRestaurantPizzaItemSizeName());
                        if (!orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getExtraTopping().equalsIgnoreCase("0")) {
                            List<FoodItemExtraTopping> foodExtraToppings = new ArrayList<>();
                            String extraToppingName = orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getExtraTopping();
                            String extraToppingId = orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getExtraToppingID();
                            String extraToppingPrice = orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem().get(i).getExtraprice();
                            String[] itemsExtraName = extraToppingName.split(",");
                            String[] itemsExtraId = extraToppingId.split(",");
                            String[] itemsExtraPrice = extraToppingPrice.split(",");
                            for (int j = 0; j < itemsExtraName.length; j++) {
                                FoodItemExtraTopping foodItemExtraTopping = new FoodItemExtraTopping();
                                foodItemExtraTopping.setFoodAddonsName(itemsExtraName[i]);
                                foodItemExtraTopping.setExtraID(Long.parseLong(itemsExtraId[i]));
                                foodItemExtraTopping.setFoodPriceAddons(itemsExtraPrice[i]);
                                foodExtraToppings.add(foodItemExtraTopping);
                            }
                            food.setFoodItemExtraToppings(foodExtraToppings);
                        }
                        //                        cartFoods.add(food);
                        selectedFoods.add(food);
//                        if (!selectedFoods.contains(foodViewModel.getCurrentSelectedFood().getValue())) {
//                            selectedFoods.add(foodViewModel.getCurrentSelectedFood().getValue());
//                        }
                    }
//                    for (int i=0;i<cartFoods.size();i++){
//                        foodViewModel.setSelectedFoods(cartFoods.get(i));
//                    }
                    foodViewModel.setSelectedFoods(selectedFoods);
                    CartFragment cartFragment = CartFragment.newInstance(binding.frameContainerCart.getId(), isFromReOrder, orderNumber);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(binding.frameContainerCart.getId(), cartFragment);
                    transaction.addToBackStack(CartFragment.getTAG());
                    transaction.commit();
                }
            });
        } else {
            CartFragment cartFragment = CartFragment.newInstance(binding.frameContainerCart.getId(), isFromReOrder, orderNumber);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(binding.frameContainerCart.getId(), cartFragment);
            transaction.addToBackStack(CartFragment.getTAG());
            transaction.commit();
        }
    }

}