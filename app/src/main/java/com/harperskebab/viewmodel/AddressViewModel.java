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

public class AddressViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<RmGetAddressResponse> getAddressResponse = new MutableLiveData<>();
    private MutableLiveData<RmAddAddressResponse> addAddressResponse = new MutableLiveData<>();
    private MutableLiveData<RmDeleteAddressResponse> deleteAddressResponse = new MutableLiveData<>();


    public AddressViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getAddress(Context context, String userid, String lang, String lat, NetworkOperations nwCall) {

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

    public MutableLiveData<RmGetAddressResponse> getGetAddressResponse() {
        return getAddressResponse;
    }

    @SuppressLint("CheckResult")
    public void addAddress(Context context, String userid, String addressTitle, String house,String flateName, String street, String city, String state, String zipcode,
                           String country, String address_direction, String phone, String lang_code, String api_key, NetworkOperations nwCall) {

        nwCall.onStart(context, "");


        rfInterface.addAddress(convertBase64(flateName),userid, convertBase64(addressTitle), convertBase64(house), convertBase64(street), convertBase64(city), state, zipcode, convertBase64(country), address_direction, phone, lang_code, api_key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((addAddressResponse) -> onSuccessAddAddress(addAddressResponse, nwCall), throwable -> onErrorAddAddress(throwable, nwCall));

    }

    private void onSuccessAddAddress(RmAddAddressResponse addAddressResponse, NetworkOperations nwCall) {
        this.addAddressResponse.setValue(addAddressResponse);
        nwCall.onComplete();
    }
    private String convertBase64(String str) {
        byte[] data;
        String base64="";

        try {

            data = str.getBytes("UTF-8");

            base64 = Base64.encodeToString(data, Base64.DEFAULT);

            Log.i("Base 64 ", base64);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }
        return base64;
    }
    private void onErrorAddAddress(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmAddAddressResponse> getAddAddressResponse() {
        return addAddressResponse;
    }

    @SuppressLint("CheckResult")
    public void deleteAddress(Context context, String api_key, String lang_code, String addressID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.deleteAddressResponse.setValue(null);

        rfInterface.deleteAddress(api_key, lang_code, addressID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((deleteAddressResponse) -> onSuccessDeleteAddress(deleteAddressResponse, nwCall), throwable -> onErrorDeleteAddress(throwable, nwCall));

    }

    private void onSuccessDeleteAddress(RmDeleteAddressResponse deleteAddressResponse, NetworkOperations nwCall) {
        this.deleteAddressResponse.setValue(deleteAddressResponse);
        nwCall.onComplete();
    }

    private void onErrorDeleteAddress(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmDeleteAddressResponse> getDeleteAddressResponse() {
        return deleteAddressResponse;
    }
}
