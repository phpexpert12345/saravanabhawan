package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmVerifyCouponCodeResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CouponViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<RmVerifyCouponCodeResponse> couponCodeResponse = new MutableLiveData<>();


    public CouponViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void verifyCouponCode(Context context,String branchId, String apiKey, String langCode, String subTotal, String couponCode, NetworkOperations nwCall) {

        couponCodeResponse.setValue(null);

        nwCall.onStart(context, "");

        rfInterface.verifyCouponCode(branchId,apiKey, langCode, subTotal, couponCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((verifyCouponCodeResponse) -> onSuccessCouponCode(verifyCouponCodeResponse, nwCall), throwable -> onErrorCouponCode(throwable, nwCall));

    }

    private void onSuccessCouponCode(RmVerifyCouponCodeResponse couponCodeResponse, NetworkOperations nwCall) {
        this.couponCodeResponse.setValue(couponCodeResponse);
        nwCall.onComplete();
    }

    private void onErrorCouponCode(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmVerifyCouponCodeResponse> getCouponCodeResponse() {
        return couponCodeResponse;
    }
}
