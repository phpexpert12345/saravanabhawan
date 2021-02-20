package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmForgetPasswordResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForgetPasswordViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<RmForgetPasswordResponse> forgetPasswordResponse = new MutableLiveData<>();


    public ForgetPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void forgetPassword(Context context, String api_key, String lang_code, String email, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.forgetPasswordResponse.setValue(null);

        rfInterface.forgetPassword(api_key, lang_code, email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((forgetPasswordResponse) -> onSuccessBranch(forgetPasswordResponse, nwCall), throwable -> onErrorBranch(throwable, nwCall));

    }

    private void onSuccessBranch(RmForgetPasswordResponse forgetPasswordResponse, NetworkOperations nwCall) {
        this.forgetPasswordResponse.setValue(forgetPasswordResponse);
        nwCall.onComplete();
    }

    private void onErrorBranch(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmForgetPasswordResponse> getForgetPasswordResponse() {
        return forgetPasswordResponse;
    }

}
