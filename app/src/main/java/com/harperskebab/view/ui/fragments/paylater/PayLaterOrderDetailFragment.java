package com.harperskebab.view.ui.fragments.paylater;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.R;
import com.harperskebab.databinding.FragmentPayLaterOrderDetailBinding;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.model.OrderDetailItem;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetServiceChargeResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.SharedPrefrenceObj;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodAdapter;
import com.harperskebab.view.adapter.OrderItemAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.OrderViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.harperskebab.viewmodel.paylater.PayLaterCartViewModel;
import com.harperskebab.viewmodel.paylater.PayLaterFoodViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PayLaterOrderDetailFragment extends BaseFragment {
    private static final String TAG = "PayLaterOrderDetailFrag";

    private FragmentPayLaterOrderDetailBinding binding;
    private PayLaterFoodViewModel payLaterFoodViewModel;
    private PayLaterCartViewModel payLaterCartViewModel;
    private OrderViewModel orderViewModel;
    private UserViewModel userViewModel;
    private CartViewModel cartViewModel;
    private int containerID;
    private String orderID;
    List<Food> selectedFoods;
    private Double subTotalAmount, toPayPrice;
    private Double foodTax = 0.00D, drinkTax = 0.00D;

    private FoodAdapter foodAdapter;
    String branchId;

    public PayLaterOrderDetailFragment() {
    }

    public static PayLaterOrderDetailFragment newInstance(int containerID, String orderID) {

        PayLaterOrderDetailFragment fragment = new PayLaterOrderDetailFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putString(Constant.Data.ORDER_ID, orderID);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            orderID = getArguments().getString(Constant.Data.ORDER_ID);
        }
        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        branchId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId", "");
        payLaterFoodViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterFoodViewModel.class);
        payLaterCartViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterCartViewModel.class);
        orderViewModel = ViewModelFactory.getInstance(getActivity()).create(OrderViewModel.class);
        orderViewModel.getOrderDetail(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, orderID, new NetworkOperations(true));
        cartViewModel = ViewModelFactory.getInstance(getActivity()).create(CartViewModel.class);
        payLaterCartViewModel.getServiceCharge().setValue(null);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPayLaterOrderDetailBinding.inflate(inflater, container, false);

        binding.recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(getActivity()));

        orderViewModel.getOrderDetailResponse().observe(this, orderDetailResponse -> {

            if (orderDetailResponse != null) {
                OrderDetailItem orderDetailItem = orderDetailResponse.getOrderDetailItem().get(0);
                callRestaurantDiscountAPI(orderDetailItem);

                orderViewModel.getOrderDetailResponse().removeObservers(this);
            }
        });

        binding.recyclerViewFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerViewFood.getContext(), LinearLayoutManager.VERTICAL);
        binding.recyclerViewFood.addItemDecoration(dividerItemDecoration);

        payLaterFoodViewModel.getSelectedFoods().observe(this, foods -> {

            if (foods != null) {

                 selectedFoods = new ArrayList<>(foods);
                if (selectedFoods.size() != 0) {
                    binding.buttonSendToKitchen.setVisibility(View.GONE);
                    binding.buttonConfirmAndPay.setVisibility(View.VISIBLE);
                    foodAdapter = new FoodAdapter(getActivity(), selectedFoods, onPlusClick, onMinusClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));
                    binding.recyclerViewFood.setAdapter(foodAdapter);
                } else {
                    binding.buttonSendToKitchen.setVisibility(View.GONE);
                    binding.buttonConfirmAndPay.setVisibility(View.VISIBLE);
                }


                orderViewModel.getOrderDetailResponse().observe(this, orderDetailResponse -> {

                    if (orderDetailResponse != null) {
                        OrderDetailItem orderDetailItem = orderDetailResponse.getOrderDetailItem().get(0);
                        callRestaurantDiscountAPI(orderDetailItem);

                        orderViewModel.getOrderDetailResponse().removeObservers(this);
                    }
                });


//                callRestaurantDiscountAPI(orderViewModel.getOrderDetailResponse().getValue().getOrderDetailItem().get(0));
            }
        });

        payLaterCartViewModel.getServiceCharge().observe(this, serviceChargeResponse -> {

            if (serviceChargeResponse != null) {

                OrderDetailItem orderDetailItem = orderViewModel.getOrderDetailResponse().getValue().getOrderDetailItem().get(0);

                binding.textViewOrderNumber.setText(orderDetailItem.getOrderNumber());
                binding.txtTableNo.setText(languageViewModel.getLanguageResponse().getValue().getTableNo() + " : " + orderDetailItem.getTableBookingNumber());
                Set<Food> foods = payLaterFoodViewModel.getSelectedFoods().getValue();

                Double totalAmount = 0.00D;
                Double totalAmountOld = 0.00D;

                foodTax = 0.00D;
                drinkTax = 0.00D;

                if (foods != null) {
                    for (Food food : foods) {

                        Double tmpTotalAmount = 0.0, tmpTotalAmountOld = 0.0;
                        if (food.getFoodItem() == null) {
                            tmpTotalAmount = (Double.parseDouble(food.getRestaurantPizzaItemPrice()) * food.getFoodCount());

                            tmpTotalAmountOld = (Double.parseDouble(food.getRestaurantPizzaItemOldPrice().equals("") ? food.getRestaurantPizzaItemPrice() : food.getRestaurantPizzaItemOldPrice()) * food.getFoodCount());

                        } else {
                            tmpTotalAmount = (Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice()) * food.getFoodCount());

                            tmpTotalAmountOld = (Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemOldPrice().equals("") ? food.getRestaurantPizzaItemPrice() : food.getFoodItem().getRestaurantPizzaItemOldPrice()) * food.getFoodCount());

                        }

                        if (food.getFoodItemExtraToppings() == null) {
                            totalAmount += tmpTotalAmount;
                            totalAmountOld += tmpTotalAmountOld;
                        } else {
                            Double totalFoodItemExtraToppingPrice = 0.0;

                            int freeToppingSelectionAllowed = food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed().equals("") ? 0 : Integer.parseInt(food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed());

                            for (int i = freeToppingSelectionAllowed; i < food.getFoodItemExtraToppings().size(); i++) {
                                totalFoodItemExtraToppingPrice += Double.parseDouble(food.getFoodItemExtraToppings().get(i).getFoodPriceAddons());
                            }

                            totalAmount += tmpTotalAmount + totalFoodItemExtraToppingPrice;
                            totalAmountOld += tmpTotalAmountOld + totalFoodItemExtraToppingPrice;
                        }

                        if (food.getFoodTaxApplicable().equalsIgnoreCase("7")) {

                            int foodTaxPercent = Integer.parseInt(food.getFoodTaxApplicable());

                            foodTax += (tmpTotalAmount * foodTaxPercent) / 100;

                        } else if (!food.getFoodTaxApplicable().equalsIgnoreCase("")) {
                            int drinkTaxPercent = Integer.parseInt(food.getFoodTaxApplicable());

                            drinkTax += (tmpTotalAmount * drinkTaxPercent) / 100;
                        }

                    }
                }

                subTotalAmount = totalAmount + Double.parseDouble(orderDetailItem.getSubTotal());

                setValueInTextView(binding.billDetail.relativeLayout1, binding.billDetail.textViewItem1, getString(R.string.New_Food_Item), binding.billDetail.textViewItem1Price, totalAmount, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout2, binding.billDetail.textViewItem2, getString(R.string.New_Food_Discount), binding.billDetail.textViewItem2Price, totalAmountOld - totalAmount, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout3, binding.billDetail.textViewItem3, languageViewModel.getLanguageResponse().getValue().getCouponDiscount(), binding.billDetail.textViewItem3Price, 0L, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout4, binding.billDetail.textViewItem4, languageViewModel.getLanguageResponse().getValue().getLoyaltyDiscount(), binding.billDetail.textViewItem4Price, 0L, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout5, binding.billDetail.textViewItem5, languageViewModel.getLanguageResponse().getValue().getRestaurantDiscount(), binding.billDetail.textViewItem5Price, 0L, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout6, binding.billDetail.textViewItem6, languageViewModel.getLanguageResponse().getValue().getTotalDiscount(), binding.billDetail.textViewItem6Price, 0L, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout7, binding.billDetail.textViewItem7, languageViewModel.getLanguageResponse().getValue().getSubtotal(), binding.billDetail.textViewItem7Price, subTotalAmount, orderDetailItem.getCurrency());

                double deliveryCharge;
                if (orderDetailItem.getOrderType().equalsIgnoreCase("Delivery")) {
                    deliveryCharge = Double.parseDouble(orderDetailItem.getDeliveryCharge());
                } else {
                    deliveryCharge = 0.0D;
                }

                setValueInTextView(binding.billDetail.relativeLayout8, binding.billDetail.textViewItem8, languageViewModel.getLanguageResponse().getValue().getDeliveryCharge(), binding.billDetail.textViewItem8Price, deliveryCharge, orderDetailItem.getCurrency());

                double serviceFees = Double.parseDouble(serviceChargeResponse.getServiceFees());
                setValueInTextView(binding.billDetail.relativeLayout9, binding.billDetail.textViewItem9, languageViewModel.getLanguageResponse().getValue().getServiceCharge(), binding.billDetail.textViewItem9Price, serviceFees, orderDetailItem.getCurrency());

                double packageFees = Double.parseDouble(serviceChargeResponse.getPackageFees());
                setValueInTextView(binding.billDetail.relativeLayout10, binding.billDetail.textViewItem10, languageViewModel.getLanguageResponse().getValue().getPackageFees(), binding.billDetail.textViewItem10Price, packageFees, orderDetailItem.getCurrency());

                double salesTax = Double.parseDouble(serviceChargeResponse.getSalesTaxAmount());
                setValueInTextView(binding.billDetail.relativeLayout11, binding.billDetail.textViewItem11, languageViewModel.getLanguageResponse().getValue().getSaleTax(), binding.billDetail.textViewItem11Price, salesTax, orderDetailItem.getCurrency());

                double vatTax = Double.parseDouble(serviceChargeResponse.getVatTax());
                setValueInTextView(binding.billDetail.relativeLayout12, binding.billDetail.textViewItem12, languageViewModel.getLanguageResponse().getValue().getVatTax(), binding.billDetail.textViewItem12Price, vatTax, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout13, binding.billDetail.textViewItem13, getString(R.string.New_Food_Tax), binding.billDetail.textViewItem13Price, foodTax, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout14, binding.billDetail.textViewItem14, getString(R.string.New_Drink_Tax), binding.billDetail.textViewItem14Price, drinkTax, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout15, binding.billDetail.textViewItem15, languageViewModel.getLanguageResponse().getValue().getRiderTip(), binding.billDetail.textViewItem15Price, 0L, orderDetailItem.getCurrency());

                toPayPrice = subTotalAmount + deliveryCharge + packageFees + serviceFees + vatTax + foodTax + drinkTax;

                setValueInTextView(binding.billDetail.relativeLayoutToPay, binding.billDetail.textViewPay, languageViewModel.getLanguageResponse().getValue().getToPay(), binding.billDetail.textViewTotalPrice, toPayPrice, orderDetailItem.getCurrency());
//if (orderViewModel.getOrderDetailResponse().getValue().getOrderFoodItem()!=null) {
                OrderItemAdapter orderItemAdapter = new OrderItemAdapter(getActivity(), orderViewModel.getOrderDetailResponse().getValue().getOrderDetailItem().get(0).getOrderFoodItem());
                binding.recyclerViewOrderItems.setAdapter(orderItemAdapter);
//}

                binding.buttonAddMore.setVisibility(View.VISIBLE);
//                binding.buttonConfirmAndPay.setVisibility(View.VISIBLE);

            }
        });

        binding.buttonAddMore.setOnClickListener(this::onClick);
        binding.buttonConfirmAndPay.setOnClickListener(this::onClick);
binding.buttonSendToKitchen.setOnClickListener(this::onClick);
        return binding.getRoot();
    }

    private void callRestaurantDiscountAPI(OrderDetailItem orderDetailItem) {

        Set<Food> foods = payLaterFoodViewModel.getSelectedFoods().getValue();
        Double totalAmount = 0.00D;

        if (foods != null) {
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

                    int freeToppingSelectionAllowed = food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed().equals("") ? 0 : Integer.parseInt(food.getFoodItemExtraToppings().get(0).getFreeToppingSelectionAllowed());

                    for (int i = freeToppingSelectionAllowed; i < food.getFoodItemExtraToppings().size(); i++) {
                        totalFoodItemExtraToppingPrice += Double.parseDouble(food.getFoodItemExtraToppings().get(i).getFoodPriceAddons());
                    }
                    totalAmount += tmpTotalAmount + totalFoodItemExtraToppingPrice;
                }

            }
        }

        subTotalAmount = totalAmount + Double.parseDouble(orderDetailItem.getSubTotal());

        payLaterCartViewModel.getServiceCharge(getActivity(), branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, Utility.DECIMAL_FORMAT.format(subTotalAmount), new NetworkOperations(true));

    }

    FoodAdapter.OnPlusClick onPlusClick = (position, food) -> {

        food.setFoodCount(food.getFoodCount() + 1);

        Set<Food> selectedFoods = payLaterFoodViewModel.getSelectedFoods().getValue();
        selectedFoods.remove(food);
        selectedFoods.add(food);
        payLaterFoodViewModel.setSelectedFoods(selectedFoods);

        foodAdapter.notifyDataSetChanged();

    };

    FoodAdapter.OnMinusClick onMinusClick = (position, food) -> {
        food.setFoodCount(food.getFoodCount() - 1);

        Set<Food> selectedFoods = payLaterFoodViewModel.getSelectedFoods().getValue();
        if (food.getFoodCount() == food.getFoodCountLimit()) {
            selectedFoods.remove(food);
            foodAdapter.remove(food);
        }
        payLaterFoodViewModel.setSelectedFoods(selectedFoods);

        foodAdapter.notifyDataSetChanged();

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
        if (v.getId() == binding.buttonAddMore.getId()) {
            initiatePayLaterAddFoodFragment();
        } else if (v.getId() == binding.buttonConfirmAndPay.getId()) {

            payLaterCartViewModel.getSubTotal().setValue(Utility.DECIMAL_FORMAT.format(subTotalAmount));
            payLaterCartViewModel.getToPay().setValue(Utility.DECIMAL_FORMAT.format(toPayPrice));

            payLaterCartViewModel.getFoodTax().setValue(Utility.DECIMAL_FORMAT.format(foodTax));
            payLaterCartViewModel.getDrinkTax().setValue(Utility.DECIMAL_FORMAT.format(drinkTax));

            initiatePayLaterPaymentFragment(orderID);
        }else if (v.getId()==binding.buttonSendToKitchen.getId()){
//            orderSendToKitchen();
        }
    }

    private void orderSendToKitchen() {
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
        RmGetServiceChargeResponse serviceCharge = payLaterCartViewModel.getServiceCharge().getValue();

        String restaurantDiscount = cartViewModel.getRestaurantDiscount().getValue();

        String couponCode = cartViewModel.getCouponCode().getValue();

        String couponCodePrice = cartViewModel.getCouponCodeDiscount().getValue();

        String loyaltyPayDiscount = cartViewModel.getLoyaltyCodeDiscount().getValue();

        String selectedTableID = cartViewModel.getSelectedTableID().getValue();

        String numberOfPeople = cartViewModel.getNumberOfPeople().getValue();

        String foodTax = cartViewModel.getFoodTax().getValue();
        String drinkTax = cartViewModel.getDrinkTax().getValue();

        cartViewModel.orderSendToKitchen(getActivity(), branchId, "", "", "", itemId.toString(), quantity.toString(), price.toString(), sizeItemId.toString(),
                extraItemId.toString(), orderID, customerId, Utility.DECIMAL_FORMAT.format(toPayPrice), Utility.DECIMAL_FORMAT.format(subTotalAmount), serviceCharge.getDeliveryChargeValue(),
                couponCode, couponCodePrice, "", serviceCharge.getSalesTaxAmount(), "", "", restaurantDiscount,
                "", serviceCharge.getServiceFees(), "0", "", "", serviceCharge.getPackageFees(),
                serviceCharge.getVatTax(), "", "", "", loyaltyPayDiscount, selectedTableID, "", "", "",
                foodTax, drinkTax, "", "", numberOfPeople, new NetworkOperations(true));

        cartViewModel.getOrderSendToKitchenResponse().observe(this, orderSendToKitchenResponse -> {

            if (orderSendToKitchenResponse != null) {
                binding.buttonSendToKitchen.setVisibility(View.GONE);
                binding.buttonConfirmAndPay.setVisibility(View.VISIBLE);

                cartViewModel.getOrderSendToKitchenResponse().removeObservers(this);
            }

        });
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

    private void initiatePayLaterAddFoodFragment() {
        PayLaterAddFoodFragment payLaterAddFoodFragment = PayLaterAddFoodFragment.newInstance(containerID, 0);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, payLaterAddFoodFragment);
        transaction.addToBackStack(PayLaterAddFoodFragment.getTAG());
        transaction.commit();
    }

    private void initiatePayLaterPaymentFragment(String orderID) {
        PayLaterPaymentFragment paymentFragment = PayLaterPaymentFragment.newInstance(containerID, orderID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, paymentFragment);
        transaction.addToBackStack(paymentFragment.getTag());
        transaction.commit();
    }

}
