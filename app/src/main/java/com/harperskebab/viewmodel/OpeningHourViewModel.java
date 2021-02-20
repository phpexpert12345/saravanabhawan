package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetOpeningHourResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OpeningHourViewModel extends BaseViewModel {
    private static final String TAG = "OpeningHourViewModel";

    private MutableLiveData<RmGetOpeningHourResponse> openingHourResponse = new MutableLiveData<>();

    public OpeningHourViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getOpeningHours(Context context, String apiKey, String langCode, NetworkOperations nwCall,String branch_id) {

        nwCall.onStart(context, "");
        this.openingHourResponse.setValue(null);

        rfInterface.getOpeningHours(apiKey, langCode,branch_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((openingHourResponse) -> onSuccessCancelOrder(openingHourResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmGetOpeningHourResponse openingHourResponse, NetworkOperations nwCall) {

        this.openingHourResponse.setValue(openingHourResponse);

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetOpeningHourResponse> getOpeningHourResponse() {
        return openingHourResponse;
    }
}
