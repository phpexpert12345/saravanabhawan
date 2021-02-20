package com.harperskebab.view.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogOrderTypeBinding;
import com.harperskebab.databinding.FragmentFoodBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.ComboFoodAdapter;
import com.harperskebab.view.adapter.FoodAdapter;
import com.harperskebab.view.adapter.FoodCategoryHorizontalAdapter;
import com.harperskebab.view.ui.activities.CartActivity;
import com.harperskebab.view.ui.activities.MapActivity;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.EatInViewModel;
import com.harperskebab.viewmodel.FoodCategoryViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.PostalCodeViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.notbytes.barcode_reader.BarcodeReaderActivity;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class FoodFragment extends BaseFragment {
    public static final String TAG = "FoodFragment";

    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1000;

    private FragmentFoodBinding binding;

    private DialogOrderTypeBinding orderTypeBinding;
    private BottomSheetDialog dialogOrderType;

    private FoodCategoryViewModel foodCategoryViewModel;
    private FoodViewModel foodViewModel;
    private CartViewModel cartViewModel;
    private PostalCodeViewModel postalCodeViewModel;
    private EatInViewModel eatInViewModel;
    private String branchId;
    private FoodAdapter foodAdapter;
    private ComboFoodAdapter comboFoodAdapter;
    private int containerID;
    private String foodCombo;
    private int foodCategoryPosition = 0;
    double totalAmount=0.0;

    public FoodFragment() {
    }

    public static FoodFragment newInstance(int containerID, int foodCategoryPosition, String foodCombo) {

        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putString("FOOD_COMBO", foodCombo);
        args.putInt(Constant.Data.FOOD_CATEGORY_POSITION, foodCategoryPosition);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            foodCombo = getArguments().getString("FOOD_COMBO");
            foodCategoryPosition = getArguments().getInt(Constant.Data.FOOD_CATEGORY_POSITION);
        }
        foodCategoryViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodCategoryViewModel.class);
        foodViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodViewModel.class);
        cartViewModel = ViewModelFactory.getInstance(getActivity()).create(CartViewModel.class);
        postalCodeViewModel = ViewModelFactory.getInstance(getActivity()).create(PostalCodeViewModel.class);
        eatInViewModel = ViewModelFactory.getInstance(getActivity()).create(EatInViewModel.class);
        branchId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId", "");
        if (foodCombo.equalsIgnoreCase("Yes")) {
            foodViewModel.getComboFood(getActivity(), "2", Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getRestaurantId(), foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getCategoryID().toString(), new NetworkOperations(true));
        } else {
            foodViewModel.getFood(getActivity(), "2", Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getId(),foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getRestaurantId(),new NetworkOperations(false));
        }
//        foodViewModel.getFood(getActivity(), branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getId(), new NetworkOperations(true));

        setHasOptionsMenu(true);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getMenu());
        binding = FragmentFoodBinding.inflate(inflater, container, false);
        Glide.with(getActivity())
                .load(foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getCategoryImg())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(binding.imageViewFFoodCat);
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
                                .into(binding.imageViewFFoodCat);
                        if (foodCategory.getCombo_Available().equalsIgnoreCase("Yes")) {
                            binding.recFoodCombo.setVisibility(View.VISIBLE);
                            foodViewModel.getComboFood(getActivity(), branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getRestaurantId(), foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getCategoryID().toString(), new NetworkOperations(true));
                        } else {
                            binding.recFoodCombo.setVisibility(View.GONE);
                            showProgress();
                            foodViewModel.getFood(getActivity(), "2", Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getId(),foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getRestaurantId(), new NetworkOperations(false));
                        }
//                        foodViewModel.getFood(getActivity(), branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodCategory.getId(), new NetworkOperations(true));
                    });

            binding.recyclerViewFoodCategory.setAdapter(foodCategoryHorizontalAdapter);
            binding.recyclerViewFoodCategory.getLayoutManager().scrollToPosition(foodCategoryPosition);
        });
        binding.recFoodCombo.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
//        DividerItemDecoration dividerItemDecorationCombo = new DividerItemDecoration(binding.recyclerViewFood.getContext(), LinearLayoutManager.VERTICAL);
//        binding.recFoodCombo.addItemDecoration(dividerItemDecorationCombo);
        binding.recFoodCombo.setNestedScrollingEnabled(false);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerViewFood.getContext(), LinearLayoutManager.VERTICAL);
//        binding.recyclerViewFood.addItemDecoration(dividerItemDecoration);
        binding.recyclerViewFood.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        foodViewModel.getGetComboListResponse().observe(this, comboListResponse -> {
            if (comboListResponse != null) {

                comboFoodAdapter = new ComboFoodAdapter(getContext(), comboListResponse.getComboList(), Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));
                binding.recFoodCombo.setAdapter(comboFoodAdapter);
                binding.recFoodCombo.setVisibility(View.VISIBLE);
                foodViewModel.getFood(getActivity(), branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getId(),foodCategoryViewModel.getFoodCategories().getValue().get(foodCategoryPosition).getRestaurantId(), new NetworkOperations(false));
            }
        });

       showProgress();
        foodViewModel.getFoods().observe(this, foods -> {
            if (foods != null) {
               hideProgress();

                foodAdapter = new FoodAdapter(getContext(), foods, onPlusClick, onMinusClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));

                binding.recyclerViewFood.setAdapter(foodAdapter);
                binding.recyclerViewFood.setVisibility(View.VISIBLE);
            }
        });
getCartItems();
//        foodViewModel.getSelectedFoods().observe(this, foods -> {
//            if (foods==null||foods.size()==0) {
//                binding.constraintLayoutCart.setVisibility(View.GONE);
//            } else {
//                binding.constraintLayoutCart.setVisibility(View.VISIBLE);
//            }
//            int totalItems = 0;
//            Double totalAmount = 0.00D;
//            if (foods != null) {
//                for (Food food : foods) {
//                    totalItems = totalItems + food.getFoodCount();
//
//                    Double tmpTotalAmount = 0.0;
//                    if (food.getFoodItem() == null) {
//                        tmpTotalAmount = (Double.parseDouble(food.getRestaurantPizzaItemPrice()) * food.getFoodCount());
//                    } else {
//                        tmpTotalAmount = (Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice()) * food.getFoodCount());
//                    }
//
//                    if (food.getFoodItemExtraToppings() == null) {
//                        totalAmount += tmpTotalAmount;
//                    } else {
//                        Double totalFoodItemExtraToppingPrice = 0.0;
//
////                        int freeToppingSelectionAllowed = food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed().equals("") ? 0 : Integer.parseInt(food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed());
//
//                        for (int i = 0; i < food.getFoodItemExtraToppings().size(); i++) {
//                            totalFoodItemExtraToppingPrice += Double.parseDouble(food.getFoodItemExtraToppings().get(i).getFoodPriceAddons());
//                        }
//
//                        totalAmount += tmpTotalAmount + totalFoodItemExtraToppingPrice;
//
//                    }
//                }
//
//
//                binding.textViewTotalItem.setText(totalItems + " items");
//
//                binding.textViewTotalAmount.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(totalAmount));
//            }
//        });
//
        binding.constraintLayoutCart.setOnClickListener(this::onClick);

    }

    FoodAdapter.OnPlusClick onPlusClick = new FoodAdapter.OnPlusClick() {
        @Override
        public void onClick(int position, Food food) {

            foodViewModel.setCurrentSelectedFood(food);
            if (restaurantViewModel.getRestaurant().getValue().getHomeDeliveryAvailable().equalsIgnoreCase("yes")
                    || restaurantViewModel.getRestaurant().getValue().getPickupAvailable().equalsIgnoreCase("yes")
                    || restaurantViewModel.getRestaurant().getValue().getDineInAvailable().equalsIgnoreCase("yes")
            ) {
                binding.constraintLayoutCart.setVisibility(View.VISIBLE);
                if (food.getFoodCount() == food.getFoodCountLimit() && (cartViewModel.getOrderType().getValue() == null || cartViewModel.getOrderType().getValue().equals(""))) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String orderName = preferences.getString("ORDERTYPE", "");
                    AtomicReference<String> orderType = new AtomicReference<>("");
                    orderType.set(orderName);
                    cartViewModel.setOrderType(orderType.get());
                    if (orderName.equals("")) {
                        showOrderTypeDialog();
                    } else {
                        onPlusFood();
                    }
                } else {
                    onPlusFood();
                }
            } else {
                onPlusFood();
            }
        }
    };

    private void onPlusFood() {
        if (foodViewModel.getCurrentSelectedFood().getValue().getFoodCount() == 0
                && (foodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES")
                || foodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES"))) {
            if (foodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES")) {
                initiateFoodItemFragment();
            } else if (foodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES")) {
                initiateFoodItemExtraToppingFragment();
            }
        } else {
            foodViewModel.getCurrentSelectedFood().getValue().setFoodCount(foodViewModel.getCurrentSelectedFood().getValue().getFoodCount() + 1);
            foodAdapter.notifyDataSetChanged();
            SetFoodtoDatabase(2);
//            Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
//            if (!selectedFoods.contains(foodViewModel.getCurrentSelectedFood().getValue())) {
//                selectedFoods.add(foodViewModel.getCurrentSelectedFood().getValue());
//            }
//            foodViewModel.setSelectedFoods(selectedFoods);
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("I" +
//                    "TEM_COUNT", String.valueOf(foodViewModel.getSelectedFoods().getValue().size()));
//            editor.apply();
        }
    }

    FoodAdapter.OnMinusClick onMinusClick = new FoodAdapter.OnMinusClick() {
        @Override
        public void onClick(int position, Food food) {

            if (food.getFoodCount() > food.getFoodCountLimit()) {
                food.setFoodCount(food.getFoodCount() - 1);
                foodAdapter.notifyDataSetChanged();

//                Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
//                if (food.getFoodCount() == 0) {
//                    selectedFoods.remove(food);
//                }
//                if (selectedFoods.size() == 0) {
//                    binding.constraintLayoutCart.setVisibility(View.GONE);
//                }
//                foodViewModel.setSelectedFoods(selectedFoods);
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("ITEM_COUNT", String.valueOf(foodViewModel.getSelectedFoods().getValue().size()));
//                editor.apply();
                SetFoodtoDatabase(0);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        if (v.getId() == binding.constraintLayoutCart.getId()) {
            Log.i("log","makes");
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("ITEM_COUNT", String.valueOf(foodViewModel.getSelectedFoods().getValue().size()));
//            editor.apply();
            startActivity(new Intent(getActivity(), CartActivity.class));
        }
        else{
            Log.i("log","error");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
                PopMessage.makeLongToast(getActivity(), barcode.rawValue);
                initiateTableSelectionFragment(barcode.rawValue);
            } else {
                orderTypeBinding.seatSelection.radioButtonScanQRCode.setChecked(false);
            }
        }
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


    private void initiateTableSelectionFragment(String tableID) {
        TableSelectionFragment tableSelectionFragment = TableSelectionFragment.newInstance(containerID, tableID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, tableSelectionFragment);
        transaction.addToBackStack(TableSelectionFragment.getTAG());
        transaction.commit();
    }

    private void initiateFoodItemFragment() {
        FoodItemFragment foodItemFragment = FoodItemFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, foodItemFragment);
        transaction.addToBackStack(FoodItemFragment.getTAG());
        transaction.commit();
    }

    private void initiateFoodItemExtraToppingFragment() {
        FoodItemExtraToppingFragment foodItemExtraToppingFragment = FoodItemExtraToppingFragment.newInstance(containerID, foodViewModel.getCurrentSelectedFood().getValue().getFoodItem());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, foodItemExtraToppingFragment);
        transaction.addToBackStack(FoodItemExtraToppingFragment.getTAG());
        transaction.commit();
    }

    private void showOrderTypeDialog() {
        AtomicReference<String> orderType = new AtomicReference<>("");

        orderTypeBinding = DialogOrderTypeBinding.inflate(getLayoutInflater());
        dialogOrderType = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogOrderType.setContentView(orderTypeBinding.getRoot());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderName = preferences.getString("ORDERTYPE", "");
        if (orderName.equalsIgnoreCase("Delivery") && cartViewModel.getOrderType().getValue() == null) {
            orderType.set("Delivery");
            orderTypeBinding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            orderTypeBinding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorWhite));

            orderTypeBinding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
        } else if (orderName.equalsIgnoreCase("Pickup") && cartViewModel.getOrderType().getValue() == null) {
            orderType.set("PickUp");
            orderTypeBinding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            orderTypeBinding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorWhite));

            orderTypeBinding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.postalCode.linearLayoutPostalCode.setVisibility(View.GONE);
            orderTypeBinding.seatSelection.linearLayoutSeatSelection.setVisibility(View.GONE);
            orderTypeBinding.buttonSubmit.setVisibility(View.VISIBLE);
        } else if (orderName.equalsIgnoreCase("EAT-IN") && cartViewModel.getOrderType().getValue() == null) {
            orderTypeBinding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            orderTypeBinding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorWhite));

            orderTypeBinding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.seatSelection.linearLayoutSeatSelection.setVisibility(View.VISIBLE);
            orderTypeBinding.postalCode.linearLayoutPostalCode.setVisibility(View.GONE);
            orderTypeBinding.buttonSubmit.setVisibility(View.GONE);
        }

        if (!restaurantViewModel.getRestaurant().getValue().getHomeDeliveryAvailable().equalsIgnoreCase("yes")) {
            orderTypeBinding.orderType.cardViewDelivery.setVisibility(View.GONE);
        }

        if (!restaurantViewModel.getRestaurant().getValue().getPickupAvailable().equalsIgnoreCase("yes")) {
            orderTypeBinding.orderType.cardViewPickUp.setVisibility(View.GONE);
        }

        if (!restaurantViewModel.getRestaurant().getValue().getDineInAvailable().equalsIgnoreCase("yes")) {
            orderTypeBinding.orderType.cardViewEatIn.setVisibility(View.GONE);
        }

        orderTypeBinding.orderType.textViewDelivery.setOnClickListener(v -> {
            orderType.set("Delivery");
            orderTypeBinding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            orderTypeBinding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorWhite));

            orderTypeBinding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.postalCode.linearLayoutPostalCode.setVisibility(View.VISIBLE);
            orderTypeBinding.seatSelection.linearLayoutSeatSelection.setVisibility(View.GONE);
            orderTypeBinding.buttonSubmit.setVisibility(View.VISIBLE);

        });

        orderTypeBinding.orderType.textViewPickUp.setOnClickListener(v -> {
            orderType.set("PickUp");
            orderTypeBinding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            orderTypeBinding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorWhite));

            orderTypeBinding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.postalCode.linearLayoutPostalCode.setVisibility(View.GONE);
            orderTypeBinding.seatSelection.linearLayoutSeatSelection.setVisibility(View.GONE);
            orderTypeBinding.buttonSubmit.setVisibility(View.VISIBLE);

        });

        orderTypeBinding.orderType.textViewEatIn.setOnClickListener(v -> {
            orderType.set("EAT-IN");
            orderTypeBinding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            orderTypeBinding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorWhite));

            orderTypeBinding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            orderTypeBinding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
            setTextViewDrawableColor(orderTypeBinding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

            orderTypeBinding.seatSelection.linearLayoutSeatSelection.setVisibility(View.VISIBLE);
            orderTypeBinding.postalCode.linearLayoutPostalCode.setVisibility(View.GONE);
            orderTypeBinding.buttonSubmit.setVisibility(View.GONE);

        });

        orderTypeBinding.seatSelection.radioButtonScanQRCode.setOnClickListener(v -> {
            dialogOrderType.dismiss();
            startQrCodeScannerActivity();
        });
        orderTypeBinding.seatSelection.radioButtonChooseTable.setOnClickListener(v -> {
            dialogOrderType.dismiss();
            initiateTableSelectionFragment("-1");
        });


        orderTypeBinding.buttonSubmit.setOnClickListener(v -> {

            if (orderType.get() == null || orderType.get().equals("")) {
                PopMessage.makeShortToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterOrderType());
            } else {
                if (orderType.get().equalsIgnoreCase("delivery")) {
                    String postalCode = orderTypeBinding.postalCode.editTextPostCode.getText().toString();
                    if (postalCode.equals("")) {
                        PopMessage.makeShortToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPostalCode());
                    } else {
                        //todo
                        postalCodeViewModel.verifyPostalCode(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, orderType.get(), postalCode, new NetworkOperations(true));

                        postalCodeViewModel.getVerifiyPostalcodeResponse().observe(this, verifiyPostalcodeResponse -> {
                            if (verifiyPostalcodeResponse != null) {
                                if (!verifiyPostalcodeResponse.getError().equals(0L)) {
                                    PopMessage.makeLongToast(getActivity(), verifiyPostalcodeResponse.getErrorMsg());
                                } else {
                                    cartViewModel.setOrderType(orderType.get());
                                    cartViewModel.setPostalCode(postalCode);
                                    dialogOrderType.dismiss();
                                    postalCodeViewModel.getVerifiyPostalcodeResponse().removeObservers(this);
                                    onPlusFood();
                                }
                            }
                        });

                    }
                } else if (orderType.get().equalsIgnoreCase("pickup")) {
                    cartViewModel.setOrderType(orderType.get());
                    onPlusFood();
                    dialogOrderType.dismiss();
                }
            }
        });

        dialogOrderType.show();

    }

    private void startQrCodeScannerActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(getActivity(), true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }

    public static String getTAG() {
        return TAG;
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
                    if(quantity==0){
                        cartDao.DeleteCartitem(food.getRestaurantPizzaItemName(), "");
                    }
                    else {
                        cart.quantity = quantity;
                        cartDao.Update(cart);
                    }
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
                cartItem.top_name = "";
                cartItem.top_price = "";
                cartItem.item_size_id = "0";
                cartItem.desc = food.getResPizzaDescription();
                cartItem.icon=food.getFoodIcon();
                cartDao.Insert(cartItem);
            }
            getCartItems();
        }


    }
    private void getCartItems(){
        totalAmount = 0.00;
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao=customerAppDatabase.cartDao();
        List<CartItem> cart_items=cartDao.getCartItems();
        if(cart_items.size()>0){
            binding.constraintLayoutCart.setVisibility(View.VISIBLE);
            for(CartItem cartItem:cart_items){
                if(cartItem.total_price!=null) {
                    totalAmount += Double.parseDouble(cartItem.total_price) * cartItem.quantity;
                }
                else{
                    totalAmount += Double.parseDouble(cartItem.item_price) * cartItem.quantity;
                }

            }
            binding.textViewTotalItem.setText(cart_items.size() + " items");

            binding.textViewTotalAmount.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(totalAmount));

        }
        else{
            binding.constraintLayoutCart.setVisibility(View.GONE);
        }
    }
    private ProgressDialog progressDialog;
    private void showProgress(){
        progressDialog= Utility.createProgressDialog(getContext());
    }
    private void hideProgress(){
        progressDialog.dismiss();
    }


}
