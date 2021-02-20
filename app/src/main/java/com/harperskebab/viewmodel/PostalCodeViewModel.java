package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmVerifiyPostalcodeResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostalCodeViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<RmVerifiyPostalcodeResponse> verifiyPostalcodeResponse = new MutableLiveData<>();


    public PostalCodeViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void verifyPostalCode(Context context, String apiKey, String langCode, String orderType, String postalCode, NetworkOperations nwCall) {

        verifiyPostalcodeResponse.setValue(null);

        nwCall.onStart(context, "");

        rfInterface.verifyPostalCode(apiKey, langCode, orderType, postalCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((verifiyPostalcodeResponse) -> onSuccessPostalcode(verifiyPostalcodeResponse, nwCall), throwable -> onErrorPostalcode(throwable, nwCall));

    }

    private void onSuccessPostalcode(RmVerifiyPostalcodeResponse verifiyPostalcodeResponse, NetworkOperations nwCall) {
        this.verifiyPostalcodeResponse.setValue(verifiyPostalcodeResponse);
        nwCall.onComplete();
    }

    private void onErrorPostalcode(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmVerifiyPostalcodeResponse> getVerifiyPostalcodeResponse() {
        return verifiyPostalcodeResponse;
    }
}
