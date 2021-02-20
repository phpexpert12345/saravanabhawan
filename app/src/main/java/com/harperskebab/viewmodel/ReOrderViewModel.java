package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetReOrderResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReOrderViewModel extends BaseViewModel {
    private static final String TAG = "ReOrderViewModel";
    private MutableLiveData<RmGetReOrderResponse> orderDetailResponse = new MutableLiveData<>();

    public ReOrderViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getReorder(Context context, String apiKey, String langCode, String orderID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.orderDetailResponse.setValue(null);

        rfInterface.getReOrder(apiKey, langCode, orderID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((orderDetailResponse) -> onSuccessReOrder(orderDetailResponse, nwCall), throwable -> onErrorOrderDetail(throwable, nwCall));

    }

    private void onSuccessReOrder(RmGetReOrderResponse orderDetailResponse, NetworkOperations nwCall) {

        this.orderDetailResponse.setValue(orderDetailResponse);

        nwCall.onComplete();
    }

    private void onErrorOrderDetail(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetReOrderResponse> getOrderDetailResponse() {
        return orderDetailResponse;
    }
}
