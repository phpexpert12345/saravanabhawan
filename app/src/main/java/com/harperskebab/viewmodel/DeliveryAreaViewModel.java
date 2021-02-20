package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.DeliveryArea;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetDeliveryAreaResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeliveryAreaViewModel extends BaseViewModel {
    private static final String TAG = "OpeningHourViewModel";

    private MutableLiveData<List<DeliveryArea>> deliveryAreas = new MutableLiveData<>();

    public DeliveryAreaViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getDeliveryArea(Context context, String apiKey, String langCode, String splashType,String branch_id, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        this.deliveryAreas.setValue(null);

        rfInterface.getDeliveryArea(apiKey, langCode, splashType,branch_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((deliveryAreaResponse) -> onSuccessCancelOrder(deliveryAreaResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmGetDeliveryAreaResponse deliveryAreaResponse, NetworkOperations nwCall) {

        this.deliveryAreas.setValue(deliveryAreaResponse.getDeliveryArea());

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<DeliveryArea>> getDeliveryAreas() {
        return deliveryAreas;
    }
}
