package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmSubmitContactUsResponse;

import java.io.UnsupportedEncodingException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactUsViewModel extends BaseViewModel {
    private static final String TAG = "OpeningHourViewModel";

    private MutableLiveData<RmSubmitContactUsResponse> contactUsResponse = new MutableLiveData<>();

    public ContactUsViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void submitContactUs(Context context, String apiKey, String langCode, String name, String email, String mobile, String message, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        this.contactUsResponse.setValue(null);

        rfInterface.submitContactUs(apiKey, langCode, convertBase64(name), email, mobile,convertBase64(message)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((contactUsResponse) -> onSuccessCancelOrder(contactUsResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmSubmitContactUsResponse contactUsResponse, NetworkOperations nwCall) {

        this.contactUsResponse.setValue(contactUsResponse);

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
    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmSubmitContactUsResponse> getContactUsResponse() {
        return contactUsResponse;
    }

}
