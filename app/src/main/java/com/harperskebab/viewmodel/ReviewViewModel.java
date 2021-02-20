package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetReviewsResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReviewViewModel extends BaseViewModel {
    private static final String TAG = "ReviewViewModel";

    private MutableLiveData<RmGetReviewsResponse> reviewsResponse = new MutableLiveData<>();

    public ReviewViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getReviews(Context context, String apiKey, String langCode, String userID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        this.reviewsResponse.setValue(null);

        rfInterface.getReviews(apiKey, langCode, userID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((reviewsResponse) -> onSuccessCancelOrder(reviewsResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmGetReviewsResponse reviewsResponse, NetworkOperations nwCall) {
        this.reviewsResponse.setValue(reviewsResponse);
        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetReviewsResponse> getReviewsResponse() {
        return reviewsResponse;
    }
}
