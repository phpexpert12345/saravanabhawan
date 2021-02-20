package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmOrderCancelResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderCancelViewModel extends BaseViewModel {
    private static final String TAG = "OrderCancelViewModel";

    private MutableLiveData<RmOrderCancelResponse> orderCancelResponse = new MutableLiveData<>();

    public OrderCancelViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void cancelOrder(Context context, String apiKey, String langCode, String orderID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        this.orderCancelResponse.setValue(null);

        rfInterface.cancelOrder(apiKey, langCode, orderID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((orderCancelResponse) -> onSuccessCancelOrder(orderCancelResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmOrderCancelResponse orderCancelResponse, NetworkOperations nwCall) {

        this.orderCancelResponse.setValue(orderCancelResponse);

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmOrderCancelResponse> getOrderCancelResponse() {
        return orderCancelResponse;
    }
}
