package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.RestaurantDiscountCoupon;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetRestaurantOfferResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RestaurantOfferViewModel extends BaseViewModel {
    private static final String TAG = "RestaurantOfferViewMode";

    private MutableLiveData<List<RestaurantDiscountCoupon>> restaurantDiscountCoupon = new MutableLiveData<>();

    public RestaurantOfferViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getRestaurantOffers(Context context, String apiKey, String langCode, NetworkOperations nwCall,String branch_id) {

        nwCall.onStart(context, "");
        this.restaurantDiscountCoupon.setValue(null);

        rfInterface.getRestaurantOffers(apiKey, langCode,branch_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurantOfferResponse) -> onSuccessCancelOrder(restaurantOfferResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmGetRestaurantOfferResponse restaurantOfferResponse, NetworkOperations nwCall) {

        this.restaurantDiscountCoupon.setValue(restaurantOfferResponse.getRestaurantDiscountCoupon());

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        nwCall.onComplete();
    }

    public MutableLiveData<List<RestaurantDiscountCoupon>> getRestaurantDiscountCoupon() {
        return restaurantDiscountCoupon;
    }

}
