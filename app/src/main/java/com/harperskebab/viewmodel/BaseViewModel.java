package com.harperskebab.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.harperskebab.network.retrofit.RFInterface;
import com.harperskebab.utils.Utility;

public class BaseViewModel extends AndroidViewModel {
    private static final String TAG = "BaseViewModel";

    protected RFInterface rfInterface;
    protected String token;


    public BaseViewModel(@NonNull Application application) {
        super(application);
        rfInterface = Utility.getRetrofitInterface();
    }

    public void setToken(String token) {
        this.token = token;
    }

}
