package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.FrontBanner;
import com.harperskebab.network.NetworkOperations;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FrontBannerViewModel extends BaseViewModel {
    private static final String TAG = "FoodViewModel";

    private MutableLiveData<List<FrontBanner>> frontBanner = new MutableLiveData<>();

    public FrontBannerViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getFrontBanner(Context context,String branchId, String apiKey, String langCode, String moduleCall, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getFrontBanner(apiKey, langCode, moduleCall,branchId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rmFrontBannerResponse -> onSuccessFrontBanner(rmFrontBannerResponse.getFrontBanner(), nwCall), throwable -> onErrorFrontBanner(throwable, nwCall));

    }

    private void onSuccessFrontBanner(List<FrontBanner> frontBanners, NetworkOperations nwCall) {
        frontBanner.setValue(frontBanners);
        nwCall.onComplete();
    }

    private void onErrorFrontBanner(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<FrontBanner>> getFrontBanner() {
        return frontBanner;
    }

}
