package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.RestaurantBranch;
import com.harperskebab.network.NetworkOperations;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BranchViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<List<RestaurantBranch>> restaurantBranches = new MutableLiveData<>();


    public BranchViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getBranch(Context context, String api_key, String lang_code, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getBranch(api_key, lang_code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((branchResponse) -> onSuccessBranch(branchResponse.getRestaurantBranch(), nwCall), throwable -> onErrorBranch(throwable, nwCall));

    }

    private void onSuccessBranch(List<RestaurantBranch> restaurantBranches, NetworkOperations nwCall) {
        this.restaurantBranches.setValue(restaurantBranches);
        nwCall.onComplete();
    }

    private void onErrorBranch(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<RestaurantBranch>> getRestaurantBranches() {
        return restaurantBranches;
    }
}
