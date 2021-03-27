package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.FoodCategory;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FoodCategoryViewModel extends BaseViewModel {
    private static final String TAG = "FoodCategoryViewModel";

    private MutableLiveData<List<FoodCategory>> foodCategories = new MutableLiveData<>();

    public FoodCategoryViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void getFoodCategory(Context context,String branchId, String apiKey, String langCode, NetworkOperations nwCall) {

        nwCall.onStart(context, "");
        String url= Constant.Url.BASE_URL+Constant.Url.FOOD_CATEGORY+"?api_key="+apiKey+"&lang_code="+langCode+"&branch_id="+branchId;
        Log.i("url",url);

        rfInterface.getFoodCategory(apiKey, langCode,branchId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rmFrontBannerResponse -> onSuccessFoodCategory(rmFrontBannerResponse.getFoodCategory(), nwCall,""), throwable -> onErrorFoodCategory(throwable, nwCall));

    }

    private void onSuccessFoodCategory(List<FoodCategory> foodCategories, NetworkOperations nwCall,String message) {
        if(foodCategories.get(0).getError()==1){
            foodCategories.get(0).setCategoryName(message);
        }
        this.foodCategories.setValue(foodCategories);
        nwCall.onComplete();
    }

    private void onErrorFoodCategory(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<FoodCategory>> getFoodCategories() {
        return foodCategories;
    }

}
