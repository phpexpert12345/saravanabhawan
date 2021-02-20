package com.harperskebab.view.ui.fragments;

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
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogAllergyBinding;
import com.harperskebab.databinding.DialogPayBinding;
import com.harperskebab.databinding.FragmentPaymentBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.RFInterface;
import com.harperskebab.network.retrofit.responsemodels.RmGetServiceChargeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmStripeUpdateChargeResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.AddressAdapter;
import com.harperskebab.view.adapter.CardListAdapter;
import com.harperskebab.view.adapter.DeliveryTimeAdapter;
import com.harperskebab.view.ui.activities.OrderCompleteActivity;
import com.harperskebab.viewmodel.AllergyViewModel;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.DeliveryTimeSlotViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends BaseFragment {
    private static final String TAG = "PaymentFragment";
private CardListAdapter cardListAdapter;
    private FragmentPaymentBinding binding;
    DialogPayBinding dialogPayBinding;
    private UserViewModel userViewModel;
    private FoodViewModel foodViewModel;
    private CartViewModel cartViewModel;
    private AllergyViewModel allergyViewModel;
    private DeliveryTimeSlotViewModel deliveryTimeSlotViewModel;
    private DeliveryTimeAdapter adapter;
    private int containerID;
    String isSaveCard = "No";
    String branchId;
    private RFInterface rfInterface = Utility.getRetrofitInterface();
    private ProgressDialog progressDialog;
    double deliveryCharge=0.0;
    double serviceFees=0.0;
    double packageFees=0.0;
    double salesTax=0.0;
    double vatTax=0.0;
    double riderTipAmount=0.0;


    private CardListAdapter.OnCardClick onCardClick = (position,isSelected,cardList) -> {
//        //todo
//        showMessage("Alert", "Are you sure you want to delete?", "YES", "NO", dialogInterface -> {
//            deleteAddress(deliveryAddress);
//            dialogInterface.dismiss();
//        }, dialogInterface -> {
//            dialogInterface.dismiss();
//        });
        if (!isSelected){
            dialogPayBinding.cardInputWidget.setCardNumber("");
            dialogPayBinding.cardInputWidget.setExpiryDate(01, 0);
        }else {
            dialogPayBinding.cardInputWidget.setCardNumber(cardList.getCard_number());
            dialogPayBinding.cardInputWidget.setExpiryDate(Integer.parseInt(cardList.getCard_expire_month()), Integer.parseInt(cardList.getCard_expire_year()));
        }
        };

    public PaymentFragment() {
    }

    public static PaymentFragment newInstance(int containerID) {

        PaymentFragment fragment = new PaymentFragment();
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

        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        branchId= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId", "");
        foodViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodViewModel.class);
        cartViewModel = ViewModelFactory.getInstance(getActivity()).create(CartViewModel.class);
        allergyViewModel = ViewModelFactory.getInstance(getActivity()).create(AllergyViewModel.class);
        deliveryTimeSlotViewModel = ViewModelFactory.getInstance(getActivity()).create(DeliveryTimeSlotViewModel.class);
        deliveryTimeSlotViewModel.getTimeSlot(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        allergyViewModel.getAllergy(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(false));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getPayment());
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
binding.txtSelectPaymentType.setText(languageViewModel.getLanguageResponse().getValue().getPleaseSelectPaymentType());
binding.txtCreditDebitCard.setText(languageViewModel.getLanguageResponse().getValue().getCreditDebitCard());
binding.txtPaypal.setText(languageViewModel.getLanguageResponse().getValue().getPaypal());
binding.textViewCashOnDelivery.setText(languageViewModel.getLanguageResponse().getValue().getCashOnDelivery());
binding.txtNoteText.setText(languageViewModel.getLanguageResponse().getValue().getLeaveANoteForTheRestaurantText());
        binding.textViewToPayPrice.setText(
                Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                        + " " +
                        cartViewModel.getToPay().getValue()
        );

        if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("eat-in")) {
            binding.linearLayoutTime.setVisibility(View.GONE);
            binding.cardViewPayLater.setVisibility(View.VISIBLE);
        }else {
            binding.cardViewPayLater.setVisibility(View.GONE);
        }

//        binding.cardViewPayLater.setVisibility(View.VISIBLE);

        if (!restaurantViewModel.getRestaurant().getValue().getRestaurantCardacceptAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewCreditDebitCard.setVisibility(View.GONE);
        }

        if (!restaurantViewModel.getRestaurant().getValue().getPayPaypalAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewPaypal.setVisibility(View.GONE);
        }

        if (!restaurantViewModel.getRestaurant().getValue().getRestaurantOnlycashonAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewCashOnDelivery.setVisibility(View.GONE);
        } else {

            if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("ear-in")) {
                binding.textViewCashOnDelivery.setText("Cash");
            }

        }

        if (!restaurantViewModel.getRestaurant().getValue().getPayLaterAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewPayLater.setVisibility(View.GONE);
        }

        binding.radioButtonASAP.setOnClickListener(this::onClick);
        binding.radioButtonLater.setOnClickListener(this::onClick);

        binding.radioButtonCreditDebitCard.setOnClickListener(this::onClick);
        binding.radioButtonPaypal.setOnClickListener(this::onClick);
        binding.radioButtonCashOnDelivery.setOnClickListener(this::onClick);
        binding.radioButtonPayLater.setOnClickListener(this::onClick);

        binding.cardViewCreditDebitCard.setOnClickListener(this::onClick);
        binding.cardViewPaypal.setOnClickListener(this::onClick);
        binding.cardViewCashOnDelivery.setOnClickListener(this::onClick);
        binding.cardViewPayLater.setOnClickListener(this::onClick);

        binding.textViewAllergy.setOnClickListener(this::onClick);
        binding.buttonMakePayment.setOnClickListener(this::onClick);


        deliveryTimeSlotViewModel.getDeliveryTimes().observe(this, deliveryTimes -> {

            if (deliveryTimes != null) {
                adapter = new DeliveryTimeAdapter(getActivity(), deliveryTimes);
                binding.recyclerViewDeliverySchedule.setAdapter(adapter);
            }

        });

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

        if (v.getId() == binding.radioButtonASAP.getId()) {

            binding.textViewDeliverySchedule.setVisibility(View.GONE);
            binding.recyclerViewDeliverySchedule.setVisibility(View.GONE);

        } else if (v.getId() == binding.radioButtonLater.getId()) {

            binding.textViewDeliverySchedule.setVisibility(View.VISIBLE);
            binding.recyclerViewDeliverySchedule.setVisibility(View.VISIBLE);

        } else if (v.getId() == binding.cardViewCreditDebitCard.getId() || v.getId() == binding.radioButtonCreditDebitCard.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(true);
            binding.radioButtonPaypal.setChecked(false);
            binding.radioButtonCashOnDelivery.setChecked(false);
            binding.radioButtonPayLater.setChecked(false);

        } else if (v.getId() == binding.cardViewPaypal.getId() || v.getId() == binding.radioButtonPaypal.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(false);
            binding.radioButtonPaypal.setChecked(true);
            binding.radioButtonCashOnDelivery.setChecked(false);
            binding.radioButtonPayLater.setChecked(false);

        } else if (v.getId() == binding.cardViewCashOnDelivery.getId() || v.getId() == binding.radioButtonCashOnDelivery.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(false);
            binding.radioButtonPaypal.setChecked(false);
            binding.radioButtonCashOnDelivery.setChecked(true);
            binding.radioButtonPayLater.setChecked(false);

        } else if (v.getId() == binding.cardViewPayLater.getId() || v.getId() == binding.radioButtonPayLater.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(false);
            binding.radioButtonPaypal.setChecked(false);
            binding.radioButtonCashOnDelivery.setChecked(false);
            binding.radioButtonPayLater.setChecked(true);

        } else if (v.getId() == binding.textViewAllergy.getId()) {
            showRedeemLoyaltyPointDialog();
        } else if (v.getId() == binding.buttonMakePayment.getId()) {
            double foodCost=0;
            if (binding.radioButtonCreditDebitCard.isChecked() || binding.radioButtonPaypal.isChecked() || binding.radioButtonCashOnDelivery.isChecked() || binding.radioButtonPayLater.isChecked()) {

                List<Food> selectedFoods = new ArrayList<>();

//                if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("eat-in")) {
////                    selectedFoods = cartViewModel.getFoods().getValue();
////                } else {
//                    selectedFoods.addAll(foodViewModel.getSelectedFoods().getValue());
//                }else {
//                    selectedFoods.addAll(foodViewModel.getSelectedFoods().getValue());
//                }
                CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
                CartDao cartDao=customerAppDatabase.cartDao();
                List<CartItem>cart_items=cartDao.getCartItems();

                StringBuilder itemId = new StringBuilder();
                StringBuilder quantity = new StringBuilder();
                StringBuilder price = new StringBuilder();
                StringBuilder sizeItemId = new StringBuilder();
                StringBuilder extraItemId = new StringBuilder();
                StringBuilder extraItemIdOne = new StringBuilder();
                StringBuilder extraItemName = new StringBuilder();
                StringBuilder extraItemPrice = new StringBuilder();
                if(cart_items.size()>0) {

                    for (CartItem cartItem : cart_items) {
                        foodCost += Double.parseDouble(cartItem.total_price) * cartItem.quantity;
                        itemId.append(itemId.toString().equals("") ? "" + cartItem.item_id : "," + cartItem.item_id);
                        quantity.append(quantity.toString().equals("") ? "" + cartItem.quantity : "," + cartItem.quantity);
                        price.append(price.toString().equals("") ? cartItem.total_price : "," + cartItem.total_price);
                        sizeItemId.append(
                                sizeItemId.toString().equals("") ? cartItem.item_id + "_" + cartItem.item_size_id : "," + cartItem.item_id + "_" + cartItem.item_size_id);
                        if (!cartItem.top_ids.equalsIgnoreCase("")) {
                            if (cartItem.top_ids.contains(",")) {
                                if (extraItemId.length() > 0) {
                                    extraItemId.append("_");
                                }
                                if(extraItemIdOne.length()>0){
                                    extraItemIdOne.append("_");
                                }
                                String[] tops = cartItem.top_ids.split(",");
                                String[]  Name=cartItem.top_name.split(",");
                                String[]prices=cartItem.top_price.split(",");

                                if (tops.length > 0) {
                                    for(int i=0;i<tops.length;i++){
                                        if (extraItemId.length() > 0) {
                                            extraItemId.append(",");
                                        }
                                        if(extraItemIdOne.length()>0){
                                            extraItemIdOne.append(",");
                                        }
                                        if(extraItemName.length()>0){
                                            extraItemName.append(",");
                                        }
                                        extraItemIdOne.append(tops[i]);
                                        extraItemName.append(Name[i]);
                                        extraItemId.append(cartItem.item_id).append("_").append(cartItem.item_size_id).append("_").append(tops[i]);
                                    }
                                }

                            }
                            else{
                                if (extraItemId.length() > 0) {
                                    extraItemId.append("_");
                                }
                                if(extraItemIdOne.length()>0){
                                    extraItemIdOne.append("_");
                                }
                                if(extraItemName.length()>0){
                                    extraItemName.append("_");
                                }
                                extraItemId.append(cartItem.item_id).append("_").append(cartItem.item_size_id).append("_").append(cartItem.top_ids);
                                extraItemIdOne.append(cartItem.top_ids);
                                extraItemName.append(cartItem.top_name);

                            }
                        }

                    }
                }

//                }
//                double intPrice=0;
//                for (Food food : selectedFoods) {
//
//                    itemId.append(itemId.toString().equals("") ? "" + food.getItemID() : "," + food.getItemID());
//
//                    quantity.append(quantity.toString().equals("") ? "" + food.getFoodCount() : "," + food.getFoodCount());
//
//                    if (food.getExtraavailable().equalsIgnoreCase("Yes")&&food.getSizeavailable().equalsIgnoreCase("Yes")){
//                        for (int i=0;i<food.getFoodItemExtraToppings().size();i++){
//                            String strPrice=food.getFoodItemExtraToppings().get(i).getFoodPriceAddons();
//                            intPrice=intPrice+Double.parseDouble(strPrice);
//                        }
//                        double val=intPrice+Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice());
//                        price.append(price.toString().equals("") ? val : "," +val);
//
//                    }else if (food.getSizeavailable().equalsIgnoreCase("Yes")&&food.getExtraavailable().equalsIgnoreCase("No")) {
//                        price.append(price.toString().equals("") ? food.getFoodItem().getRestaurantPizzaItemPrice() : "," + food.getFoodItem().getRestaurantPizzaItemPrice());
//                    }else if (food.getSizeavailable().equalsIgnoreCase("No")&&food.getExtraavailable().equalsIgnoreCase("Yes")) {
//                        for (int i=0;i<food.getFoodItemExtraToppings().size();i++){
//                            String strPrice=food.getFoodItemExtraToppings().get(i).getFoodPriceAddons();
//                            intPrice=intPrice+Double.parseDouble(strPrice);
//                        }
//                        double val=intPrice+Double.parseDouble(food.getRestaurantPizzaItemPrice());
//                        price.append(price.toString().equals("") ? val : "," +val);
//                    }else {
//                        price.append(price.toString().equals("") ? food.getRestaurantPizzaItemPrice() : "," + food.getRestaurantPizzaItemPrice());
//
//                    }
//
//
//                    if (food.getSizeavailable().equalsIgnoreCase("yes")) {
//                        sizeItemId.append(
//                                sizeItemId.toString().equals("") ? food.getItemID() + "_" + food.getFoodItem().getId() : "," + food.getItemID() + "_" + food.getFoodItem().getId()
//                        );
//                    } else {
//                        sizeItemId.append(sizeItemId.toString().equals("") ? food.getItemID() + "_" +"0" : ","+food.getItemID() + "_" +"0");
//                    }
//
//                    if (food.getExtraavailable().equalsIgnoreCase("yes")) {
//
//                        String foodItemID = food.getSizeavailable().equalsIgnoreCase("yes") ? "" + food.getFoodItem().getId() : "0";
//
//                        for (FoodItemExtraTopping foodItemExtraTopping : food.getFoodItemExtraToppings()) {
//                            extraItemId.append(
//                                    extraItemId.toString().equals("") ? food.getItemID() + "_" + foodItemID + "_" + foodItemExtraTopping.getExtraID() : "," + food.getItemID() + "_" + foodItemID + "_" + foodItemExtraTopping.getExtraID()
//                            );
//                        }
//
//                    } else {
//                        String foodItemID = food.getSizeavailable().equalsIgnoreCase("yes") ? "" + food.getFoodItem().getId() : "0";
//                        extraItemId.append(extraItemId.toString().equals("") ? food.getItemID() + "_" + foodItemID + "_" +"0" : ","+food.getItemID() + "_" + foodItemID + "_" +"0");
//                    }
//                    //extra item only id
//                    if (food.getExtraavailable().equalsIgnoreCase("yes")) {
//
//                        for (FoodItemExtraTopping foodItemExtraTopping : food.getFoodItemExtraToppings()) {
//                            extraItemIdOne.append(
//                                    extraItemIdOne.toString().equals("") ? foodItemExtraTopping.getExtraID() : ","+ foodItemExtraTopping.getExtraID()
//                            );
//                        }
//
//                    } else {
//                        extraItemIdOne.append(extraItemIdOne.toString().equals("") ? "0" : "_0");
//                    }
//
//
//                    //extra name
//                    if (food.getExtraavailable().equalsIgnoreCase("yes")) {
//
//                        for (FoodItemExtraTopping foodItemExtraTopping : food.getFoodItemExtraToppings()) {
//                            extraItemName.append(
//                                    extraItemName.toString().equals("") ? foodItemExtraTopping.getFoodAddonsName() : ","+ foodItemExtraTopping.getFoodAddonsName()
//                            );
//                        }
//
//                    } else {
//                        extraItemName.append(extraItemName.toString().equals("") ? "0" : "_0");
//                    }
//
//
//                    //extra price
//                    if (food.getExtraavailable().equalsIgnoreCase("yes")) {
//
//                        for (FoodItemExtraTopping foodItemExtraTopping : food.getFoodItemExtraToppings()) {
//                            extraItemPrice.append(
//                                    extraItemPrice.toString().equals("") ? foodItemExtraTopping.getFoodPriceAddons() : ","+ foodItemExtraTopping.getFoodPriceAddons()
//                            );
//                        }
//
//                    } else {
//                        extraItemPrice.append(extraItemPrice.toString().equals("") ? "0" : "_0");
//                    }
//
//                    //foodCost
//                   double foodPrice=0;
//                    if (food.getSizeavailable().equalsIgnoreCase("Yes")&&food.getExtraavailable().equalsIgnoreCase("Yes")) {
//                        for (int i = 0; i < food.getFoodItemExtraToppings().size(); i++) {
//                            String strPrice = food.getFoodItemExtraToppings().get(i).getFoodPriceAddons();
//                            foodPrice = foodPrice + Double.parseDouble(strPrice);
//                        }
//                        foodCost = foodPrice + Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice());
//                    }else if (food.getSizeavailable().equalsIgnoreCase("Yes")&&food.getExtraavailable().equalsIgnoreCase("No")) {
//                        foodCost = foodCost + Double.parseDouble(food.getFoodItem().getRestaurantPizzaItemPrice());
//                    }else if (food.getSizeavailable().equalsIgnoreCase("No")&&food.getExtraavailable().equalsIgnoreCase("Yes")){
//                        for (int i = 0; i < food.getFoodItemExtraToppings().size(); i++) {
//                            String strPrice = food.getFoodItemExtraToppings().get(i).getFoodPriceAddons();
//                            foodPrice = foodPrice + Double.parseDouble(strPrice);
//                        }
//                        foodCost = foodPrice + Double.parseDouble(food.getRestaurantPizzaItemPrice());
//                    }else {
//                         foodCost=foodCost+Double.parseDouble(food.getRestaurantPizzaItemPrice());
//                    }
//                }

                String customerId = userViewModel.getSignInResponse().getValue().getCustomerId();
                String orderType = cartViewModel.getOrderType().getValue();
                String selectedDeliveryAddressID = "";
                if (orderType.equalsIgnoreCase("Delivery")) {
                    selectedDeliveryAddressID = cartViewModel.getSelectedDeliveryAddress().getValue().getId().toString();
                }

                String foodTax = cartViewModel.getFoodTax().getValue();
                String drinkTax = cartViewModel.getDrinkTax().getValue();

                String toPayPrice = cartViewModel.getToPay().getValue();
                String subTotalAmount = cartViewModel.getSubTotal().getValue();
                String deliveyDate = Utility.DELIVERY_DATE_FORMAT.format(new Date());

                String deliveyTime = Utility.DELIVERY_TIME_FORMAT.format(new Date());
                if (binding.radioButtonLater.isChecked()) {
                    deliveyTime = adapter.getSelectedDeliveryTime().getGetTime();
                }

                String instruction = binding.editTextInstruction.getText().toString().replace("'", "");

                String riderTip = cartViewModel.getRiderTip().getValue();

                RmGetServiceChargeResponse serviceCharge = cartViewModel.getServiceCharge().getValue();

                String deliveryCharge = "";
                if (orderType.equalsIgnoreCase("Delivery")) {
                    if(serviceCharge!=null) {
                        deliveryCharge = serviceCharge.getDeliveryChargeValue();
                    }
                }

                String restaurantDiscount = cartViewModel.getRestaurantDiscount().getValue();

                String couponCode = cartViewModel.getCouponCode().getValue();

                String couponCodePrice = cartViewModel.getCouponCodeDiscount().getValue();

                String loyaltyPayDiscount = cartViewModel.getLoyaltyCodeDiscount().getValue();

                String numberOfPeople = "";
                String tableNuberId = "";
                if (cartViewModel.getOrderType().getValue().equalsIgnoreCase("eat-in")) {
                    numberOfPeople = cartViewModel.getNumberOfPeople().getValue();
                    tableNuberId=cartViewModel.getSelectedTableID().getValue();
                }

                Log.d(TAG, "onClick: itemId:" + itemId);
                Log.d(TAG, "onClick: quantity:" + quantity);
                Log.d(TAG, "onClick: price:" + price);
                Log.d(TAG, "onClick: sizeItemId:" + sizeItemId);
                Log.d(TAG, "onClick: extraItemId:" + extraItemId);


                if (binding.radioButtonCreditDebitCard.isChecked()) {
                    showPayDialog(foodCost,itemId.toString(), quantity.toString(), price.toString(), sizeItemId.toString(), extraItemId.toString(), customerId, selectedDeliveryAddressID, "Card", toPayPrice, subTotalAmount,
                            deliveyDate, deliveyTime, deliveryCharge, couponCode, couponCodePrice, orderType, instruction, riderTip, restaurantDiscount, loyaltyPayDiscount, foodTax, drinkTax, numberOfPeople,tableNuberId,extraItemIdOne.toString(),extraItemPrice.toString(),extraItemName.toString());
                } else if (binding.radioButtonPaypal.isChecked()) {
                    PopMessage.makeLongToast(getActivity(), "WIP");
                } else if (binding.radioButtonCashOnDelivery.isChecked()) {
                    callPlaceOrderAPI(foodCost,branchId,"", "", "", "", "", itemId.toString(), quantity.toString(), price.toString(), sizeItemId.toString(), extraItemId.toString(), customerId, selectedDeliveryAddressID, "CASH", toPayPrice, subTotalAmount,
                            deliveyDate, deliveyTime, deliveryCharge, couponCode, couponCodePrice, orderType, instruction, riderTip, restaurantDiscount, loyaltyPayDiscount, foodTax, drinkTax, numberOfPeople,tableNuberId,extraItemIdOne.toString(),extraItemPrice.toString(),extraItemName.toString());
                } else if (binding.radioButtonPayLater.isChecked()) {
                    callPlaceOrderAPI(foodCost,branchId,"", "", "", "", "", itemId.toString(), quantity.toString(), price.toString(), sizeItemId.toString(), extraItemId.toString(), customerId, selectedDeliveryAddressID, "payLater", toPayPrice, subTotalAmount,
                            deliveyDate, deliveyTime, deliveryCharge, couponCode, couponCodePrice, orderType, instruction, riderTip, restaurantDiscount, loyaltyPayDiscount, foodTax, drinkTax, numberOfPeople,tableNuberId,extraItemIdOne.toString(),extraItemPrice.toString(),extraItemName.toString());
                }
            } else {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseSelectPaymentType());
            }

        }

    }

    private void showPayDialog(double foodCost,String itemId, String quantity, String price, String sizeItemId, String extraItemId, String customerId, String selectedDeliveryAddressID, String paymentType,
                               String toPayPrice, String subTotalAmount, String deliveryDate, String deliveryTime, String deliveryCharge, String couponCode, String couponCodePrice,
                               String orderType, String instruction, String riderTip, String restaurantDiscount, String loyaltyPayDiscount, String foodTax, String drinkTax, String numberOfPeople,String tableNumberId,String extraItemIdOne,String extraItemPrice,String extraItemName) {
         dialogPayBinding = DialogPayBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogPay = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogPay.setContentView(dialogPayBinding.getRoot());

        callCardAPI( customerId);


        dialogPayBinding.checkBoxSaveCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    isSaveCard = "Yes";
                } else {
                    isSaveCard = "No";
                }
            }

        });
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
                                                    if (dialogPayBinding.checkBoxSaveCard.isChecked()) {
                                                        isSaveCard = "Yes";
                                                    } else {
                                                        isSaveCard = "No";
                                                    }
                                                    if (response.body().getResponse().equalsIgnoreCase("success")) {
                                                        callPlaceOrderAPI(foodCost,branchId,isSaveCard, card.getBrand(), card.getNumber(), card.getExpMonth().toString(), card.getExpYear().toString(), itemId, quantity, price, sizeItemId, extraItemId, customerId, selectedDeliveryAddressID, paymentType, toPayPrice, subTotalAmount, deliveryDate, deliveryTime,
                                                                deliveryCharge, couponCode, couponCodePrice, orderType, instruction, riderTip, restaurantDiscount, loyaltyPayDiscount, foodTax, drinkTax, numberOfPeople,tableNumberId,extraItemIdOne,extraItemPrice,extraItemName);
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
private void callCardAPI( String customerId){
    cartViewModel.CardList(getActivity(),branchId,Constant.API.FOOD_KEY, Constant.API.FOOD_KEY,customerId,new NetworkOperations(true));

    dialogPayBinding.recCardList.setLayoutManager(new LinearLayoutManager(getActivity()));
    dialogPayBinding.recCardList.setNestedScrollingEnabled(false);
    cartViewModel.getGetCardListResponse().observe(this, cardListResponse -> {
        //TODO
        if (cardListResponse != null) {
            if (cardListResponse.getCreditCardList().size() == 0) {
                dialogPayBinding.recCardList.setVisibility(View.GONE);
            } else {
                dialogPayBinding.recCardList.setVisibility(View.VISIBLE);
            }
            cardListAdapter = new CardListAdapter(getActivity(), cardListResponse.getCreditCardList(), onCardClick);
            dialogPayBinding.recCardList.setAdapter(cardListAdapter);
        }

    });
    }
    private void callPlaceOrderAPI(double foodCost,String branchId,String saveCard, String cardType, String cardNumber, String cardExpMonth, String cardExpYear, String itemId, String quantity, String price, String sizeItemId, String extraItemId, String customerId, String selectedDeliveryAddressID, String paymentType,
                                   String toPayPrice, String subTotalAmount, String deliveryDate, String deliveryTime, String deliveryCharge, String couponCode, String couponCodePrice,
                                   String orderType, String instruction, String riderTip, String restaurantDiscount, String loyaltyPayAmount, String foodTax, String drinkTax, String numberOfPeople,String tableNumberId,String extraItemIdOne,String extraItemPrice,String extraItemName) {

        RmGetServiceChargeResponse serviceCharge = cartViewModel.getServiceCharge().getValue();

        cartViewModel.placeOrder(getActivity(),branchId, saveCard, cardType, cardNumber, cardExpMonth, cardExpYear, Constant.API.FOOD_KEY, Constant.API.FOOD_KEY, "", "", "", "", itemId,
                quantity, price, sizeItemId, extraItemId, customerId, selectedDeliveryAddressID, paymentType, toPayPrice, subTotalAmount, deliveryDate, deliveryTime, "",
                deliveryCharge, couponCode, couponCodePrice, "", String.valueOf(salesTax), orderType, instruction, riderTip, "",
                "", restaurantDiscount, "", String.valueOf(serviceFees), "0", "",
                "", "", String.valueOf(packageFees), "", "", "", "",
                String.valueOf(vatTax), "", "", loyaltyPayAmount, tableNumberId, foodTax, drinkTax, "", "", numberOfPeople,String.valueOf(foodCost)
                ,extraItemIdOne,extraItemPrice,extraItemName, new NetworkOperations(true));

        cartViewModel.getPlaceOrderResponse().observe(this, placeOrderResponse -> {
            initiateOrderCompleteActivity(paymentType);

            cartViewModel.getPlaceOrderResponse().removeObservers(this);
            cartViewModel.getSelectedTableID().setValue("");
            cartViewModel.getNumberOfPeople().setValue("" );
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ITEM_COUNT", "");
            editor.apply();
        });

    }

    private void initiateOrderCompleteActivity(String paymentType) {

        Boolean isContinueOrderVisible = false;

        if (paymentType.equalsIgnoreCase("payLater")) {
            isContinueOrderVisible = true;
        }

        startActivity(
                new Intent(getActivity(), OrderCompleteActivity.class)
                        .putExtra(Constant.Data.IS_CONTINUE_ORDER_VISIBLE, isContinueOrderVisible)
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

    private void showRedeemLoyaltyPointDialog() {
        DialogAllergyBinding dialogAllergyBinding = DialogAllergyBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogAllergy = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogAllergy.setContentView(dialogAllergyBinding.getRoot());

        dialogAllergyBinding.textViewAllergyMessage.setText(allergyViewModel.getAllergyResponse().getValue().getAlleryInfo());

        dialogAllergy.show();
    }

    public static String getTAG() {
        return TAG;
    }
}
