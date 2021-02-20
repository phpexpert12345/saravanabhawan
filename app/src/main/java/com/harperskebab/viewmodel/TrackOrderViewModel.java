package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmTrackOrderResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TrackOrderViewModel extends BaseViewModel {
    private static final String TAG = "FoodItemViewModel";

    private MutableLiveData<RmTrackOrderResponse> trackOrderResponse = new MutableLiveData<>();

    public TrackOrderViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void trackOrder(Context context, String apiKey, String langCode, String orderID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        this.trackOrderResponse.setValue(null);

        rfInterface.trackOrder(apiKey, langCode, orderID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((trackOrderResponse) -> onSuccessTrackOrder(trackOrderResponse, nwCall), throwable -> onErrorTrackOrder(throwable, nwCall));

    }

    private void onSuccessTrackOrder(RmTrackOrderResponse trackOrderResponse, NetworkOperations nwCall) {

        this.trackOrderResponse.setValue(trackOrderResponse);

        nwCall.onComplete();
    }

    private void onErrorTrackOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmTrackOrderResponse> getTrackOrderResponse() {
        return trackOrderResponse;
    }
}
