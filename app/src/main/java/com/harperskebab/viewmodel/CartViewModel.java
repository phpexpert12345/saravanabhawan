package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harperskebab.model.Deliveryaddress;
import com.harperskebab.model.Food;
import com.harperskebab.model.RestaurantBranch;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetAddressResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetCardsResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetServiceChargeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmOrderSendToKitchenResponse;
import com.harperskebab.network.retrofit.responsemodels.RmPlaceOrderResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.SharedPrefrenceObj;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends BaseViewModel {
    private static final String TAG = "CartViewModel";

    private MutableLiveData<String> orderType = new MutableLiveData<>();
    private MutableLiveData<String> postalCode = new MutableLiveData<>();
    private MutableLiveData<String> selectedTableID = new MutableLiveData<>();
    private MutableLiveData<String> numberOfPeople = new MutableLiveData<>();
    private MutableLiveData<String> couponCode = new MutableLiveData<>();
    private MutableLiveData<String> couponCodeDiscount = new MutableLiveData<>();
    private MutableLiveData<String> loyaltyCodeDiscount = new MutableLiveData<>();
    private MutableLiveData<String> restaurantDiscount = new MutableLiveData<>();
    private MutableLiveData<RmGetServiceChargeResponse> serviceCharge = new MutableLiveData<>();
    private MutableLiveData<String> foodTax = new MutableLiveData<>();
    private MutableLiveData<String> drinkTax = new MutableLiveData<>();
    private MutableLiveData<String> riderTip = new MutableLiveData<>();
    private MutableLiveData<String> subTotal = new MutableLiveData<>();
    private MutableLiveData<String> toPay = new MutableLiveData<>();
    private MutableLiveData<RestaurantBranch> selectedRestaurantBranch = new MutableLiveData<>();
    private MutableLiveData<Deliveryaddress> selectedDeliveryAddress = new MutableLiveData<>();

    private MutableLiveData<List<Food>> foods = new MutableLiveData<>();

    private MutableLiveData<RmOrderSendToKitchenResponse> orderSendToKitchenResponse = new MutableLiveData<>();
    private MutableLiveData<RmPlaceOrderResponse> placeOrderResponse = new MutableLiveData<>();
    private MutableLiveData<RmGetCardsResponse> cardListResponse = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);

        orderType.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.ORDER_TYPE));
        postalCode.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.POSTAL_CODE));
        selectedTableID.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.TABLE_ID));
        couponCode.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.COUPON_CODE));
        couponCodeDiscount.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.COUPON_CODE_DISCOUNT));
        loyaltyCodeDiscount.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.LOYALTY_CODE_DISCOUNT));
        restaurantDiscount.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.RESTAURANT_DISCOUNT));
        foodTax.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.FOOD_TAX));
        drinkTax.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.DRINK_TAX));
        riderTip.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.RIDER_TIP));
        subTotal.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.SUB_TOTAL));
        toPay.setValue(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.TO_PAY));
        selectedRestaurantBranch.setValue(new Gson().fromJson(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.SELECTED_RESTAURANT_BRANCH), RestaurantBranch.class));
        selectedDeliveryAddress.setValue(new Gson().fromJson(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.SELECTED_DELIVERY_ADDRESS), Deliveryaddress.class));
        orderSendToKitchenResponse.setValue(new Gson().fromJson(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.ORDER_SEND_TO_KITCHEN_RESPONSE), RmOrderSendToKitchenResponse.class));

        Type type = new TypeToken<List<Food>>() {
        }.getType();

        foods.setValue(new Gson().fromJson(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.ORDERED_FOOD), type));

    }

    public MutableLiveData<String> getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType.setValue(orderType);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.ORDER_TYPE, orderType);
    }

    public MutableLiveData<String> getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.setValue(postalCode);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.POSTAL_CODE, postalCode);
    }

    public MutableLiveData<String> getSelectedTableID() {
        return selectedTableID;
    }

    public void setSelectedTableID(String selectedTableID) {
        this.selectedTableID.setValue(selectedTableID);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.TABLE_ID, selectedTableID);
    }

    public MutableLiveData<String> getNumberOfPeople() {
        return numberOfPeople;
    }

    public MutableLiveData<String> getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode.setValue(couponCode);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.COUPON_CODE, couponCode);
    }

    public MutableLiveData<String> getCouponCodeDiscount() {
        return couponCodeDiscount;
    }

    public void setCouponCodeDiscount(String couponCodeDiscount) {
        this.couponCodeDiscount.setValue(couponCodeDiscount);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.COUPON_CODE_DISCOUNT, couponCodeDiscount);
    }

    public MutableLiveData<String> getLoyaltyCodeDiscount() {
        return loyaltyCodeDiscount;
    }

    public void setLoyaltyCodeDiscount(String loyaltyCodeDiscount) {
        this.loyaltyCodeDiscount.setValue(loyaltyCodeDiscount);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.LOYALTY_CODE_DISCOUNT, loyaltyCodeDiscount);
    }

    public MutableLiveData<String> getRestaurantDiscount() {
        return restaurantDiscount;
    }

    public void setRestaurantDiscount(String restaurantDiscount) {
        this.restaurantDiscount.setValue(restaurantDiscount);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.RESTAURANT_DISCOUNT, restaurantDiscount);
    }

    public MutableLiveData<RmGetServiceChargeResponse> getServiceCharge() {
        return serviceCharge;
    }

    public MutableLiveData<String> getFoodTax() {
        return foodTax;
    }

    public void setFoodTax(String foodTax) {
        this.foodTax.setValue(foodTax);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.FOOD_TAX, foodTax);
    }

    public MutableLiveData<String> getDrinkTax() {
        return drinkTax;
    }

    public void setDrinkTax(String drinkTax) {
        this.drinkTax.setValue(drinkTax);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.DRINK_TAX, drinkTax);
    }

    public MutableLiveData<String> getRiderTip() {
        return riderTip;
    }

    public void setRiderTip(String riderTip) {
        this.riderTip.setValue(riderTip);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.RIDER_TIP, riderTip);
    }

    public MutableLiveData<String> getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal.setValue(subTotal);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.SUB_TOTAL, subTotal);
    }


    public MutableLiveData<String> getToPay() {
        return toPay;
    }

    public void setToPay(String toPay) {
        this.toPay.setValue(toPay);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.TO_PAY, toPay);
    }

    public MutableLiveData<RestaurantBranch> getSelectedRestaurantBranch() {
        return selectedRestaurantBranch;
    }

    public void setSelectedRestaurantBranch(RestaurantBranch selectedRestaurantBranch) {
        this.selectedRestaurantBranch.setValue(selectedRestaurantBranch);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.SELECTED_RESTAURANT_BRANCH, new Gson().toJson(selectedRestaurantBranch));
    }

    public MutableLiveData<Deliveryaddress> getSelectedDeliveryAddress() {
        return selectedDeliveryAddress;
    }

    public void setSelectedDeliveryAddress(Deliveryaddress selectedDeliveryAddress) {
        this.selectedDeliveryAddress.setValue(selectedDeliveryAddress);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.SELECTED_DELIVERY_ADDRESS, new Gson().toJson(selectedDeliveryAddress));
    }


    public MutableLiveData<RmPlaceOrderResponse> getPlaceOrderResponse() {
        return placeOrderResponse;
    }

    @SuppressLint("CheckResult")
    public void getRestaurentDiscountPrice(Context context,String branchId, String apiKey, String langCode, String subTotal, String orderType, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getRestaurentDiscountPrice(branchId,apiKey, langCode, subTotal, orderType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurantDiscountResponse) -> onSuccessRestaurentDiscountPrice(restaurantDiscountResponse.getDiscountOfferPrice(), nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessRestaurentDiscountPrice(String discountOfferPrice, NetworkOperations nwCall) {
        this.restaurantDiscount.setValue(discountOfferPrice);
        nwCall.onComplete();
    }

    private void onError(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    @SuppressLint("CheckResult")
    public void getServiceCharge(Context context,String branchId, String apiKey, String langCode, String subTotal, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getServiceCharge(branchId,apiKey, langCode, subTotal).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((serviceChargeResponse) -> onSuccessServiceCharge(serviceChargeResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessServiceCharge(RmGetServiceChargeResponse serviceChargeResponse, NetworkOperations nwCall) {
        this.serviceCharge.setValue(serviceChargeResponse);
        nwCall.onComplete();
    }


    @SuppressLint("CheckResult")
    public void CardList(Context context,String branchId, String apiKey, String langCode, String customerId, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getCardList(branchId,apiKey, langCode, customerId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((cardListResponse) -> onSuccessCardList(cardListResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessCardList(RmGetCardsResponse cardListResponse, NetworkOperations nwCall) {
        this.cardListResponse.setValue(cardListResponse);
        nwCall.onComplete();
    }

    @SuppressLint("CheckResult")
    public void placeOrder(Context context,String branchId,String saveCard,String cardType,String cardNumber,String cardExpMonth,String cardExpYear, String apiKey, String langCode, String payment_transaction_paypal, String mealID, String mealquqntity, String mealPrice, String itemId,
                           String Quantity, String Price, String strsizeid, String extraItemID, String CustomerId, String CustomerAddressId, String payment_type, String order_price,
                           String subTotalAmount, String delivery_date, String delivery_time, String instructions, String deliveryCharge, String CouponCode, String CouponCodePrice,
                           String couponCodeType, String SalesTaxAmount, String order_type, String SpecialInstruction, String extraTipAddAmount, String RestaurantNameEstimate,
                           String discountOfferDescription, String discountOfferPrice, String RestaurantoffrType, String ServiceFees, String PaymentProcessingFees,
                           String deliveryChargeValueType, String ServiceFeesType, String PackageFeesType, String PackageFees, String WebsiteCodePrice, String WebsiteCodeType,
                           String WebsiteCodeNo, String preorderTime, String VatTax, String GiftCardPay, String WalletPay, String loyptamount, String table_number_assign,
                           String foodTax, String drinkTax, String customer_country, String group_member_id, String numberOfPeople,String foodCost,String extraItemIdOne,String extraItemPrice,String extraItemName,
                           NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        rfInterface.placeOrder(convertBase64(extraItemName),extraItemIdOne,extraItemPrice,branchId,saveCard,cardType,cardNumber,cardExpMonth,cardExpYear,apiKey, langCode, payment_transaction_paypal, mealID, mealquqntity, mealPrice, itemId, Quantity, Price, strsizeid, extraItemID, CustomerId,
                CustomerAddressId, payment_type, order_price, subTotalAmount, delivery_date, delivery_time, instructions, deliveryCharge, CouponCode, CouponCodePrice,
                couponCodeType, SalesTaxAmount, order_type, SpecialInstruction, extraTipAddAmount, RestaurantNameEstimate, discountOfferDescription, discountOfferPrice,
                RestaurantoffrType, ServiceFees, PaymentProcessingFees, deliveryChargeValueType, ServiceFeesType, PackageFeesType, PackageFees, WebsiteCodePrice,
                WebsiteCodeType, WebsiteCodeNo, preorderTime, VatTax, GiftCardPay, WalletPay, loyptamount, table_number_assign, foodTax, drinkTax, customer_country,
                group_member_id, numberOfPeople,foodCost).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((placeOrderResponse) -> onSuccessPlaceOrder(placeOrderResponse, nwCall), throwable -> onError(throwable, nwCall));

    }
    private String convertBase64(String str) {
        byte[] data;
        String base64="";

        try {

            data = str.getBytes("UTF-8");

            base64 = Base64.encodeToString(data, Base64.NO_WRAP);

            Log.i("Base 64 ", base64);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }
        return base64;
    }
    private void onSuccessPlaceOrder(RmPlaceOrderResponse placeOrderResponse, NetworkOperations nwCall) {
        this.placeOrderResponse.setValue(placeOrderResponse);
        nwCall.onComplete();
    }

    @SuppressLint("CheckResult")
    public void orderSendToKitchen(Context context,String branchId, String mealID, String mealquqntity, String mealPrice, String itemId, String Quantity, String Price, String strsizeid,
                                   String extraItemID, String order_identifyno, String CustomerId, String order_price, String subTotalAmount, String deliveryCharge,
                                   String CouponCode, String CouponCodePrice, String couponCodeType, String SalesTaxAmount, String extraTipAddAmount,
                                   String discountOfferDescription, String discountOfferPrice, String RestaurantoffrType, String ServiceFees, String PaymentProcessingFees,
                                   String ServiceFeesType, String PackageFeesType, String PackageFees, String VatTax, String GiftCardPay, String WalletPay, String loyptamount,
                                   String loyltPnts, String table_number_assign, String branch_id, String FoodCosts, String getTotalItemDiscount, String getFoodTaxTotal7,
                                   String getFoodTaxTotal19, String TotalSavedDiscount, String discountOfferFreeItems, String numberOfPeople,
                                   NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.orderSendToKitchenResponse.setValue(null);

        Observable<RmOrderSendToKitchenResponse> orderSendToKitchenResponseObservable;

        if (order_identifyno == null) {

            orderSendToKitchenResponseObservable = rfInterface.addItemToKitchen(branchId,mealID, mealquqntity, mealPrice, itemId, Quantity, Price, strsizeid, extraItemID, order_identifyno,
                    CustomerId, order_price, subTotalAmount, deliveryCharge, CouponCode, CouponCodePrice, couponCodeType, SalesTaxAmount, extraTipAddAmount, discountOfferDescription,
                    discountOfferPrice, RestaurantoffrType, ServiceFees, PaymentProcessingFees, ServiceFeesType, PackageFeesType, PackageFees, VatTax, GiftCardPay, WalletPay, loyptamount,
                    loyltPnts, table_number_assign, branch_id, FoodCosts, getTotalItemDiscount, getFoodTaxTotal7, getFoodTaxTotal19, TotalSavedDiscount, discountOfferFreeItems, numberOfPeople
            );

        } else {

            orderSendToKitchenResponseObservable = rfInterface.orderSendToKitchen(branchId,mealID, mealquqntity, mealPrice, itemId, Quantity, Price, strsizeid, extraItemID, CustomerId,
                    order_price, subTotalAmount, deliveryCharge, CouponCode, CouponCodePrice, couponCodeType, SalesTaxAmount, extraTipAddAmount, discountOfferDescription,
                    discountOfferPrice, RestaurantoffrType, ServiceFees, PaymentProcessingFees, ServiceFeesType, PackageFeesType, PackageFees, VatTax, GiftCardPay, WalletPay,
                    loyptamount, loyltPnts, table_number_assign, branch_id, FoodCosts, getTotalItemDiscount, getFoodTaxTotal7, getFoodTaxTotal19, TotalSavedDiscount,
                    discountOfferFreeItems, numberOfPeople
            );

        }

        orderSendToKitchenResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((placeOrderResponse) -> onSuccessOrderSendToKitchen(placeOrderResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessOrderSendToKitchen(RmOrderSendToKitchenResponse orderSendToKitchenResponse, NetworkOperations nwCall) {

        if (orderSendToKitchenResponse.getSuccess().equals(1L)) {
            SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.EAT_IN_ORDER_ID, orderSendToKitchenResponse.getOrderIdentifyno());
        }
        this.orderSendToKitchenResponse.setValue(orderSendToKitchenResponse);

        nwCall.onComplete();
    }

    public MutableLiveData<RmOrderSendToKitchenResponse> getOrderSendToKitchenResponse() {
        return orderSendToKitchenResponse;
    }
    public MutableLiveData<RmGetCardsResponse> getGetCardListResponse() {
        return cardListResponse;
    }

    public void setOrderSendToKitchenResponse(RmOrderSendToKitchenResponse orderSendToKitchenResponse) {
        this.orderSendToKitchenResponse.setValue(orderSendToKitchenResponse);
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.ORDER_SEND_TO_KITCHEN_RESPONSE, new Gson().toJson(orderSendToKitchenResponse));
    }

    public MutableLiveData<List<Food>> getFoods() {
        return foods;
    }

    public void setFoods(Set<Food> foods) {

        if (foods != null) {
            List<Food> tmpFoods = new ArrayList<>();

            tmpFoods.addAll(foods);

            if (this.foods.getValue() == null) {
                this.foods.setValue(new ArrayList<Food>());
            }

            this.foods.getValue().addAll(tmpFoods);
        } else {
            this.foods.setValue(null);
        }

        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.ORDERED_FOOD, new Gson().toJson(this.foods.getValue()));
    }
}
