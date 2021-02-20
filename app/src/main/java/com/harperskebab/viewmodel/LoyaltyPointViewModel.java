package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmVerifyLoyaltyPointResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoyaltyPointViewModel extends BaseViewModel {
    private static final String TAG = "LoyaltyPointViewModel";

    private MutableLiveData<RmVerifyLoyaltyPointResponse> loyalityPointResponse = new MutableLiveData<>();
    private MutableLiveData<String> totalLoyaltyPoint = new MutableLiveData<>();

    public LoyaltyPointViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getTotalLoyaltyPoint(Context context, String userid, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getTotalLoyaltyPoint(userid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((getLoyaltyPointResponse) -> onSuccessTotalLoyaltyPoint(getLoyaltyPointResponse.getTotalLoyaltyPoints(), nwCall), throwable -> onErrorTotalLoyaltyPoint(throwable, nwCall));

    }

    private void onSuccessTotalLoyaltyPoint(String totalLoyaltyPoint, NetworkOperations nwCall) {
        this.totalLoyaltyPoint.setValue(totalLoyaltyPoint);
        nwCall.onComplete();
    }

    private void onErrorTotalLoyaltyPoint(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<String> getTotalLoyaltyPoint() {
        return totalLoyaltyPoint;
    }

    @SuppressLint("CheckResult")
    public void verifyCouponCode(Context context, String userid, String loyalityPoint, String subTotal, NetworkOperations nwCall) {

        loyalityPointResponse.setValue(null);

        nwCall.onStart(context, "");

        rfInterface.verifyLoyaltyPoint(userid, loyalityPoint, subTotal).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((verifyLoyaltyPointResponse) -> onSuccessLoyaltyPoint(verifyLoyaltyPointResponse, nwCall), throwable -> onErrorLoyaltyPoint(throwable, nwCall));

    }

    private void onSuccessLoyaltyPoint(RmVerifyLoyaltyPointResponse verifyLoyaltyPointResponse, NetworkOperations nwCall) {
        this.loyalityPointResponse.setValue(verifyLoyaltyPointResponse);
        nwCall.onComplete();
    }

    private void onErrorLoyaltyPoint(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmVerifyLoyaltyPointResponse> getLoyaltyPointResponse() {
        return loyalityPointResponse;
    }
}
