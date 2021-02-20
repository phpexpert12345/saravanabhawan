package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.RestaurantReview;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetRestaurantReviewResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RestaurantReviewViewModel extends BaseViewModel {
    private static final String TAG = "ReviewViewModel";

    private MutableLiveData<List<RestaurantReview>> reviews = new MutableLiveData<>();

    public RestaurantReviewViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getReviews(Context context, String apiKey, String langCode, NetworkOperations nwCall,String branch_id) {

        nwCall.onStart(context, "");
        this.reviews.setValue(null);

        rfInterface.getRestaurantReview(apiKey, langCode,branch_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((reviewsResponse) -> onSuccessCancelOrder(reviewsResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmGetRestaurantReviewResponse reviewsResponse, NetworkOperations nwCall) {

        if (reviewsResponse.getSuccess().equals(0l)) {
            this.reviews.setValue(reviewsResponse.getReview().getRestaurantReviews());
        }

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<RestaurantReview>> getReviews() {
        return reviews;
    }
}
