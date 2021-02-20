package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmLocationResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocationViewModel extends BaseViewModel {
    private static final String TAG = "LocationViewModel";

    private MutableLiveData<RmLocationResponse> locationResponse = new MutableLiveData<>();


    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void checkLocation(Context context, String latitude, String longitude, String apiKey, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.checkLocation(latitude, longitude, apiKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((locationResponse) -> onSuccessFood(locationResponse, nwCall), throwable -> onErrorFood(throwable, nwCall));

    }

    private void onSuccessFood(RmLocationResponse locationResponse, NetworkOperations nwCall) {
        this.locationResponse.setValue(locationResponse);
        nwCall.onComplete();
    }

    private void onErrorFood(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmLocationResponse> getLocationResponse() {
        return locationResponse;
    }
}
