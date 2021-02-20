package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmSubmitTicketResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewTicketViewModel extends BaseViewModel {
    private static final String TAG = "NewTicketViewModel";

    private MutableLiveData<RmSubmitTicketResponse> ticketResponse = new MutableLiveData<>();

    public NewTicketViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void submitContactUs(Context context, String apiKey, String langCode, String subject, String orderID, String email, String message, String userID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        this.ticketResponse.setValue(null);

        rfInterface.submitNewTicket(apiKey, langCode, subject, orderID, email, message, userID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((submitTicketResponse) -> onSuccessCancelOrder(submitTicketResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmSubmitTicketResponse ticketResponse, NetworkOperations nwCall) {

        this.ticketResponse.setValue(ticketResponse);

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmSubmitTicketResponse> getTicketResponse() {
        return ticketResponse;
    }
}
