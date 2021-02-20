package com.harperskebab.viewmodel.paylater;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.Order;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.viewmodel.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayLaterOrderViewModel extends BaseViewModel {
    private static final String TAG = "OrderViewModel";

    private MutableLiveData<List<Order>> orderViewResult = new MutableLiveData<>();

    public PayLaterOrderViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getPayLaterOrderList(Context context, String apiKey, String langCode, String userID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.orderViewResult.setValue(null);

        rfInterface.getPayLaterOrderList(apiKey, langCode, userID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((orderListResponse) -> onSuccessOrderList(orderListResponse.getOrders().getOrder(), nwCall), throwable -> onErrorOrderList(throwable, nwCall));

    }

    private void onSuccessOrderList(List<Order> orders, NetworkOperations nwCall) {

        this.orderViewResult.setValue(orders);

        nwCall.onComplete();
    }

    private void onErrorOrderList(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<Order>> getOrderViewResult() {
        return orderViewResult;
    }

}
