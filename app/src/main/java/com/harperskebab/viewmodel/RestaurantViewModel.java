package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.Restaurant;
import com.harperskebab.network.NetworkOperations;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends BaseViewModel {
    private static final String TAG = "RestaurantViewModel";

    private MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();


    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getRestrurentInformation(Context context, String apiKey, String langCode, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getRestrurentInformation(apiKey, langCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurant) -> onSuccessFood(restaurant, nwCall), throwable -> onErrorFood(throwable, nwCall));

    }

    private void onSuccessFood(Restaurant restaurant, NetworkOperations nwCall) {
        this.restaurant.setValue(restaurant);
        nwCall.onComplete();
    }

    private void onErrorFood(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<Restaurant> getRestaurant() {
        return restaurant;
    }
}
