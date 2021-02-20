package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetAllergyResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AllergyViewModel extends BaseViewModel {
    private static final String TAG = "AllergyViewModel";

    private MutableLiveData<RmGetAllergyResponse> allergyResponse = new MutableLiveData<>();

    public AllergyViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getAllergy(Context context, String apiKey, String langCode, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getAllergy(apiKey, langCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((getAllergyResponse) -> onSuccess(getAllergyResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccess(RmGetAllergyResponse allergyResponse, NetworkOperations nwCall) {

        this.allergyResponse.setValue(allergyResponse);

        nwCall.onComplete();
    }

    private void onError(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetAllergyResponse> getAllergyResponse() {
        return allergyResponse;
    }
}
