package com.harperskebab.viewmodel.paylater;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetServiceChargeResponse;
import com.harperskebab.network.retrofit.responsemodels.RmPayLaterPlaceOrderResponse;
import com.harperskebab.viewmodel.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayLaterCartViewModel extends BaseViewModel {
    private static final String TAG = "PayLaterCartViewModel";

    private MutableLiveData<RmGetServiceChargeResponse> serviceCharge = new MutableLiveData<>();
    private MutableLiveData<String> foodTax = new MutableLiveData<>();
    private MutableLiveData<String> drinkTax = new MutableLiveData<>();
    private MutableLiveData<String> subTotal = new MutableLiveData<>();
    private MutableLiveData<String> toPay = new MutableLiveData<>();

    private MutableLiveData<RmPayLaterPlaceOrderResponse> placeOrderResponse = new MutableLiveData<>();

    public PayLaterCartViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RmGetServiceChargeResponse> getServiceCharge() {
        return serviceCharge;
    }

    public MutableLiveData<String> getFoodTax() {
        return foodTax;
    }

    public MutableLiveData<String> getDrinkTax() {
        return drinkTax;
    }

    public MutableLiveData<String> getSubTotal() {
        return subTotal;
    }

    public MutableLiveData<String> getToPay() {
        return toPay;
    }

    @SuppressLint("CheckResult")
    public void getServiceCharge(Context context,String branchId, String apiKey, String langCode, String subTotal, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.serviceCharge.setValue(null);

        rfInterface.getServiceCharge(branchId,apiKey, langCode, subTotal).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((serviceChargeResponse) -> onSuccessServiceCharge(serviceChargeResponse, nwCall), throwable -> onErrorServiceCharge(throwable, nwCall));

    }

    private void onSuccessServiceCharge(RmGetServiceChargeResponse serviceChargeResponse, NetworkOperations nwCall) {
        this.serviceCharge.setValue(serviceChargeResponse);
        nwCall.onComplete();
    }

    private void onErrorServiceCharge(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    @SuppressLint("CheckResult")
    public void placeOrder(Context context, String apiKey, String langCode, String payment_transaction_paypal, String mealID, String mealquqntity, String mealPrice, String itemId,
                           String Quantity, String Price, String strsizeid, String extraItemID, String CustomerId, String CustomerAddressId, String payment_type, String order_price,
                           String subTotalAmount, String delivery_date, String delivery_time, String instructions, String deliveryCharge, String CouponCode, String CouponCodePrice,
                           String couponCodeType, String SalesTaxAmount, String order_type, String SpecialInstruction, String extraTipAddAmount, String RestaurantNameEstimate,
                           String discountOfferDescription, String discountOfferPrice, String RestaurantoffrType, String ServiceFees, String PaymentProcessingFees,
                           String deliveryChargeValueType, String ServiceFeesType, String PackageFeesType, String PackageFees, String WebsiteCodePrice, String WebsiteCodeType,
                           String WebsiteCodeNo, String preorderTime, String VatTax, String GiftCardPay, String WalletPay, String loyptamount, String table_number_assign,
                           String customer_country, String group_member_id, String loyltPnts, String branch_id, String FoodCosts, String getTotalItemDiscount,
                           String getFoodTaxTotal7, String getFoodTaxTotal19, String TotalSavedDiscount, String discountOfferFreeItems, String order_identifyno,
                           NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.placeOrderResponse.setValue(null);

        rfInterface.payLaterPlaceOrder(apiKey, langCode, payment_transaction_paypal, mealID, mealquqntity, mealPrice, itemId, Quantity, Price, strsizeid, extraItemID, CustomerId,
                CustomerAddressId, payment_type, order_price, subTotalAmount, delivery_date, delivery_time, instructions, deliveryCharge, CouponCode, CouponCodePrice, couponCodeType,
                SalesTaxAmount, order_type, SpecialInstruction, extraTipAddAmount, RestaurantNameEstimate, discountOfferDescription, discountOfferPrice, RestaurantoffrType,
                ServiceFees, PaymentProcessingFees, deliveryChargeValueType, ServiceFeesType, PackageFeesType, PackageFees, WebsiteCodePrice, WebsiteCodeType, WebsiteCodeNo,
                preorderTime, VatTax, GiftCardPay, WalletPay, loyptamount, table_number_assign, customer_country, group_member_id, loyltPnts, branch_id, FoodCosts,
                getTotalItemDiscount, getFoodTaxTotal7, getFoodTaxTotal19, TotalSavedDiscount, discountOfferFreeItems, order_identifyno).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((placeOrderResponse) -> onSuccessPlaceOrder(placeOrderResponse, nwCall), throwable -> onErrorPlaceOrder(throwable, nwCall));

    }

    @SuppressLint("CheckResult")
    public void placeOrder(Context context, String apiKey, String langCode, String order_identifyno, String payment_transaction_paypal, String CustomerId, String payment_type, String order_price,
                           NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.placeOrderResponse.setValue(null);

        rfInterface.payLaterPlaceOrder(apiKey, langCode, order_identifyno, payment_transaction_paypal, CustomerId, payment_type, order_price).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((placeOrderResponse) -> onSuccessPlaceOrder(placeOrderResponse, nwCall), throwable -> onErrorPlaceOrder(throwable, nwCall));

    }

    private void onSuccessPlaceOrder(RmPayLaterPlaceOrderResponse placeOrderResponse, NetworkOperations nwCall) {
        this.placeOrderResponse.setValue(placeOrderResponse);
        nwCall.onComplete();
    }

    private void onErrorPlaceOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmPayLaterPlaceOrderResponse> getPlaceOrderResponse() {
        return placeOrderResponse;
    }
}
