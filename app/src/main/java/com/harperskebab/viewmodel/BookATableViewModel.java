package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmBookATableResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BookATableViewModel extends BaseViewModel {
    private static final String TAG = "BookATableViewModel";

    private MutableLiveData<RmBookATableResponse> bookATableResponse = new MutableLiveData<>();


    public BookATableViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void bookATable(Context context, String apiKey, String langCode, String userid, String tableNumber, String mobile, String bookingDate, String bookingTime,
                                 String bookingInstruction, String noOfPerson, String totalBillAmount, String billDiscountAmount,
                                 String subTotalAfterDiscount, String serviceCharge, String finalAmount, String depositeAmount, NetworkOperations nwCall) {

        bookATableResponse.setValue(null);

        nwCall.onStart(context, "");

        rfInterface.bookATable(apiKey, langCode, userid, tableNumber, mobile, bookingDate, bookingTime, bookingInstruction, noOfPerson, totalBillAmount, billDiscountAmount,
                subTotalAfterDiscount, serviceCharge, finalAmount, depositeAmount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((bookATableResponse) -> onSuccessCouponCode(bookATableResponse, nwCall), throwable -> onErrorCouponCode(throwable, nwCall));

    }

    private void onSuccessCouponCode(RmBookATableResponse bookATableResponse, NetworkOperations nwCall) {
        this.bookATableResponse.setValue(bookATableResponse);
        nwCall.onComplete();
    }

    private void onErrorCouponCode(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<RmBookATableResponse> getBookATableResponse() {
        return bookATableResponse;
    }

}
