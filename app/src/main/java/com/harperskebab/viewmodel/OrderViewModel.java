package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetOrderDetailResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetOrderListResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends BaseViewModel {
    private static final String TAG = "OrderViewModel";

    private MutableLiveData<RmGetOrderListResponse> orderListResponse = new MutableLiveData<>();
    private MutableLiveData<RmGetOrderDetailResponse> orderDetailResponse = new MutableLiveData<>();

    public OrderViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getOrderList(Context context, String apiKey, String langCode, String userID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");


        rfInterface.getOrderList(apiKey, langCode, userID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((orderListResponse) -> onSuccessOrderList(orderListResponse, nwCall), throwable -> onErrorOrderList(throwable, nwCall));

    }

    private void onSuccessOrderList(RmGetOrderListResponse orderListResponses, NetworkOperations nwCall) {

        this.orderListResponse.setValue(orderListResponses);

        nwCall.onComplete();
    }

    private void onErrorOrderList(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetOrderListResponse> getOrderListResponse() {
        return orderListResponse;
    }

    @SuppressLint("CheckResult")
    public void getOrderDetail(Context context, String apiKey, String langCode, String orderID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.orderDetailResponse.setValue(null);

        rfInterface.getOrderDetail(apiKey, langCode, orderID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((orderDetailResponse) -> onSuccessOrderDetail(orderDetailResponse, nwCall), throwable -> onErrorOrderDetail(throwable, nwCall));

    }

    private void onSuccessOrderDetail(RmGetOrderDetailResponse orderDetailResponse, NetworkOperations nwCall) {

        this.orderDetailResponse.setValue(orderDetailResponse);

        nwCall.onComplete();
    }

    private void onErrorOrderDetail(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetOrderDetailResponse> getOrderDetailResponse() {
        return orderDetailResponse;
    }
}
