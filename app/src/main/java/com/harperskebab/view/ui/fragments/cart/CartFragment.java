package com.harperskebab.view.ui.fragments.cart;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogAuthBinding;
import com.harperskebab.databinding.DialogCouponBinding;
import com.harperskebab.databinding.DialogEatInTypeSelectionBinding;
import com.harperskebab.databinding.DialogPostalCodeBinding;
import com.harperskebab.databinding.DialogRedeemLoyaltyPointBinding;
import com.harperskebab.databinding.FragmentCartBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.model.OrderDetailItem;
import com.harperskebab.model.OrderFoodItem;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetServiceChargeResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.LocationTracker;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.SharedPrefrenceObj;
import com.harperskebab.utils.Utility;
import com.harperskebab.utils.Validation;
import com.harperskebab.view.adapter.CartAdapter;
import com.harperskebab.view.adapter.CartFoodAdapter;
import com.harperskebab.view.adapter.OrderItemAdapter;
import com.harperskebab.view.ui.activities.AddressActivity;
import com.harperskebab.view.ui.activities.ForgetPasswordActivity;
import com.harperskebab.view.ui.activities.HomeActivity;
import com.harperskebab.view.ui.activities.MapActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.view.ui.fragments.BranchFragment;
import com.harperskebab.view.ui.fragments.PaymentFragment;
import com.harperskebab.view.ui.fragments.TableSelectionFragment;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.CouponViewModel;
import com.harperskebab.viewmodel.EatInViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.LoyaltyPointViewModel;
import com.harperskebab.viewmodel.OrderDetailViewModel;
import com.harperskebab.viewmodel.PostalCodeViewModel;
import com.harperskebab.viewmodel.ReOrderViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.notbytes.barcode_reader.BarcodeReaderActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class CartFragment extends BaseFragment {
    private static final String TAG = "CartFragment";

    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1000;
    private static final int CURRENT_LOCATION_REQUEST = 1001;

    private FragmentCartBinding binding;

    private DialogPostalCodeBinding orderTypeBinding;
    private BottomSheetDialog dialogOrderType;

    private DialogEatInTypeSelectionBinding eatInTypeSelectionBinding;
    private Dialog dialogEatInType;
    private ReOrderViewModel reOrderViewModel;
    private CartViewModel cartViewModel;
    private FoodViewModel foodViewModel;
    private PostalCodeViewModel postalCodeViewModel;
    private EatInViewModel eatInViewModel;
    private CouponViewModel couponViewModel;
    private LoyaltyPointViewModel loyaltyPointViewModel;
    private UserViewModel userViewModel;
    boolean isFromReOrder = false;
    String orderNumber;
    private Double subTotalAmount, toPayPrice;
    private Double foodTax = 0.00D, drinkTax = 0.00D;
    String strTotalLoyaltyPoint;
    private CartAdapter foodAdapter;
    private int containerID;
    String branhId;
    double deliveryCharge=0.0;
    double serviceFees=0.0;
    double packageFees=0.0;
    double salesTax=0.0;
    double vatTax=0.0;
    double riderTipAmount=0.0;
    double totalAmount = 0.00;
    double food_tax_amount=0.0;

    public CartFragment() {
    }

    public static CartFragment newInstance(int containerID, boolean isFromReOrder, String orderNumber) {

        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putBoolean("isFromReOrder", isFromReOrder);
        args.putString("orderNumber", orderNumber);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            isFromReOrder = getArguments().getBoolean("isFromReOrder");
            orderNumber = getArguments().getString("orderNumber");
        }
        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        branhId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId", "");
        cartViewModel = ViewModelFactory.getInstance(getActivity()).create(CartViewModel.class);
        foodViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodViewModel.class);
        postalCodeViewModel = ViewModelFactory.getInstance(getActivity()).create(PostalCodeViewModel.class);
        eatInViewModel = ViewModelFactory.getInstance(getActivity()).create(EatInViewModel.class);
        couponViewModel = ViewModelFactory.getInstance(getActivity()).create(CouponViewModel.class);
        loyaltyPointViewModel = ViewModelFactory.getInstance(getActivity()).create(LoyaltyPointViewModel.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderName = preferences.getString("ORDERTYPE", "Delivery");
        AtomicReference<String> orderType = new AtomicReference<>("");
        orderType.set(orderName);
        cartViewModel.setOrderType(orderType.get());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        if (restaurantViewModel.getRestaurant().getValue().getHomeDeliveryAvailable().equalsIgnoreCase("Yes")) {
            binding.orderType.cardViewDelivery.setVisibility(View.VISIBLE);
        } else {
            binding.orderType.cardViewDelivery.setVisibility(View.GONE);
        }
        if (restaurantViewModel.getRestaurant().getValue().getPickupAvailable().equalsIgnoreCase("Yes")) {
            binding.orderType.cardViewPickUp.setVisibility(View.VISIBLE);
        } else {
            binding.orderType.cardViewPickUp.setVisibility(View.GONE);
        }
        if (restaurantViewModel.getRestaurant().getValue().getDineInAvailable().equalsIgnoreCase("Yes")) {
            binding.orderType.cardViewEatIn.setVisibility(View.VISIBLE);
        } else {
            binding.orderType.cardViewEatIn.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.orderType.textViewDelivery.setOnClickListener(this::onClick);
        binding.orderType.textViewPickUp.setOnClickListener(this::onClick);
        binding.orderType.textViewEatIn.setOnClickListener(this::onClick);

        binding.textViewPostalCode.setOnClickListener(this::onClick);
        binding.layTable.setOnClickListener(this::onClick);

        binding.cardViewApplyCoupon.setOnClickListener(this::onClick);
        binding.cardViewRedeemLoyaltyPoint.setOnClickListener(this::onClick);

        binding.textViewRiderTip0.setOnClickListener(this::onClick);
        binding.textViewRiderTip10.setOnClickListener(this::onClick);
        binding.textViewRiderTip20.setOnClickListener(this::onClick);
        binding.textViewRiderTip30.setOnClickListener(this::onClick);
        binding.textViewRiderTip40.setOnClickListener(this::onClick);
        binding.textViewRiderTip50.setOnClickListener(this::onClick);

        binding.buttonAddMore.setOnClickListener(this::onClick);
        binding.buttonOrderSendToKitchen.setOnClickListener(this::onClick);
        binding.buttonCheckout.setOnClickListener(this::onClick);

        restaurantViewModel.getRestaurant().observe(this, restaurant -> {

            if (restaurant != null) {

                if (!restaurant.getHomeDeliveryAvailable().equalsIgnoreCase("yes")) {
                    binding.orderType.cardViewDelivery.setVisibility(View.GONE);
                }

                if (!restaurant.getPickupAvailable().equalsIgnoreCase("yes")) {
                    binding.orderType.cardViewPickUp.setVisibility(View.GONE);
                }

                if (!restaurant.getDineInAvailable().equalsIgnoreCase("yes")) {
                    binding.orderType.cardViewEatIn.setVisibility(View.GONE);
                }

                if (!restaurant.getLoyalty_program_enable().equalsIgnoreCase("yes")) {
                    binding.cardViewRedeemLoyaltyPoint.setVisibility(View.GONE);
                }

                restaurantViewModel.getRestaurant().removeObservers(this);
            }

        });
         cartViewModel.getOrderType().observe(this, orderType -> {

            if (orderType.equalsIgnoreCase(getString(R.string.delivery))) {

                binding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                binding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                setTextViewDrawableColor(binding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorWhite));

                binding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
                binding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
                setTextViewDrawableColor(binding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

                binding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
                binding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
                setTextViewDrawableColor(binding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

                if (cartViewModel.getPostalCode().getValue() != null && !cartViewModel.getPostalCode().getValue().equals("")) {
                    binding.textViewPostalCode.setVisibility(View.VISIBLE);
                    binding.textViewPostalCode.setText(String.format(getString(R.string.postal_code) + ": %s", cartViewModel.getPostalCode().getValue()));
                }

                binding.layTable.setVisibility(View.GONE);

            } else if (orderType.equalsIgnoreCase(getString(R.string.pick_up))) {

                binding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                binding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                setTextViewDrawableColor(binding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorWhite));

                binding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
                binding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
                setTextViewDrawableColor(binding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

                binding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
                binding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
                setTextViewDrawableColor(binding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

                binding.textViewPostalCode.setVisibility(View.GONE);
                binding.layTable.setVisibility(View.GONE);

            } else if (orderType.equalsIgnoreCase(getString(R.string.eat_in))) {
                binding.orderType.textViewEatIn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                binding.orderType.textViewEatIn.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                setTextViewDrawableColor(binding.orderType.textViewEatIn, ContextCompat.getColor(getActivity(), R.color.colorWhite));

                binding.orderType.textViewDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
                binding.orderType.textViewDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
                setTextViewDrawableColor(binding.orderType.textViewDelivery, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));

                binding.orderType.textViewPickUp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
                binding.orderType.textViewPickUp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
                setTextViewDrawableColor(binding.orderType.textViewPickUp, ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
                if (cartViewModel.getSelectedTableID().getValue() == null) {
                    binding.layTable.setVisibility(View.GONE);
                } else {
                    binding.layTable.setVisibility(View.VISIBLE);
                    String tableName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("TableNumber", "");
                    binding.textViewTableIDAndName.setText(getString(R.string.table) + " " + tableName);
                    binding.txtTableNo.setText("No Of Person : " + cartViewModel.getNumberOfPeople().getValue());
                }
                binding.textViewPostalCode.setVisibility(View.GONE);

//                checkLocationPermission();

            }

            callRestaurantDiscountAPI();

        });


        binding.recyclerViewFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(binding.recyclerViewFood.getContext(), LinearLayoutManager.VERTICAL);
        binding.recyclerViewFood.addItemDecoration(dividerItemDecoration2);
//        List<Food> cartFoods = new ArrayList<>(foodViewModel.getSelectedFoods().getValue());
//        foodAdapter = new CartFoodAdapter(getActivity(), cartFoods, onPlusClick, onMinusClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));
//        binding.recyclerViewFood.setAdapter(foodAdapter);

        if (cartViewModel.getCouponCodeDiscount().getValue() == null || cartViewModel.getCouponCodeDiscount().getValue().equals("")) {
            cartViewModel.setCouponCodeDiscount("0");
        }

        if (cartViewModel.getLoyaltyCodeDiscount().getValue() == null || cartViewModel.getLoyaltyCodeDiscount().getValue().equals("")) {
            cartViewModel.setLoyaltyCodeDiscount("0");
        }

        if (cartViewModel.getRestaurantDiscount().getValue() == null || cartViewModel.getRestaurantDiscount().getValue().equals("")) {
            cartViewModel.setRestaurantDiscount("0");
        }

        if (cartViewModel.getRiderTip().getValue() == null || cartViewModel.getRiderTip().getValue().equals("")) {
            cartViewModel.setRiderTip("0");
        }
getCartItems();
//        cartViewModel.getFoods().observe(this, foods -> {
//
//            if (foods != null) {
//
//                binding.recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(getActivity()));
//                DividerItemDecoration dividerItemDecoration3 = new DividerItemDecoration(binding.recyclerViewOrderItems.getContext(), LinearLayoutManager.VERTICAL);
//                binding.recyclerViewOrderItems.addItemDecoration(dividerItemDecoration3);
//
//                CartFoodAdapter foodAdapter = new CartFoodAdapter(getActivity(), foods, onOrderedFoodPlusClick, onOrderedFoodMinusClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));
//                binding.recyclerViewOrderItems.setAdapter(foodAdapter);
//
//            }
//
//        });

        foodViewModel.getSelectedFoods().observe(this, foods -> {
            callRestaurantDiscountAPI();
        });

        cartViewModel.getCouponCodeDiscount().observe(this, couponCodeDiscount -> {
            callRestaurantDiscountAPI();
        });

        cartViewModel.getLoyaltyCodeDiscount().observe(this, loyalityDiscount -> {
            callRestaurantDiscountAPI();
        });

        cartViewModel.getRestaurantDiscount().observe(this, s -> {

            if (s != null) {
                Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
                List<Food> orderedFoods = new ArrayList<>();

                if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("eat-in")) {
                    if (cartViewModel.getFoods().getValue() != null) {
                        orderedFoods = cartViewModel.getFoods().getValue();
                    }
                }

//                List<Food> foods = new ArrayList<>();
//                foods.addAll(selectedFoods);
//                foods.addAll(orderedFoods);

                Double totalAmount = 0.00D;
                Double totalAmountOld = 0.00D;

                Double foodTax = 0.00D;
                Double drinkTax = 0.00D;
//                for (Food food : foods) {
//
//
//                    Double tmpTotalAmount = 0.0, tmpTotalAmountOld = 0.0;
//                    if (food.getFoodItem() == null) {
//                        tmpTotalAmount = (Double.parseDouble(food.getRestaurantPizzaItemPrice()) * food.getFoodCount());
//
//                        tmpTotalAmountOld = (Double.parseDouble(food.getRestaurantPizzaItemOldPrice().equals("") ? food.getRestaurantPizzaItemPrice() : food.getRestaurantPizzaItemOldPrice()) * food.getFoodCount());
//
//                    } else {
//                        tmpTotalAmount = (Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice()) * food.getFoodCount());
//
//                        tmpTotalAmountOld = (Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemOldPrice().equals("") ? food.getRestaurantPizzaItemPrice() : food.getFoodItem().getRestaurantPizzaItemOldPrice()) * food.getFoodCount());
//
//                    }
//
//                    if (food.getFoodItemExtraToppings() == null) {
//                        totalAmount += tmpTotalAmount;
//                        totalAmountOld += tmpTotalAmountOld;
//                    } else {
//                        Double totalFoodItemExtraToppingPrice = 0.0;
//
//                        for (int i = 0; i < food.getFoodItemExtraToppings().size(); i++) {
//                            totalFoodItemExtraToppingPrice += Double.parseDouble(food.getFoodItemExtraToppings().get(i).getFoodPriceAddons());
//                        }
////                        int freeToppingSelectionAllowed = food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed().equals("") ? 0 : Integer.parseInt(food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed());
////
////                        for (int i = freeToppingSelectionAllowed; i < food.getFoodItemExtraToppings().size(); i++) {
////                            totalFoodItemExtraToppingPrice += Double.parseDouble(food.getFoodItemExtraToppings().get(i).getFoodPriceAddons());
////                        }
//
//                        totalAmount += tmpTotalAmount + totalFoodItemExtraToppingPrice;
//                        totalAmountOld += tmpTotalAmountOld + totalFoodItemExtraToppingPrice;
//                    }
//
//                }
//
//                subTotalAmount = totalAmount - (Double.parseDouble(cartViewModel.getRestaurantDiscount().getValue()) + Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue()) + Double.parseDouble(cartViewModel.getLoyaltyCodeDiscount().getValue()));
//
//                Double totalDiscount = Double.parseDouble(cartViewModel.getRestaurantDiscount().getValue()) + Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue());
//
//                setValueInTextView(binding.billDetail.relativeLayout1, binding.billDetail.textViewItem1, languageViewModel.getLanguageResponse().getValue().getFoodItem(), binding.billDetail.textViewItem1Price, totalAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
//
//                setValueInTextView(binding.billDetail.relativeLayout2, binding.billDetail.textViewItem2, languageViewModel.getLanguageResponse().getValue().getFoodDiscount(), binding.billDetail.textViewItem2Price, totalAmountOld - totalAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
//
//                setValueInTextView(binding.billDetail.relativeLayout3, binding.billDetail.textViewItem3, languageViewModel.getLanguageResponse().getValue().getCouponDiscount(), binding.billDetail.textViewItem3Price, Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue()), restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
//
//                setValueInTextView(binding.billDetail.relativeLayout4, binding.billDetail.textViewItem4, languageViewModel.getLanguageResponse().getValue().getLoyaltyDiscount(), binding.billDetail.textViewItem4Price, Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue()), restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
//
//                setValueInTextView(binding.billDetail.relativeLayout5, binding.billDetail.textViewItem5, languageViewModel.getLanguageResponse().getValue().getRestaurantDiscount(), binding.billDetail.textViewItem5Price, Double.parseDouble(cartViewModel.getRestaurantDiscount().getValue()), restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
//
//                setValueInTextView(binding.billDetail.relativeLayout6, binding.billDetail.textViewItem6, languageViewModel.getLanguageResponse().getValue().getTotalDiscount(), binding.billDetail.textViewItem6Price, 0L, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
//
//                setValueInTextView(binding.billDetail.relativeLayout7, binding.billDetail.textViewItem7, languageViewModel.getLanguageResponse().getValue().getSubtotal(), binding.billDetail.textViewItem7Price, subTotalAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
                updateData();
            }

        });

        cartViewModel.getServiceCharge().observe(this, serviceCharge -> {

            Set<Food> foods = foodViewModel.getSelectedFoods().getValue();

            foodTax = 0.00D;
            drinkTax = 0.00D;
//
//            for (Food food : foods) {
//
//                Double tmpTotalAmount = 0.0;
//                if (food.getFoodItem() == null) {
//                    tmpTotalAmount = (Double.parseDouble(food.getRestaurantPizzaItemPrice()) * food.getFoodCount());
//
//                } else {
//                    tmpTotalAmount = (Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice()) * food.getFoodCount());
//
//                }
//
//                if (food.getFoodItemExtraToppings() != null) {
//                    Double totalFoodItemExtraToppingPrice = 0.0;
//
//                    int freeToppingSelectionAllowed = food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed().equals("") ? 0 : Integer.parseInt(food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed());
//
//                    for (int i = freeToppingSelectionAllowed; i < food.getFoodItemExtraToppings().size(); i++) {
//                        totalFoodItemExtraToppingPrice += Double.parseDouble(food.getFoodItemExtraToppings().get(i).getFoodPriceAddons());
//                    }
//
//                    tmpTotalAmount = tmpTotalAmount + totalFoodItemExtraToppingPrice;
//                }
//
//                if (food.getFoodTaxApplicable().equalsIgnoreCase("7")) {
//
//                    int foodTaxPercent = Integer.parseInt(food.getFoodTaxApplicable());
//
//                    foodTax += (tmpTotalAmount * foodTaxPercent) / 100;
//
//                } else if (!food.getFoodTaxApplicable().equalsIgnoreCase("")) {
//                    int drinkTaxPercent = Integer.parseInt(food.getFoodTaxApplicable());
//
//                    drinkTax += (tmpTotalAmount * drinkTaxPercent) / 100;
//                }
//
//            }

            if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("Delivery")) {
                deliveryCharge = Double.parseDouble(serviceCharge.getDeliveryChargeValue());
            } else {
                deliveryCharge = 0.0D;
            }

            setValueInTextView(binding.billDetail.relativeLayout8, binding.billDetail.textViewItem8, languageViewModel.getLanguageResponse().getValue().getDeliveryCharge(), binding.billDetail.textViewItem8Price, deliveryCharge, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

             serviceFees = Double.parseDouble(serviceCharge.getServiceFees());
            setValueInTextView(binding.billDetail.relativeLayout9, binding.billDetail.textViewItem9, languageViewModel.getLanguageResponse().getValue().getServiceCharge(), binding.billDetail.textViewItem9Price, serviceFees, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

             packageFees = Double.parseDouble(serviceCharge.getPackageFees());
            setValueInTextView(binding.billDetail.relativeLayout10, binding.billDetail.textViewItem10, languageViewModel.getLanguageResponse().getValue().getPackageFees(), binding.billDetail.textViewItem10Price, packageFees, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

             salesTax = Double.parseDouble(serviceCharge.getSalesTaxAmount());
            setValueInTextView(binding.billDetail.relativeLayout11, binding.billDetail.textViewItem11, languageViewModel.getLanguageResponse().getValue().getSaleTax(), binding.billDetail.textViewItem11Price, salesTax, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

             vatTax = Double.parseDouble(serviceCharge.getVatTax());
            setValueInTextView(binding.billDetail.relativeLayout12, binding.billDetail.textViewItem12, languageViewModel.getLanguageResponse().getValue().getVatTax(), binding.billDetail.textViewItem12Price, vatTax, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

            setValueInTextView(binding.billDetail.relativeLayout13, binding.billDetail.textViewItem13, languageViewModel.getLanguageResponse().getValue().getFoodTax(), binding.billDetail.textViewItem13Price, foodTax, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

            setValueInTextView(binding.billDetail.relativeLayout14, binding.billDetail.textViewItem14, languageViewModel.getLanguageResponse().getValue().getDrinkTax(), binding.billDetail.textViewItem14Price, drinkTax, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

             riderTipAmount = (subTotalAmount * Double.parseDouble(cartViewModel.getRiderTip().getValue())) / 100;
            if (restaurantViewModel.getRestaurant().getValue().getRider_Available().equalsIgnoreCase("No")) {
                binding.billDetail.relativeLayout15.setVisibility(View.GONE);
            } else {
                binding.billDetail.relativeLayout15.setVisibility(View.VISIBLE);
                setValueInTextView(binding.billDetail.relativeLayout15, binding.billDetail.textViewItem15, languageViewModel.getLanguageResponse().getValue().getRiderTip(), binding.billDetail.textViewItem15Price, riderTipAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
            }
            toPayPrice = subTotalAmount + deliveryCharge + packageFees + serviceFees + vatTax + riderTipAmount + foodTax + drinkTax;

            setValueInTextView(binding.billDetail.relativeLayoutToPay, binding.billDetail.textViewPay, languageViewModel.getLanguageResponse().getValue().getToPay(), binding.billDetail.textViewTotalPrice, toPayPrice, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

            binding.textViewFinalAmountToPay.setText("Total Pay\n" +
                    Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                    + " " +
                    Utility.DECIMAL_FORMAT.format(toPayPrice));

        });

        cartViewModel.getRiderTip().observe(this, riderTip -> {

            if (riderTip != null) {
                RmGetServiceChargeResponse serviceCharge = cartViewModel.getServiceCharge().getValue();

                if (serviceCharge != null) {

                    if (cartViewModel.getOrderType().getValue().equalsIgnoreCase(getString(R.string.delivery))) {
                        deliveryCharge = Double.parseDouble(serviceCharge.getDeliveryChargeValue());
                    } else {
                        deliveryCharge = 0.0D;
                    }

                    serviceFees = Double.parseDouble(serviceCharge.getServiceFees());

                     packageFees = Double.parseDouble(serviceCharge.getPackageFees());

                     salesTax = Double.parseDouble(serviceCharge.getSalesTaxAmount());

                     vatTax = Double.parseDouble(serviceCharge.getVatTax());

                    if (subTotalAmount != null) {
                        riderTipAmount = (subTotalAmount * Double.parseDouble(cartViewModel.getRiderTip().getValue())) / 100;
                    }
                    setValueInTextView(binding.billDetail.relativeLayout12, binding.billDetail.textViewItem12, languageViewModel.getLanguageResponse().getValue().getRiderTip(), binding.billDetail.textViewItem12Price, riderTipAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

                    toPayPrice = subTotalAmount + deliveryCharge + packageFees + serviceFees + vatTax + riderTipAmount + foodTax + drinkTax;


                    setValueInTextView(binding.billDetail.relativeLayoutToPay, binding.billDetail.textViewPay, languageViewModel.getLanguageResponse().getValue().getToPay(), binding.billDetail.textViewTotalPrice, toPayPrice, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

                    binding.textViewFinalAmountToPay.setText("Total Pay\n" +
                            Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                            + " " +
                            Utility.DECIMAL_FORMAT.format(toPayPrice));

                }

            }
        });


        if (userViewModel.getSignInResponse().getValue() != null) {
            loyaltyPointViewModel.getTotalLoyaltyPoint(getActivity(), userViewModel.getSignInResponse().getValue().getCustomerId(), new NetworkOperations(false));

            loyaltyPointViewModel.getTotalLoyaltyPoint().observe(this, totalLoyaltyPoint -> {
                strTotalLoyaltyPoint = totalLoyaltyPoint;
                if (!totalLoyaltyPoint.equalsIgnoreCase("0")) {
                    binding.linearLayoutTotalLoyaltyPoint.setVisibility(View.VISIBLE);
                    binding.textViewTotalLoyaltyPoint.setText(totalLoyaltyPoint);
                    binding.cardViewRedeemLoyaltyPoint.setVisibility(View.VISIBLE);
                } else {
                    binding.cardViewRedeemLoyaltyPoint.setVisibility(View.GONE);
                }

            });
        } else {
            binding.linearLayoutTotalLoyaltyPoint.setVisibility(View.GONE);
        }

    }

    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, CURRENT_LOCATION_REQUEST);

        } else {
            getLocationAndValidate();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getCart());
        binding.orderType.txtOrderType.setText(languageViewModel.getLanguageResponse().getValue().getOrderType());
        binding.orderType.textViewDelivery.setText(languageViewModel.getLanguageResponse().getValue().getDelivery());
        binding.orderType.textViewPickUp.setText(languageViewModel.getLanguageResponse().getValue().getPickup());
        binding.orderType.textViewEatIn.setText(languageViewModel.getLanguageResponse().getValue().getEATIN());
        binding.txtApplyCoupon.setText(languageViewModel.getLanguageResponse().getValue().getApplyCoupon());
        binding.textViewTotalLoyaltyPoint.setText(languageViewModel.getLanguageResponse().getValue().getRedeemLoyaltyPoints());
        binding.buttonCheckout.setText(languageViewModel.getLanguageResponse().getValue().getCheckout());
        if (userViewModel.getSignInResponse().getValue() != null && restaurantViewModel.getRestaurant().getValue().getLoyalty_program_enable().equalsIgnoreCase("Yes")) {
            binding.cardViewRedeemLoyaltyPoint.setVisibility(View.VISIBLE);
        } else {
            binding.cardViewRedeemLoyaltyPoint.setVisibility(View.GONE);
        }
    }

    private void getLocationAndValidate() {
        LocationTracker.LocationResult locationResult = new LocationTracker.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                if (!(location == null)) {
                    //todo
                } else {
                    //todo
                }

            }
        };

        LocationTracker locationTracker = new LocationTracker();
        locationTracker.getLocation(getActivity(), locationResult);
    }

    CartFoodAdapter.OnPlusClick onPlusClick = new CartFoodAdapter.OnPlusClick() {
        @Override
        public void onClick(int position, Food food) {

            food.setFoodCount(food.getFoodCount() + 1);

            Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
            selectedFoods.remove(food);
            selectedFoods.add(food);
            foodViewModel.setSelectedFoods(selectedFoods);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ITEM_COUNT", String.valueOf(foodViewModel.getSelectedFoods().getValue().size()));
            editor.apply();
            foodAdapter.notifyDataSetChanged();
        }
    };

    CartFoodAdapter.OnMinusClick onMinusClick = new CartFoodAdapter.OnMinusClick() {
        @Override
        public void onClick(int position, Food food) {

//            if (food.getFoodCount() > food.getFoodCountLimit()) {
//                food.setFoodCount(food.getFoodCount() - 1);
//
//                Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
//                if (food.getFoodCount() == 0) {
//                    selectedFoods.remove(food);
//                    foodAdapter.remove(food);
//                }
//                foodViewModel.setSelectedFoods(selectedFoods);
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("ITEM_COUNT", String.valueOf(foodViewModel.getSelectedFoods().getValue().size()));
//                editor.apply();
//                foodAdapter.notifyDataSetChanged();
//            } else {
//                //todo
//            }

        }
    };


    CartFoodAdapter.OnPlusClick onOrderedFoodPlusClick = (position, food) -> {
    };
    CartFoodAdapter.OnMinusClick onOrderedFoodMinusClick = (position, food) -> {
    };

    private void callRestaurantDiscountAPI() {
        try {
            Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
            List<Food> orderedFoods = new ArrayList<>();

            if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("eat-in")) {
                if (cartViewModel.getFoods().getValue() != null) {
                    orderedFoods = cartViewModel.getFoods().getValue();
                }
            }

            List<Food> foods = new ArrayList<>();
            foods.addAll(selectedFoods);
            foods.addAll(orderedFoods);

            Double totalAmount = 0.00D;

            boolean orderedFoodFlag = true;

            if (cartViewModel.getFoods().getValue() == null) {
                orderedFoodFlag = false;
            } else if (cartViewModel.getFoods().getValue().size() == 0) {
                orderedFoodFlag = false;
            }

            if (foods.size() != 0 || orderedFoodFlag) {
                boolean isNewFoodAdded = false;

                for (Food food : foods) {

                    Double tmpTotalAmount = 0.0;
                    if (food.getFoodItem() == null) {
                        tmpTotalAmount = (Double.parseDouble(food.getRestaurantPizzaItemPrice()) * food.getFoodCount());
                    } else {
                        tmpTotalAmount = (Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice()) * food.getFoodCount());
                    }

                    if (food.getFoodItemExtraToppings() == null) {
                        totalAmount += tmpTotalAmount;
                    } else {
                        Double totalFoodItemExtraToppingPrice = 0.0;

//                        int freeToppingSelectionAllowed = food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed().equals("") ? 0 : Integer.parseInt(food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed());

                        for (int i = 0; i < food.getFoodItemExtraToppings().size(); i++) {
                            totalFoodItemExtraToppingPrice += Double.parseDouble(food.getFoodItemExtraToppings().get(i).getFoodPriceAddons());
                        }
                        totalAmount += tmpTotalAmount + totalFoodItemExtraToppingPrice;
                    }

                    if (!food.isOrderSendToKitchen() || food.getFoodCount() > food.getFoodCountLimit()) {
                        isNewFoodAdded = true;
                    }
                }

                Double subTotalAmount = totalAmount - (Double.parseDouble(cartViewModel.getRestaurantDiscount().getValue()) + Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue()) + Double.parseDouble(cartViewModel.getLoyaltyCodeDiscount().getValue()));



                if (isNewFoodAdded && cartViewModel.getOrderType().getValue().equalsIgnoreCase(getString(R.string.eat_in))) {

                    binding.buttonAddMore.setVisibility(View.GONE);

                    binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                    binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);

                } else {

                    if (!cartViewModel.getOrderType().getValue().equalsIgnoreCase(getString(R.string.eat_in))) {
                        binding.buttonAddMore.setVisibility(View.GONE);
                    }

                    binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                    binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "callRestaurantDiscountAPI: Exception: ", e);
        }
    }

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

        if (v.getId() == binding.orderType.textViewDelivery.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                if (cartViewModel.getPostalCode().getValue() == null || cartViewModel.getPostalCode().getValue().equals("")) {
//                    showPostalCodeDialog();
                    cartViewModel.setOrderType("Delivery");

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ORDERTYPE", "Delivery");
                    editor.apply();
                    binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                    binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);
                } else {
                    cartViewModel.setOrderType("Delivery");
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ORDERTYPE", "Delivery");
                    editor.apply();
                    binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                    binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);
                }
            }

        } else if (v.getId() == binding.orderType.textViewPickUp.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                cartViewModel.setOrderType("PickUp");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ORDERTYPE", "PickUp");
                editor.apply();
                binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);
            }

        } else if (v.getId() == binding.orderType.textViewEatIn.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                if (cartViewModel.getSelectedTableID().getValue() == null || cartViewModel.getSelectedTableID().getValue().equals("")) {
                    showEatInTypeDialog();
                } else {
                    cartViewModel.setOrderType("EAT-IN");
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ORDERTYPE", "EAT-IN");
                    editor.apply();
                    binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                    binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);

                }
            }

        } else if (v.getId() == binding.textViewPostalCode.getId()) {
            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                showPostalCodeDialog();
            }
        } else if (v.getId() == binding.layTable.getId()) {
            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                showEatInTypeDialog();
            }
        } else if (v.getId() == binding.cardViewApplyCoupon.getId()) {
            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                showApplyCouponDialog();
            }
        } else if (v.getId() == binding.cardViewRedeemLoyaltyPoint.getId()) {
            if (userViewModel.getSignInResponse().getValue() != null) {
                if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                    showRedeemLoyaltyPointDialog();
                }
            } else {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseSignIn());
            }
        } else if (v.getId() == binding.textViewRiderTip0.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                cartViewModel.setRiderTip("0");

                changeRiderTipTextView(true, binding.textViewRiderTip0);
                changeRiderTipTextView(false, binding.textViewRiderTip10);
                changeRiderTipTextView(false, binding.textViewRiderTip20);
                changeRiderTipTextView(false, binding.textViewRiderTip30);
                changeRiderTipTextView(false, binding.textViewRiderTip40);
                changeRiderTipTextView(false, binding.textViewRiderTip50);
            }
        } else if (v.getId() == binding.textViewRiderTip10.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                cartViewModel.setRiderTip("10");

                changeRiderTipTextView(false, binding.textViewRiderTip0);
                changeRiderTipTextView(true, binding.textViewRiderTip10);
                changeRiderTipTextView(false, binding.textViewRiderTip20);
                changeRiderTipTextView(false, binding.textViewRiderTip30);
                changeRiderTipTextView(false, binding.textViewRiderTip40);
                changeRiderTipTextView(false, binding.textViewRiderTip50);
            }
        } else if (v.getId() == binding.textViewRiderTip20.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                cartViewModel.setRiderTip("20");

                changeRiderTipTextView(false, binding.textViewRiderTip0);
                changeRiderTipTextView(false, binding.textViewRiderTip10);
                changeRiderTipTextView(true, binding.textViewRiderTip20);
                changeRiderTipTextView(false, binding.textViewRiderTip30);
                changeRiderTipTextView(false, binding.textViewRiderTip40);
                changeRiderTipTextView(false, binding.textViewRiderTip50);
            }
        } else if (v.getId() == binding.textViewRiderTip30.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                cartViewModel.setRiderTip("30");

                changeRiderTipTextView(false, binding.textViewRiderTip0);
                changeRiderTipTextView(false, binding.textViewRiderTip10);
                changeRiderTipTextView(false, binding.textViewRiderTip20);
                changeRiderTipTextView(true, binding.textViewRiderTip30);
                changeRiderTipTextView(false, binding.textViewRiderTip40);
                changeRiderTipTextView(false, binding.textViewRiderTip50);
            }
        } else if (v.getId() == binding.textViewRiderTip40.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                cartViewModel.setRiderTip("40");

                changeRiderTipTextView(false, binding.textViewRiderTip0);
                changeRiderTipTextView(false, binding.textViewRiderTip10);
                changeRiderTipTextView(false, binding.textViewRiderTip20);
                changeRiderTipTextView(false, binding.textViewRiderTip30);
                changeRiderTipTextView(true, binding.textViewRiderTip40);
                changeRiderTipTextView(false, binding.textViewRiderTip50);
            }
        } else if (v.getId() == binding.textViewRiderTip50.getId()) {

            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                cartViewModel.setRiderTip("50");

                changeRiderTipTextView(false, binding.textViewRiderTip0);
                changeRiderTipTextView(false, binding.textViewRiderTip10);
                changeRiderTipTextView(false, binding.textViewRiderTip20);
                changeRiderTipTextView(false, binding.textViewRiderTip30);
                changeRiderTipTextView(false, binding.textViewRiderTip40);
                changeRiderTipTextView(true, binding.textViewRiderTip50);
            }

        } else if (v.getId() == binding.buttonAddMore.getId()) {
//            if (isFromReOrder){
//                Intent intent=new Intent(getActivity(),HomeActivity.class);
//                getActivity().startActivity(intent);
//            }else {
                goBack();
//            }
        } else if (v.getId() == binding.buttonOrderSendToKitchen.getId()) {

            cartViewModel.setSubTotal(Utility.DECIMAL_FORMAT.format(subTotalAmount));
            cartViewModel.setToPay(Utility.DECIMAL_FORMAT.format(toPayPrice));

            cartViewModel.setFoodTax(Utility.DECIMAL_FORMAT.format(foodTax));
            cartViewModel.setDrinkTax(Utility.DECIMAL_FORMAT.format(drinkTax));

            if (userViewModel.getSignInResponse().getValue() == null) {
                showAuthDialog("2");
            } else {
                orderSendToKitchen();
            }

        } else if (v.getId() == binding.buttonCheckout.getId()) {
            if (SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID) == null) {
                if (cartViewModel.getSelectedTableID().getValue() == null || cartViewModel.getSelectedTableID().getValue().equals("")) {
                    if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("EAT-IN")) {
                        showEatInTypeDialog();
                    } else if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("Delivery")) {
                        binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                        binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);
                        cartViewModel.setSubTotal(Utility.DECIMAL_FORMAT.format(subTotalAmount));
                        cartViewModel.setToPay(Utility.DECIMAL_FORMAT.format(toPayPrice));

                        cartViewModel.setFoodTax(Utility.DECIMAL_FORMAT.format(foodTax));
                        cartViewModel.setDrinkTax(Utility.DECIMAL_FORMAT.format(drinkTax));

                        if (userViewModel.getSignInResponse().getValue() == null) {
                            showAuthDialog("1");
                        } else {
                            initiateNextFragment();
                        }

                    } else {
                        binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                        binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);
                        cartViewModel.setSubTotal(Utility.DECIMAL_FORMAT.format(subTotalAmount));
                        cartViewModel.setToPay(Utility.DECIMAL_FORMAT.format(toPayPrice));

                        cartViewModel.setFoodTax(Utility.DECIMAL_FORMAT.format(foodTax));
                        cartViewModel.setDrinkTax(Utility.DECIMAL_FORMAT.format(drinkTax));

                        if (userViewModel.getSignInResponse().getValue() == null) {
                            showAuthDialog("1");
                        } else {
                            initiateNextFragment();
                        }
                    }
                } else {
//                    cartViewModel.setOrderType("EAT-IN");

                    binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                    binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);
                    cartViewModel.setSubTotal(Utility.DECIMAL_FORMAT.format(subTotalAmount));
                    cartViewModel.setToPay(Utility.DECIMAL_FORMAT.format(toPayPrice));

                    cartViewModel.setFoodTax(Utility.DECIMAL_FORMAT.format(foodTax));
                    cartViewModel.setDrinkTax(Utility.DECIMAL_FORMAT.format(drinkTax));

                    if (userViewModel.getSignInResponse().getValue() == null) {
                        showAuthDialog("1");
                    } else {
                        initiateNextFragment();
                    }


                }
            }

        }

    }

    private void changeRiderTipTextView(boolean isSelected, TextView textView) {

        if (isSelected) {
            textView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrayLite));
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrayDark));
        }
    }

    @Override
    public boolean onBackPressed(View v, int keyCode, KeyEvent event) {
//        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getCart());
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                if (isFromReOrder){
//                   Intent intent=new Intent(getActivity(),HomeActivity.class);
//                   getActivity().startActivity(intent);
//                }else {
                    goBack();
//                }
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
                eatInTypeSelectionBinding.seatSelection.radioButtonScanQRCode.setChecked(false);
            }
            dialogEatInType.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CURRENT_LOCATION_REQUEST) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                }, CURRENT_LOCATION_REQUEST);

            } else {
                getLocationAndValidate();
            }

        }

    }

    private void orderSendToKitchen() {
        Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();

        StringBuilder itemId = new StringBuilder();
        StringBuilder quantity = new StringBuilder();
        StringBuilder price = new StringBuilder();
        StringBuilder sizeItemId = new StringBuilder();
        StringBuilder extraItemId = new StringBuilder();
        for (Food food : selectedFoods) {

            if (!food.isOrderSendToKitchen()) {
                itemId.append(itemId.toString().equals("") ? "" + food.getItemID() : "," + food.getItemID());

                quantity.append(quantity.toString().equals("") ? "" + (food.getFoodCount() - food.getFoodCountLimit()) : "," + (food.getFoodCount() - food.getFoodCountLimit()));

                price.append(price.toString().equals("") ? food.getRestaurantPizzaItemPrice() : "," + food.getRestaurantPizzaItemPrice());

                if (food.getSizeavailable().equalsIgnoreCase("yes")) {
                    sizeItemId.append(
                            sizeItemId.toString().equals("") ? food.getItemID() + "_" + food.getFoodItem().getId() : "," + food.getItemID() + "_" + food.getFoodItem().getId()
                    );
                } else {
                    sizeItemId.append(sizeItemId.toString().equals("") ? "0" : ",0");
                }

                if (food.getExtraavailable().equalsIgnoreCase("yes")) {

                    String foodItemID = food.getSizeavailable().equalsIgnoreCase("yes") ? "" + food.getFoodItem().getId() : "0";

                    for (FoodItemExtraTopping foodItemExtraTopping : food.getFoodItemExtraToppings()) {
                        extraItemId.append(
                                extraItemId.toString().equals("") ? food.getItemID() + "_" + foodItemID + "_" + foodItemExtraTopping.getExtraID() : "," + food.getItemID() + "_" + foodItemID + "_" + foodItemExtraTopping.getExtraID()
                        );
                    }

                } else {
                    extraItemId.append(extraItemId.toString().equals("") ? "0" : ",0");
                }
            }
        }

        String customerId = userViewModel.getSignInResponse().getValue().getCustomerId();
        String orderID = SharedPrefrenceObj.getSharedValue(getActivity(), Constant.SharedPreference.EAT_IN_ORDER_ID);
        RmGetServiceChargeResponse serviceCharge = cartViewModel.getServiceCharge().getValue();

        String restaurantDiscount = cartViewModel.getRestaurantDiscount().getValue();

        String couponCode = cartViewModel.getCouponCode().getValue();

        String couponCodePrice = cartViewModel.getCouponCodeDiscount().getValue();

        String loyaltyPayDiscount = cartViewModel.getLoyaltyCodeDiscount().getValue();

        String selectedTableID = cartViewModel.getSelectedTableID().getValue();

        String numberOfPeople = cartViewModel.getNumberOfPeople().getValue();

        String foodTax = cartViewModel.getFoodTax().getValue();
        String drinkTax = cartViewModel.getDrinkTax().getValue();

        cartViewModel.orderSendToKitchen(getActivity(), branhId, "", "", "", itemId.toString(), quantity.toString(), price.toString(), sizeItemId.toString(),
                extraItemId.toString(), orderID, customerId, Utility.DECIMAL_FORMAT.format(toPayPrice), Utility.DECIMAL_FORMAT.format(subTotalAmount), serviceCharge.getDeliveryChargeValue(),
                couponCode, couponCodePrice, "", serviceCharge.getSalesTaxAmount(), "", "", restaurantDiscount,
                "", serviceCharge.getServiceFees(), "0", "", "", serviceCharge.getPackageFees(),
                serviceCharge.getVatTax(), "", "", "", loyaltyPayDiscount, selectedTableID, "", "", "",
                foodTax, drinkTax, "", "", numberOfPeople, new NetworkOperations(true));

        cartViewModel.getOrderSendToKitchenResponse().observe(this, orderSendToKitchenResponse -> {

            if (orderSendToKitchenResponse != null) {

                Set<Food> foods = foodViewModel.getSelectedFoods().getValue();

                for (Food food : foods) {
                    food.setOrderSendToKitchen(true);
                    food.setFoodCountLimit(food.getFoodCount());
                }

                cartViewModel.setFoods(foods);

                foodViewModel.setSelectedFoods(new HashSet<>());

                binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);

                cartViewModel.getOrderSendToKitchenResponse().removeObservers(this);
            }

        });
    }

    private void initiateNextFragment() {
        if (cartViewModel.getOrderType().getValue().equalsIgnoreCase(getString(R.string.eat_in)) && !branhId.equals("")) {
            initiatePaymentFragment();
        } else if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("Delivery") && !branhId.equals("")) {
            initiateBranchFragment();
        } else {
            initiatePaymentFragment();
        }
    }

    private void initiateTableSelectionFragment(String tableID) {
        TableSelectionFragment tableSelectionFragment = TableSelectionFragment.newInstance(containerID, tableID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, tableSelectionFragment);
        transaction.addToBackStack(TableSelectionFragment.getTAG());
        transaction.commit();
    }

    private void initiateEmptyCartFragment() {
        EmptyCartFragment emptyCartFragment = EmptyCartFragment.newInstance(containerID);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, emptyCartFragment);
        transaction.addToBackStack(EmptyCartFragment.getTAG());
        transaction.commit();
    }

    private void initiateBranchFragment() {
//        BranchFragment branchFragment = BranchFragment.newInstance(containerID, "", 0.0, 0.0, 0.0, 0.0);
//
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.add(containerID, branchFragment);
//        transaction.addToBackStack(branchFragment.getTag());
//        transaction.commit();
        startActivity(
                new Intent(getActivity(), AddressActivity.class)
        );
    }

    private void initiatePaymentFragment() {
        PaymentFragment paymentFragment = PaymentFragment.newInstance(containerID);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(containerID, paymentFragment);
        transaction.addToBackStack(PaymentFragment.getTAG());
        transaction.commit();
    }

    private void showPostalCodeDialog() {

        orderTypeBinding = DialogPostalCodeBinding.inflate(getLayoutInflater());
        dialogOrderType = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogOrderType.setContentView(orderTypeBinding.getRoot());

        orderTypeBinding.postalCode.linearLayoutPostalCode.setVisibility(View.VISIBLE);
        orderTypeBinding.postalCode.editTextPostCode.setText(cartViewModel.getPostalCode().getValue());

        orderTypeBinding.buttonSubmit.setOnClickListener(v -> {

            String postalCode = orderTypeBinding.postalCode.editTextPostCode.getText().toString();
            if (postalCode.equals("")) {
                PopMessage.makeShortToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPostalCode());
            } else {
                postalCodeViewModel.verifyPostalCode(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, cartViewModel.getOrderType().getValue(), postalCode, new NetworkOperations(true));

                postalCodeViewModel.getVerifiyPostalcodeResponse().observe(this, verifiyPostalcodeResponse -> {
                    if (verifiyPostalcodeResponse != null) {
                        if (!verifiyPostalcodeResponse.getError().equals(0L)) {
                            PopMessage.makeLongToast(getActivity(), verifiyPostalcodeResponse.getErrorMsg());
                        } else {
                            cartViewModel.setOrderType("Delivery");

                            binding.buttonOrderSendToKitchen.setVisibility(View.GONE);
                            binding.linearLayoutCheckOut.setVisibility(View.VISIBLE);

                            cartViewModel.setPostalCode(postalCode);
                            binding.textViewPostalCode.setVisibility(View.VISIBLE);
                            binding.textViewPostalCode.setText(languageViewModel.getLanguageResponse().getValue().getPostalCode() + ": " + cartViewModel.getPostalCode().getValue());
                            dialogOrderType.dismiss();
                        }
                    }
                });

            }

        });

        dialogOrderType.show();
    }

    private void showEatInTypeDialog() {

        eatInTypeSelectionBinding = DialogEatInTypeSelectionBinding.inflate(getLayoutInflater());
        dialogEatInType = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogEatInType.setContentView(eatInTypeSelectionBinding.getRoot());
        eatInTypeSelectionBinding.seatSelection.linearLayoutSeatSelection.setVisibility(View.VISIBLE);
        eatInTypeSelectionBinding.seatSelection.radioButtonScanQRCode.setText(languageViewModel.getLanguageResponse().getValue().getScanQRCode());
        eatInTypeSelectionBinding.seatSelection.radioButtonChooseTable.setText(languageViewModel.getLanguageResponse().getValue().getChooseTable());
        eatInTypeSelectionBinding.seatSelection.imgClose.setOnClickListener(v -> {
            dialogEatInType.dismiss();
        });
        eatInTypeSelectionBinding.seatSelection.radioButtonScanQRCode.setOnClickListener(v -> {
            dialogEatInType.dismiss();
            startQrCodeScannerActivity();
        });
        eatInTypeSelectionBinding.seatSelection.radioButtonChooseTable.setOnClickListener(v -> {
            dialogEatInType.dismiss();
            initiateTableSelectionFragment("-1");
        });

        dialogEatInType.show();
    }

    private void startQrCodeScannerActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(getActivity(), true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }

    private void showApplyCouponDialog() {
        DialogCouponBinding dialogCouponBinding = DialogCouponBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogCoupon = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogCoupon.setContentView(dialogCouponBinding.getRoot());

        dialogCouponBinding.buttonSubmit.setOnClickListener(v -> {
            String couponCode = dialogCouponBinding.editTextCouponCode.getText().toString();
            if (couponCode.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterCouponCode());
            } else {
                couponViewModel.verifyCouponCode(getActivity(), branhId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, subTotalAmount.toString(), couponCode, new NetworkOperations(true));
                couponViewModel.getCouponCodeResponse().observe(this, rmVerifyCouponCodeResponse -> {

                    if (rmVerifyCouponCodeResponse != null) {
                        if (rmVerifyCouponCodeResponse.getError() == 0) {

                            cartViewModel.setCouponCode(rmVerifyCouponCodeResponse.getCouponCode());
                            cartViewModel.setCouponCodeDiscount(rmVerifyCouponCodeResponse.getCouponCodePrice());

                            couponViewModel.getCouponCodeResponse().removeObservers(this);
                            binding.cardViewApplyCoupon.setVisibility(View.GONE);
                            dialogCoupon.dismiss();

                            showMessage("Alert", rmVerifyCouponCodeResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);

                        } else {

                            showMessage("Alert", rmVerifyCouponCodeResponse.getError_msg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);

                        }
                    }

                });
            }
        });

        dialogCoupon.show();
    }

    private void showRedeemLoyaltyPointDialog() {
        DialogRedeemLoyaltyPointBinding dialogRedeemLoyaltyPointBinding = DialogRedeemLoyaltyPointBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogRedeemLoyaltyPoint = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogRedeemLoyaltyPoint.setContentView(dialogRedeemLoyaltyPointBinding.getRoot());

        dialogRedeemLoyaltyPointBinding.buttonSubmit.setOnClickListener(v -> {
            String loyaltyPoint = dialogRedeemLoyaltyPointBinding.editTextLoyaltyPoint.getText().toString();
            if (loyaltyPoint.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterLoyaltyPoint());
            } else {
                loyaltyPointViewModel.verifyCouponCode(getActivity(), userViewModel.getSignInResponse().getValue().getCustomerId(), loyaltyPoint, subTotalAmount.toString(), new NetworkOperations(true));
                loyaltyPointViewModel.getLoyaltyPointResponse().observe(this, verifyLoyaltyPointResponse -> {

                    if (verifyLoyaltyPointResponse != null) {
                        if (verifyLoyaltyPointResponse.getSuccess() == 0) {
                            cartViewModel.setLoyaltyCodeDiscount(verifyLoyaltyPointResponse.getTotalLoyaltyAmount());

                            loyaltyPointViewModel.getLoyaltyPointResponse().removeObservers(this);
                            dialogRedeemLoyaltyPoint.dismiss();

                            showMessage("Alert", verifyLoyaltyPointResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);

                        } else {


                            showMessage("Alert", verifyLoyaltyPointResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);

                        }
                    }

                });
            }
        });


        dialogRedeemLoyaltyPoint.show();
    }

    private void signIn(String stage, String emailID, String password, Dialog dialog) {
        userViewModel.signIn(getActivity(), emailID, password, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "", "", new NetworkOperations(true));
        userViewModel.getSignInResponse().observe(this, signInResponse -> {

            if (signInResponse != null) {
                if (signInResponse.getSuccess() == 0) {

                    showMessage("Alert", signInResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
                        if (stage.equals("1")) {
                            initiateNextFragment();
                        } else if (stage.equals("2")) {
                            orderSendToKitchen();
                        }
                        dialogInterface.dismiss();
                    }, dialogInterface -> {
                        dialogInterface.dismiss();
                    });

                    dialog.dismiss();

                } else {
                    userViewModel.setSignInResponse(null);

                    showMessage("Alert", signInResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                        dialogInterface.dismiss();
                    }, null);

                }
                userViewModel.getSignInResponse().removeObservers(this);
            }

        });
    }

    private void showAuthDialog(String stage) {
        DialogAuthBinding dialogAuthBinding = DialogAuthBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogAuth = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogAuth.setContentView(dialogAuthBinding.getRoot());

        dialogAuthBinding.tabLayoutAuth.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        dialogAuthBinding.linearLayoutSignIn.setVisibility(View.VISIBLE);
                        dialogAuthBinding.linearLayoutSignUp.setVisibility(View.GONE);

                        break;
                    case 1:
                        dialogAuthBinding.linearLayoutSignIn.setVisibility(View.GONE);
                        dialogAuthBinding.linearLayoutSignUp.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dialogAuthBinding.tabLayoutAuth.selectTab(dialogAuthBinding.tabLayoutAuth.getTabAt(0));

        dialogAuthBinding.buttonForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
        });

        String regex = "\\d+";

        dialogAuthBinding.buttonSignIn.setOnClickListener(v -> {

            String emailID = dialogAuthBinding.editTextSignInEmail.getText().toString();
            String password = dialogAuthBinding.editTextSignInPassword.getText().toString();

            if (emailID.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (Validation.isValidEmail(emailID)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (password.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPassword());
            } else {
                signIn(stage, emailID, password, dialogAuth);
            }

        });


        dialogAuthBinding.buttonSignUp.setOnClickListener(v -> {

            String fullName = dialogAuthBinding.editTextSignUptFullName.getText().toString();
            String emailID = dialogAuthBinding.editTextSignUpEmail.getText().toString();
            String phone = dialogAuthBinding.editTextSignUpPhone.getText().toString();
            String password = dialogAuthBinding.editTextSignUpPassword.getText().toString();

            int phoneLength = phone.length();

            if (fullName.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterFullName());
            } else if (!fullName.contains(" ")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterFullName());
            } else if (emailID.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (Validation.isValidEmail(emailID)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (phone.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (!phone.matches(regex)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (phoneLength < 10) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (password.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPassword());
            } else {
                String firstName, lastname;
                if (fullName.contains(" ")) {
                    firstName = fullName.split(" ")[0];
                    lastname = fullName.substring(fullName.indexOf(' ') + 1);
                } else {
                    firstName = fullName;
                    lastname = "";
                }

                userViewModel.signUp(getActivity(), firstName, lastname, emailID, password, phone, "", "", Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                        "", "", new NetworkOperations(true));

                userViewModel.getSignUpResponse().observe(this, signUpResponse -> {

                    if (signUpResponse != null) {
                        if (signUpResponse.getError().equals(1L)) {
                            signIn(stage, emailID, password, dialogAuth);
                        } else {
                            userViewModel.getSignUpResponse().setValue(null);
                            showMessage("Alert", signUpResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);
                        }
                        userViewModel.getSignUpResponse().removeObservers(this);
                    }
                });
            }
        });

        dialogAuth.show();
    }

    public static String getTAG() {
        return TAG;
    }
    List<CartItem> cartItems=new ArrayList<>();
    private void getCartItems(){
        totalAmount=0.0;
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao=customerAppDatabase.cartDao();
        List<CartItem> cart_items=cartDao.getCartItems();
        if(cart_items.size()>0){
            cartItems=cart_items;
            binding.recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(getActivity()));
            DividerItemDecoration dividerItemDecoration3 = new DividerItemDecoration(binding.recyclerViewOrderItems.getContext(), LinearLayoutManager.VERTICAL);
            binding.recyclerViewOrderItems.addItemDecoration(dividerItemDecoration3);

            foodAdapter = new CartAdapter( cartItems, foodClicked,Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));
            binding.recyclerViewOrderItems.setAdapter(foodAdapter);

            for(CartItem cartItem:cart_items){
                double vat_tax=0.0;
                if(cartItem.total_price!=null) {
                    if(cartItem.food_tax_applicable!=null){
                        if(!cartItem.food_tax_applicable.equalsIgnoreCase("")){
                            vat_tax=Double.parseDouble(cartItem.food_tax_applicable);
                          food_tax_amount+=(Double.parseDouble(cartItem.total_price)*vat_tax)/100;

                        }
                    }
                    totalAmount += Double.parseDouble(cartItem.total_price) * cartItem.quantity;

                }
                else{
                    if(cartItem.food_tax_applicable!=null){
                        if(!cartItem.food_tax_applicable.equalsIgnoreCase("")){
                            vat_tax=Double.parseDouble(cartItem.food_tax_applicable);
                            food_tax_amount+=(Double.parseDouble(cartItem.item_price)*vat_tax)/100;

                        }
                    }
                    totalAmount += Double.parseDouble(cartItem.item_price) * cartItem.quantity;
                }

            }
            cartViewModel.getRestaurentDiscountPrice(getActivity(), branhId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, Utility.DECIMAL_FORMAT.format(totalAmount), cartViewModel.getOrderType().getValue(), new NetworkOperations(true));

            cartViewModel.getServiceCharge(getActivity(), branhId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, Utility.DECIMAL_FORMAT.format(totalAmount), new NetworkOperations(true));
            updateData();

        }
        else{
            initiateEmptyCartFragment();
        }
    }
    private void updateData(){
        subTotalAmount = totalAmount - (Double.parseDouble(cartViewModel.getRestaurantDiscount().getValue()) + Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue()) + Double.parseDouble(cartViewModel.getLoyaltyCodeDiscount().getValue()));
        toPayPrice = subTotalAmount + deliveryCharge + packageFees + serviceFees + vatTax + riderTipAmount + foodTax + drinkTax;
        setValueInTextView(binding.billDetail.relativeLayoutToPay, binding.billDetail.textViewPay, languageViewModel.getLanguageResponse().getValue().getToPay(), binding.billDetail.textViewTotalPrice, toPayPrice, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

        if(food_tax_amount>0.0){

            setValueInTextView(binding.billDetail.relativeLayout2, binding.billDetail.textViewItem2, languageViewModel.getLanguageResponse().getValue().getVatTax(), binding.billDetail.textViewItem2Price, food_tax_amount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

        }
        else{
            binding.billDetail.textViewItem2.setVisibility(View.GONE);
            binding.billDetail.textViewItem2Price.setVisibility(View.GONE);
        }
        binding.textViewFinalAmountToPay.setText("Total Pay\n" +
                Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                + " " +
                Utility.DECIMAL_FORMAT.format(toPayPrice));
        Double totalDiscount = Double.parseDouble(cartViewModel.getRestaurantDiscount().getValue()) + Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue());

        setValueInTextView(binding.billDetail.relativeLayout1, binding.billDetail.textViewItem1, languageViewModel.getLanguageResponse().getValue().getFoodItem(), binding.billDetail.textViewItem1Price, totalAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

//        setValueInTextView(binding.billDetail.relativeLayout2, binding.billDetail.textViewItem2, languageViewModel.getLanguageResponse().getValue().getFoodDiscount(), binding.billDetail.textViewItem2Price, totalAmountOld - totalAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

        setValueInTextView(binding.billDetail.relativeLayout3, binding.billDetail.textViewItem3, languageViewModel.getLanguageResponse().getValue().getCouponDiscount(), binding.billDetail.textViewItem3Price, Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue()), restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

        setValueInTextView(binding.billDetail.relativeLayout4, binding.billDetail.textViewItem4, languageViewModel.getLanguageResponse().getValue().getLoyaltyDiscount(), binding.billDetail.textViewItem4Price, Double.parseDouble(cartViewModel.getCouponCodeDiscount().getValue()), restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

        setValueInTextView(binding.billDetail.relativeLayout5, binding.billDetail.textViewItem5, languageViewModel.getLanguageResponse().getValue().getRestaurantDiscount(), binding.billDetail.textViewItem5Price, Double.parseDouble(cartViewModel.getRestaurantDiscount().getValue()), restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

        setValueInTextView(binding.billDetail.relativeLayout6, binding.billDetail.textViewItem6, languageViewModel.getLanguageResponse().getValue().getTotalDiscount(), binding.billDetail.textViewItem6Price, 0L, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());

        setValueInTextView(binding.billDetail.relativeLayout7, binding.billDetail.textViewItem7, languageViewModel.getLanguageResponse().getValue().getSubtotal(), binding.billDetail.textViewItem7Price, subTotalAmount, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole());
    }
    CartAdapter.CartClicked foodClicked=new CartAdapter.CartClicked() {
        @Override
        public void Clicked(int position, CartItem food, Integer type) {
            CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
            CartDao cartDao=customerAppDatabase.cartDao();
            switch (type){
                case 0:
                {
                    Integer quantity=food.quantity;
                    quantity-=1;
                    if(quantity==0){
                        cartDao.DeleteCartitem(food.item_name,food.top_ids);
                    }
                    else {
                        food.quantity = quantity;
                        cartDao.Update(food);
                    }
                    break;
                }
                case 1:
                {
                    Integer quantity=food.quantity;
                    quantity+=1;
                    food.quantity=quantity;
                    cartDao.Update(food);
                    break;
                }
            }
            getCartItems();
//            foodAdapter.notifyDataSetChanged();

        }
    };

}
