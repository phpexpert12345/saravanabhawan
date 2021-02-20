package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmAddAddressResponse;
import com.harperskebab.network.retrofit.responsemodels.RmDeleteAddressResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetAddressResponse;

import java.io.UnsupportedEncodingException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddressFromPostcodeViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<RmGetAddressResponse> getAddressResponse = new MutableLiveData<>();


    public AddressFromPostcodeViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getAddressFromPostcode(Context context, String userid, String lang, String lat, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getAddress(userid, lang, lat).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((addressResponse) -> onSuccessAddress(addressResponse, nwCall), throwable -> onErrorAddress(throwable, nwCall));

    }

    private void onSuccessAddress(RmGetAddressResponse getAddressResponse, NetworkOperations nwCall) {
        this.getAddressResponse.setValue(getAddressResponse);
        nwCall.onComplete();
    }

    private void onErrorAddress(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

}
