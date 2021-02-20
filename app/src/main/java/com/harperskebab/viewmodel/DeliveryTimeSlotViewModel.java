package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.DeliveryTime;
import com.harperskebab.network.NetworkOperations;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeliveryTimeSlotViewModel extends BaseViewModel {
    private static final String TAG = "FoodItemViewModel";

    private MutableLiveData<List<DeliveryTime>> deliveryTimes = new MutableLiveData<>();

    public DeliveryTimeSlotViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getTimeSlot(Context context, String apiKey, String langCode, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getTimeSlot(apiKey, langCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((deliveryTimeSlotResponse) -> onSuccessTimeSlot(deliveryTimeSlotResponse.getDeliveryTime(), nwCall), throwable -> onErrorTimeSlot(throwable, nwCall));

    }

    private void onSuccessTimeSlot(List<DeliveryTime> deliveryTimes, NetworkOperations nwCall) {

        this.deliveryTimes.setValue(deliveryTimes);

        nwCall.onComplete();
    }

    private void onErrorTimeSlot(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<DeliveryTime>> getDeliveryTimes() {
        return deliveryTimes;
    }
}
