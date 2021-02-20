package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetOrderDetailResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetOrderListResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailViewModel extends BaseViewModel {
    private static final String TAG = "OrderDetailViewModel";
    private MutableLiveData<RmGetOrderDetailResponse> orderDetailResponse = new MutableLiveData<>();

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getOrderDetail(Context context, String apiKey, String langCode, String orderID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.orderDetailResponse.setValue(null);
        String url="https://dmd.foodsdemo.com/restaurantAPI/phpexpert_Order_DetailsDisplay.php?api_key="+apiKey+"&lang_code="+langCode+"&order_identifyno="+orderID;
        Log.i("log",url);

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
