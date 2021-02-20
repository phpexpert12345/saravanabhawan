package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.SplashBanners;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetSplashResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends BaseViewModel {
    private static final String TAG = "SplashViewModel";

    private MutableLiveData<List<SplashBanners>> splashBanners = new MutableLiveData<>();


    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getSplash(Context context, String apiKey, String langCode, String splashType, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getSplash(apiKey, langCode, splashType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((getSplashResponse) -> onSuccessFood(getSplashResponse, nwCall), throwable -> onErrorFood(throwable, nwCall));

    }

    private void onSuccessFood(RmGetSplashResponse splashResponse, NetworkOperations nwCall) {
        this.splashBanners.setValue(splashResponse.getSplashBanners());
        nwCall.onComplete();
    }

    private void onErrorFood(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<SplashBanners>> getSplashBanners() {
        return splashBanners;
    }
}
