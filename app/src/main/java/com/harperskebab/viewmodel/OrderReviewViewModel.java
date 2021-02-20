package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmUpdateReviewResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderReviewViewModel extends BaseViewModel {
    private static final String TAG = "OrderCancelViewModel";

    private MutableLiveData<RmUpdateReviewResponse> updateReviewResponse = new MutableLiveData<>();

    public OrderReviewViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void updateReview(Context context, String apiKey, String langCode, String orderID, String userID, String restaurantReviewRating, String qualityRating, String serviceRating, String timeRating, String restaurantReviewComment, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        this.updateReviewResponse.setValue(null);

        rfInterface.updateReview(apiKey, langCode, orderID, userID, restaurantReviewRating, qualityRating, serviceRating, timeRating, restaurantReviewComment).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((updateReviewResponse) -> onSuccessCancelOrder(updateReviewResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmUpdateReviewResponse orderCancelResponse, NetworkOperations nwCall) {

        this.updateReviewResponse.setValue(orderCancelResponse);

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmUpdateReviewResponse> getUpdateReviewResponse() {
        return updateReviewResponse;
    }
}
