package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.FoodGallery;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetGalleryResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GalleryViewModel extends BaseViewModel {
    private static final String TAG = "OpeningHourViewModel";

    private MutableLiveData<List<FoodGallery>> foodGalleries = new MutableLiveData<>();

    public GalleryViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getDeliveryArea(Context context, String apiKey, String langCode, String splashType, NetworkOperations nwCall,String branch_id) {

        nwCall.onStart(context, "");
        this.foodGalleries.setValue(null);

        rfInterface.getGallery(apiKey, langCode, splashType,branch_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((galleryResponse) -> onSuccessCancelOrder(galleryResponse, nwCall), throwable -> onErrorCancelOrder(throwable, nwCall));

    }

    private void onSuccessCancelOrder(RmGetGalleryResponse galleryResponse, NetworkOperations nwCall) {

        this.foodGalleries.setValue(galleryResponse.getFoodGalleryList().get(0));

        nwCall.onComplete();
    }

    private void onErrorCancelOrder(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<FoodGallery>> getFoodGalleries() {
        return foodGalleries;
    }
}
