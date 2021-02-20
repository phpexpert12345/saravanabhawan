package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.FoodItem;
import com.harperskebab.network.NetworkOperations;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FoodItemViewModel extends BaseViewModel {
    private static final String TAG = "FoodItemViewModel";

    private MutableLiveData<List<FoodItem>> foodItems = new MutableLiveData<>();

    public FoodItemViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getFoodItem(Context context, String apiKey, String langCode, String foodID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getFoodItem(apiKey, langCode, foodID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((rmGetFoodItemResponse) -> onSuccessFood(rmGetFoodItemResponse.getFoodItem(), nwCall), throwable -> onErrorFood(throwable, nwCall));

    }

    private void onSuccessFood(List<FoodItem> foodItems, NetworkOperations nwCall) {

        this.foodItems.setValue(foodItems);

        nwCall.onComplete();
    }

    private void onErrorFood(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<FoodItem>> getFoodItems() {
        return foodItems;
    }
}
