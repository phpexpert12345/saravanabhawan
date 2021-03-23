package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmLanguageListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmLanguageResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LanguageViewModel extends BaseViewModel {
    private static final String TAG = "LocationViewModel";

    private MutableLiveData<RmLanguageResponse> languageResponse = new MutableLiveData<>();
    private MutableLiveData<RmLanguageListResponse> languageListResponse = new MutableLiveData<>();


    public LanguageViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getLanguage(Context context, String apiKey, String languageCode, NetworkOperations nwCall) {

//        nwCall.onStart(context, "");

        this.languageResponse.setValue(null);

        rfInterface.getLanguage(apiKey, languageCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((languageResponse) -> onSuccessLanguage(languageResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessLanguage(RmLanguageResponse languageResponse, NetworkOperations nwCall) {
        this.languageResponse.setValue(languageResponse);
//        nwCall.onComplete();
    }

    private void onError(Throwable throwable, NetworkOperations nwCall) {
        //todo
//        nwCall.onComplete();
    }

    public MutableLiveData<RmLanguageResponse> getLanguageResponse() {
        return languageResponse;
    }

    @SuppressLint("CheckResult")
    public void getLanguageList(Context context, String apiKey, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getLanguageList(apiKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((languageListResponse) -> onSuccessLanguageList(languageListResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessLanguageList(RmLanguageListResponse languageListResponse, NetworkOperations nwCall) {
        this.languageListResponse.setValue(languageListResponse);
        nwCall.onComplete();
    }

    public MutableLiveData<RmLanguageListResponse> getLanguageListResponse() {
        return languageListResponse;
    }
}
