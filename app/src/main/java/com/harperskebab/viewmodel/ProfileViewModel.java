package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmChangePasswordResponse;
import com.harperskebab.network.retrofit.responsemodels.RmSignInResponse;
import com.harperskebab.network.retrofit.responsemodels.RmUpdateProfileResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileViewModel extends BaseViewModel {
    private static final String TAG = "UpdateProfileModel";

    private MutableLiveData<RmUpdateProfileResponse> updateProfileResponse = new MutableLiveData<>();
    private MutableLiveData<RmSignInResponse> signInResponse = new MutableLiveData<>();
    private MutableLiveData<RmChangePasswordResponse> changePasswordResponse = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);

    }

    @SuppressLint("CheckResult")
    public void updateProfile(Context context, String userid, String firstName, String lastName, String mobileNo, String houseNo, String flatName, String streetName, String postalCode,
                              String cityName, File photo,
                              NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.updateProfileResponse.setValue(null);

        if (photo == null) {

            rfInterface.updateProfile(userid, convertBase64(firstName), convertBase64(lastName), mobileNo, convertBase64(houseNo), convertBase64(flatName), convertBase64(streetName), postalCode, convertBase64(cityName)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((updateProfileResponse) -> onSuccessUpdateProfile(updateProfileResponse, nwCall), throwable -> onError(throwable, nwCall));

        } else {

            try {
                InputStream is;
                is = new FileInputStream(photo.getAbsoluteFile());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = Math.max(options.outWidth / 460, options.outHeight / 288); // Max
                Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
                is.close();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                // System.out.println(fnm + " Compressed");
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                // System.out.println(fnm + " GOT Compressed");
                byte[] data = bos.toByteArray();

                MultipartBody.Part filePart = MultipartBody.Part.createFormData("user_photo", "user_photo", RequestBody.create(data));

                rfInterface.updateProfile(userid, convertBase64(firstName), convertBase64(lastName), mobileNo,convertBase64(houseNo), convertBase64(flatName), convertBase64(streetName), postalCode, convertBase64(cityName), filePart).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((updateProfileResponse) -> onSuccessUpdateProfile(updateProfileResponse, nwCall), throwable -> onError(throwable, nwCall));

            } catch (Exception e) {
                Log.e(TAG, "updateProfile: Exception: ", e);
            }
        }

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

    private void onSuccessUpdateProfile(RmUpdateProfileResponse updateProfileResponse, NetworkOperations nwCall) {
        setUpdateProfileResponse(updateProfileResponse);
        nwCall.onComplete();
    }

    private void onError(Throwable throwable, NetworkOperations nwCall) {
        nwCall.onComplete();
    }

    public MutableLiveData<RmUpdateProfileResponse> getUpdateProfileResponse() {
        return updateProfileResponse;
    }

    private void setUpdateProfileResponse(RmUpdateProfileResponse updateProfileResponse) {
        this.updateProfileResponse.setValue(updateProfileResponse);
    }

    @SuppressLint("CheckResult")
    public void getProfile(Context context, String apiKey, String langCode, String userid, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.signInResponse.setValue(null);

        rfInterface.getProfile(apiKey, langCode, userid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((signInResponse) -> onSuccessGetProfile(signInResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessGetProfile(RmSignInResponse signInResponse, NetworkOperations nwCall) {

        if (signInResponse.getSuccess().equals(0L)) {
            setSignInResponse(signInResponse);
        }
        nwCall.onComplete();
    }

    private void setSignInResponse(RmSignInResponse signInResponse) {
        this.signInResponse.setValue(signInResponse);
    }

    public MutableLiveData<RmSignInResponse> getSignInResponse() {
        return signInResponse;
    }

    @SuppressLint("CheckResult")
    public void changePassword(Context context, String userid, String oldPassword, String newPassword, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        this.changePasswordResponse.setValue(null);

        rfInterface.changePassword(userid, oldPassword, newPassword).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((changePasswordResponse) -> onSuccessChangePassword(changePasswordResponse, nwCall), throwable -> onError(throwable, nwCall));

    }

    private void onSuccessChangePassword(RmChangePasswordResponse changePasswordResponse, NetworkOperations nwCall) {
        setChangePassword(changePasswordResponse);
        nwCall.onComplete();
    }

    private void setChangePassword(RmChangePasswordResponse changePasswordResponse) {
        this.changePasswordResponse.setValue(changePasswordResponse);
    }

    public MutableLiveData<RmChangePasswordResponse> getChangePasswordResponse() {
        return changePasswordResponse;
    }
}
