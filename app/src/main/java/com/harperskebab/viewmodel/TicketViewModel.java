package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetTicketResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TicketViewModel extends BaseViewModel {
    private static final String TAG = "TicketViewModel";

    private MutableLiveData<RmGetTicketResponse> ticketResponse = new MutableLiveData<>();

    public TicketViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getTickets(Context context, String api_key, String lang_code, String userid, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getTickets(api_key, lang_code, userid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ticketResponse) -> onSuccessAddress(ticketResponse, nwCall), throwable -> onErrorAddress(throwable, nwCall));

    }

    private void onSuccessAddress(RmGetTicketResponse ticketResponse, NetworkOperations nwCall) {
        this.ticketResponse.setValue(ticketResponse);
        nwCall.onComplete();
    }

    private void onErrorAddress(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetTicketResponse> getTicketResponse() {
        return ticketResponse;
    }

}
