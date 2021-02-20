package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.Table;
import com.harperskebab.network.NetworkOperations;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EatInViewModel extends BaseViewModel {
    private static final String TAG = "PostalCodeViewModel";

    private MutableLiveData<List<Table>> tableList = new MutableLiveData<>();


    public EatInViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getTableList(Context context, String apiKey, String langCode, NetworkOperations nwCall,String branch_id) {

        tableList.setValue(null);

        nwCall.onStart(context, "");

        rfInterface.getTableList(apiKey, langCode,branch_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((tableListResponse) -> onSuccessPostalcode(tableListResponse.getTable(), nwCall), throwable -> onErrorPostalcode(throwable, nwCall));

    }

    private void onSuccessPostalcode(List<Table> tableList, NetworkOperations nwCall) {
        this.tableList.setValue(tableList);
        nwCall.onComplete();
    }

    private void onErrorPostalcode(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<Table>> getTableList() {
        return tableList;
    }
}
