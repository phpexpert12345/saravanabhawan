package com.harperskebab.view.ui.fragments.paylater;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogPayBinding;
import com.harperskebab.databinding.FragmentPayLaterPaymentBinding;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.model.OrderDetailItem;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.RFInterface;
import com.harperskebab.network.retrofit.responsemodels.RmGetServiceChargeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmStripeUpdateChargeResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.ui.activities.PayLaterOrderCompleteActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.OrderViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.harperskebab.viewmodel.paylater.PayLaterCartViewModel;
import com.harperskebab.viewmodel.paylater.PayLaterFoodViewModel;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayLaterPaymentFragment extends BaseFragment {
    private static final String TAG = "PayLaterPaymentFragment";

    private FragmentPayLaterPaymentBinding binding;

    private UserViewModel userViewModel;
    private PayLaterFoodViewModel payLaterFoodViewModel;
    private PayLaterCartViewModel payLaterCartViewModel;
    private OrderViewModel orderViewModel;

    private int containerID;
    private String orderID;

    private RFInterface rfInterface = Utility.getRetrofitInterface();
    private ProgressDialog progressDialog;

    public PayLaterPaymentFragment() {
    }

    public static PayLaterPaymentFragment newInstance(int containerID, String orderID) {

        PayLaterPaymentFragment fragment = new PayLaterPaymentFragment();
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
        payLaterFoodViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterFoodViewModel.class);
        payLaterCartViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterCartViewModel.class);
        orderViewModel = ViewModelFactory.getInstance(getActivity()).create(OrderViewModel.class);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPayLaterPaymentBinding.inflate(inflater, container, false);

        binding.textViewToPayPrice.setText(
                Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                        + " " +
                        payLaterCartViewModel.getToPay().getValue()
        );

        if (!restaurantViewModel.getRestaurant().getValue().getRestaurantCardacceptAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewCreditDebitCard.setVisibility(View.GONE);
        }

        if (!restaurantViewModel.getRestaurant().getValue().getPayPaypalAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewPaypal.setVisibility(View.GONE);
        }

        binding.radioButtonCreditDebitCard.setOnClickListener(this::onClick);
        binding.radioButtonPaypal.setOnClickListener(this::onClick);

        binding.cardViewCreditDebitCard.setOnClickListener(this::onClick);
        binding.cardViewPaypal.setOnClickListener(this::onClick);

        binding.buttonMakePayment.setOnClickListener(this::onClick);


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

        if (v.getId() == binding.cardViewCreditDebitCard.getId() || v.getId() == binding.radioButtonCreditDebitCard.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(true);
            binding.radioButtonPaypal.setChecked(false);

        } else if (v.getId() == binding.cardViewPaypal.getId() || v.getId() == binding.radioButtonPaypal.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(false);
            binding.radioButtonPaypal.setChecked(true);

        } else if (v.getId() == binding.buttonMakePayment.getId()) {

            if (binding.radioButtonCreditDebitCard.isChecked() || binding.radioButtonPaypal.isChecked()) {

                boolean isdirectAPICall = false;

                Set<Food> selectedFoods = payLaterFoodViewModel.getSelectedFoods().getValue();

                StringBuilder itemId = new StringBuilder();
                StringBuilder quantity = new StringBuilder();
                StringBuilder price = new StringBuilder();
                StringBuilder sizeItemId = new StringBuilder();
                StringBuilder extraItemId = new StringBuilder();

                String orderType = "", deliveryDate = "", delivieryTime = "", deliveryCharge = "", couponPrice = "", instruction = "", riderTip = "", restaurantDiscount = "", loyaltyAmount = "", tableNumberAssign = "";
                RmGetServiceChargeResponse serviceCharge;

                if (selectedFoods != null && selectedFoods.size() != 0) {
                    for (Food food : selectedFoods) {

                        itemId.append(itemId.toString().equals("") ? "" + food.getItemID() : "," + food.getItemID());

                        quantity.append(quantity.toString().equals("") ? "" + food.getFoodCount() : "," + food.getFoodCount());

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

                    OrderDetailItem orderDetailItem = orderViewModel.getOrderDetailResponse().getValue().getOrderDetailItem().get(0);

                    orderType = orderDetailItem.getOrderType();
                    deliveryCharge = orderDetailItem.getDeliveryCharge();

                    deliveryDate = orderDetailItem.getRequestforDate();
                    delivieryTime = orderDetailItem.getRequestforTime();
                    couponPrice = orderDetailItem.getCouponPrice();
                    instruction = orderDetailItem.getCustomerInstruction();
                    riderTip = orderDetailItem.getExtraTipAddAmount();
                    restaurantDiscount = orderDetailItem.getDiscountPrice();
                    loyaltyAmount = orderDetailItem.getPayByLoyality();
                    tableNumberAssign = orderDetailItem.getTableBookingNumber();
                    serviceCharge = payLaterCartViewModel.getServiceCharge().getValue();

                } else {
                    isdirectAPICall = true;
                }

                String customerId = userViewModel.getSignInResponse().getValue().getCustomerId();
                String toPayPrice = payLaterCartViewModel.getToPay().getValue();
                String subTotalAmount = payLaterCartViewModel.getSubTotal().getValue();
                String foodTax = payLaterCartViewModel.getFoodTax().getValue();
                String drinkTax = payLaterCartViewModel.getDrinkTax().getValue();

                Log.d(TAG, "onClick: itemId:" + itemId);
                Log.d(TAG, "onClick: quantity:" + quantity);
                Log.d(TAG, "onClick: price:" + price);
                Log.d(TAG, "onClick: sizeItemId:" + sizeItemId);
                Log.d(TAG, "onClick: extraItemId:" + extraItemId);

                if (binding.radioButtonCreditDebitCard.isChecked()) {
                    //todo
                    showPayDialog(itemId.toString(), quantity.toString(), price.toString(), sizeItemId.toString(), extraItemId.toString(), customerId, toPayPrice, subTotalAmount,
                            "Card", deliveryDate, delivieryTime, deliveryCharge, couponPrice, orderType, instruction, riderTip, restaurantDiscount,
                            loyaltyAmount, tableNumberAssign, foodTax, drinkTax, isdirectAPICall);
                } else if (binding.radioButtonPaypal.isChecked()) {
                    PopMessage.makeLongToast(getActivity(), "WIP");
                }
            } else {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseSelectPaymentType());
            }

        }

    }

    private void showPayDialog(String itemId, String quantity, String price, String sizeItemId, String extraItemId, String userID, String toPayPrice, String subTotalAmount,
                               String paymentType, String deliveryDate, String deliveryTime, String deliveryCharge, String couponCodePrice,
                               String orderType, String instruction, String riderTip, String restaurantDiscount, String loyaltyAmount, String tableNumberAssign,
                               String foodTax, String drinkTax, boolean isDirectAPICall) {

        DialogPayBinding dialogPayBinding = DialogPayBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogPay = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogPay.setContentView(dialogPayBinding.getRoot());

        dialogPayBinding.buttonPay.setOnClickListener(v -> {

            Card card = dialogPayBinding.cardInputWidget.getCard();

            if (card != null) {

                progressDialog = Utility.createProgressDialog(getActivity());
                new Stripe(getActivity().getApplicationContext(), PaymentConfiguration.getInstance(getActivity().getApplicationContext()).getPublishableKey())
                        .createToken(card, new ApiResultCallback<Token>() {

                            public void onSuccess(Token token) {

                                rfInterface.updateCharge(
                                        Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                                        toPayPrice, restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole(),
                                        token.getId(), userViewModel.getSignInResponse().getValue().getUserEmail())
                                        .enqueue(new Callback<RmStripeUpdateChargeResponse>() {
                                            @Override
                                            public void onResponse(Call<RmStripeUpdateChargeResponse> call, Response<RmStripeUpdateChargeResponse> response) {
                                                progressDialog.dismiss();
                                                if (response.isSuccessful()) {

                                                    if (response.body().getResponse().equalsIgnoreCase("success")) {
                                                        if (isDirectAPICall) {
                                                            //todo
                                                            callPlaceOrderDirectAPI(userID, paymentType, toPayPrice);
                                                        } else {
                                                            //todo
                                                            callPlaceOrderAPI(
                                                                    itemId, quantity, price, sizeItemId, extraItemId, userID, paymentType,
                                                                    toPayPrice, subTotalAmount, deliveryDate, deliveryTime, deliveryCharge, couponCodePrice,
                                                                    orderType, instruction, riderTip, restaurantDiscount, loyaltyAmount, tableNumberAssign, foodTax, drinkTax
                                                            );
                                                        }

                                                    } else {
                                                        Log.d(TAG, "onFailure: Exception: " + response.body().getMessage());
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RmStripeUpdateChargeResponse> call, Throwable t) {
                                                Log.e(TAG, "onFailure: Throwable: ", t);
                                            }
                                        });
                            }

                            public void onError(Exception error) {
                                Log.d("Stripe", error.getLocalizedMessage());
                            }
                        });
            } else {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterCardDetails());
            }

        });

        dialogPay.show();

    }

    private void callPlaceOrderDirectAPI(String userID, String paymentType, String toPayPrice) {

        payLaterCartViewModel.placeOrder(getActivity(), Constant.API.FOOD_KEY, Constant.API.FOOD_KEY, orderID, "", userID, paymentType, toPayPrice, new NetworkOperations(true));

        payLaterCartViewModel.getPlaceOrderResponse().observe(this, placeOrderResponse -> {
            if (placeOrderResponse != null) {
                initiateOrderCompleteActivity();
                payLaterCartViewModel.getPlaceOrderResponse().removeObservers(this);
            }
        });

    }

    private void callPlaceOrderAPI(String itemId, String quantity, String price, String sizeItemId, String extraItemId, String customerId, String paymentType,
                                   String toPayPrice, String subTotalAmount, String deliveryDate, String deliveryTime, String deliveryCharge, String couponCodePrice,
                                   String orderType, String instruction, String riderTip, String restaurantDiscount, String loyaltyAmount, String tableNumberAssign,
                                   String foodTax, String drinkTax) {

        RmGetServiceChargeResponse serviceCharge = payLaterCartViewModel.getServiceCharge().getValue();

        payLaterCartViewModel.placeOrder(getActivity(), Constant.API.FOOD_KEY, Constant.API.FOOD_KEY, "", "", "", "", itemId,
                quantity, price, sizeItemId, extraItemId, customerId, "", paymentType, toPayPrice, subTotalAmount, deliveryDate, deliveryTime, "",
                deliveryCharge, "", couponCodePrice, "", serviceCharge.getSalesTaxAmount(), orderType, instruction, riderTip, "",
                "", restaurantDiscount, "", serviceCharge.getServiceFees(), "0", "",
                "", "", serviceCharge.getPackageFees(), "", "", "", "",
                serviceCharge.getVatTax(), "", "", loyaltyAmount, tableNumberAssign, "", "", "", "",
                "", "", foodTax, drinkTax, "", "", orderID
                , new NetworkOperations(true));

        payLaterCartViewModel.getPlaceOrderResponse().observe(this, placeOrderResponse -> {
            if (placeOrderResponse != null) {
                initiateOrderCompleteActivity();
                payLaterCartViewModel.getPlaceOrderResponse().removeObservers(this);
            }
        });

    }

    private void initiateOrderCompleteActivity() {
        startActivity(
                new Intent(getActivity(), PayLaterOrderCompleteActivity.class)
        );
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
