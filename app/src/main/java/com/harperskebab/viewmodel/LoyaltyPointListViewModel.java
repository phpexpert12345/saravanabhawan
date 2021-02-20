package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetLoyaltyPointListResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoyaltyPointListViewModel extends BaseViewModel {
    private static final String TAG = "LoyaltyPointListViewMod";

    private MutableLiveData<RmGetLoyaltyPointListResponse> loyaltyPointListResponse = new MutableLiveData<>();

    public LoyaltyPointListViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getTotalLoyaltyPoint(Context context, String api_key, String lang_code, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getLoyaltyPointList(api_key, lang_code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((loyaltyPointListResponse) -> onSuccessTotalLoyaltyPoint(loyaltyPointListResponse, nwCall), throwable -> onErrorTotalLoyaltyPoint(throwable, nwCall));

    }

    private void onSuccessTotalLoyaltyPoint(RmGetLoyaltyPointListResponse loyaltyPointListResponse, NetworkOperations nwCall) {
        this.loyaltyPointListResponse.setValue(loyaltyPointListResponse);
        nwCall.onComplete();
    }

    private void onErrorTotalLoyaltyPoint(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetLoyaltyPointListResponse> getLoyaltyPointListResponse() {
        return loyaltyPointListResponse;
    }
}
