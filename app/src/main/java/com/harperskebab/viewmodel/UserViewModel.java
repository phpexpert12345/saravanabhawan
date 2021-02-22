package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmSignInResponse;
import com.harperskebab.network.retrofit.responsemodels.RmSignUpResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.SharedPrefrenceObj;

import java.io.UnsupportedEncodingException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<RmSignInResponse> signInResponse = new MutableLiveData<>();
    private MutableLiveData<RmSignUpResponse> signUpResponse = new MutableLiveData<>();


    public UserViewModel(@NonNull Application application) {
        super(application);

        signInResponse.setValue(new Gson().fromJson(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.USER), RmSignInResponse.class));

    }

    @SuppressLint("CheckResult")
    public void signIn(Context context, String email, String password, String api_key, String lang_code, String device_id, String device_platform, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.signIn(email, password, api_key, lang_code, device_id, device_platform).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((signInResponse) -> onSuccessSignIn(signInResponse, nwCall), throwable -> onErrorSignIn(throwable, nwCall));

    }

    private void onSuccessSignIn(RmSignInResponse signInResponse, NetworkOperations nwCall) {
        setSignInResponse(signInResponse);
        nwCall.onComplete();
    }

    private void onErrorSignIn(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmSignInResponse> getSignInResponse() {
        return signInResponse;
    }

    public void setSignInResponse(RmSignInResponse signInResponse) {
        if (signInResponse == null) {
            SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.USER, null);
        } else {
            SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.USER, signInResponse.toString());
        }
        this.signInResponse.setValue(signInResponse);
    }

    @SuppressLint("CheckResult")
    public void signUp(Context context, String first_name, String last_name, String email, String password, String phone, String country, String referral_code,
                       String api_key, String lang_code, String device_id, String device_platform,String branch_id, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.signUp(convertBase64(first_name), convertBase64(last_name), email, password, phone, country, referral_code, api_key, lang_code, device_id, device_platform,branch_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((signUpResponse) -> onSuccessSignUp(signUpResponse, nwCall), throwable -> onErrorSignUp(throwable, nwCall));

    }

    private void onSuccessSignUp(RmSignUpResponse signUpResponse, NetworkOperations nwCall) {
        setSignupResponse(signUpResponse);
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
    private void onErrorSignUp(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmSignUpResponse> getSignUpResponse() {
        return signUpResponse;
    }

    public void setSignupResponse(RmSignUpResponse signUpResponse) {
        this.signUpResponse.setValue(signUpResponse);
    }
}
