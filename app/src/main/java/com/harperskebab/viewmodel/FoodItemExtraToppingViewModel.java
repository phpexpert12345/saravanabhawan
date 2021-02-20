package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetFoodItemExtraToppingResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FoodItemExtraToppingViewModel extends BaseViewModel {
    private static final String TAG = "FoodItemExtraToppingVie";

    private MutableLiveData<List<FoodItemExtraTopping>> foodItemExtraTopping = new MutableLiveData<>();
    private MutableLiveData<RmGetFoodItemExtraToppingResponse>foodItemExtraToppingResponseMutableLiveData=new MutableLiveData<>();

    public FoodItemExtraToppingViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getFoodItemExtraTopping(Context context, String apiKey, String langCode, String foodID, String foodItemID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.foodItemExtraTopping.setValue(null);

        rfInterface.getFoodItemExtraTopping(apiKey, langCode, foodID, foodItemID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((foodItemExtraToppingResponse) -> onSuccessFood(foodItemExtraToppingResponse, nwCall), throwable -> onErrorFood(throwable, nwCall));

    }

    private void onSuccessFood(RmGetFoodItemExtraToppingResponse foodItemExtraToppingResponse, NetworkOperations nwCall) {

        if (foodItemExtraToppingResponse.getMenuItemExtraGroup().get(0).get(0).getError().equalsIgnoreCase("0")) {
            this.foodItemExtraToppingResponseMutableLiveData.setValue(foodItemExtraToppingResponse);
            this.foodItemExtraTopping.setValue(foodItemExtraToppingResponse.getMenuItemExtraGroup().get(0).get(0).getFoodItemExtraTopping());
        }

        nwCall.onComplete();
    }

    private void onErrorFood(Throwable throwable, NetworkOperations nwCall) {
        nwCall.onComplete();
    }

    public MutableLiveData<List<FoodItemExtraTopping>> getFoodItemExtraTopping() {
        return foodItemExtraTopping;
    }

    public MutableLiveData<RmGetFoodItemExtraToppingResponse> getFoodItemExtraToppingResponseMutableLiveData() {
        return foodItemExtraToppingResponseMutableLiveData;
    }
}
