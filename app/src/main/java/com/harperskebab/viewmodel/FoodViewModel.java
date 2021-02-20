package com.harperskebab.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harperskebab.model.Food;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetCardsResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetComboExtraToppingListResponse;
import com.harperskebab.network.retrofit.responsemodels.RmGetComboListResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.SharedPrefrenceObj;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FoodViewModel extends BaseViewModel {
    private static final String TAG = "FoodViewModel";

    private MutableLiveData<List<Food>> foods = new MutableLiveData<>();

    private MutableLiveData<Set<Food>> selectedFoods = new MutableLiveData<>();

    private MutableLiveData<Food> currentSelectedFood = new MutableLiveData<>();
    private MutableLiveData<RmGetComboListResponse> comboListResponse = new MutableLiveData<>();
    private MutableLiveData<RmGetComboExtraToppingListResponse> comboExtraToppingListResponse = new MutableLiveData<>();

    public FoodViewModel(@NonNull Application application) {
        super(application);

        Type type = new TypeToken<Set<Food>>() {
        }.getType();

        this.selectedFoods.setValue(new Gson().fromJson(SharedPrefrenceObj.getSharedValue(getApplication(), Constant.SharedPreference.SELECTED_FOOD), type));
    }
    @SuppressLint("CheckResult")
    public void getComboExtraTopping(Context context, String apiKey, String langCode, String itemID,String comboslot_id,String foodItemSizeID, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getComboExtraTopping(apiKey, langCode,itemID,comboslot_id,foodItemSizeID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((comboExtraToppingListResponse) -> onSuccessComboExtraToppingList(comboExtraToppingListResponse, nwCall), throwable -> onError(throwable, nwCall));
    }
    private void onSuccessComboExtraToppingList(RmGetComboExtraToppingListResponse comboExtraToppingListResponse, NetworkOperations nwCall) {
        this.comboExtraToppingListResponse.setValue(comboExtraToppingListResponse);
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetComboExtraToppingListResponse> getGetComboExtraToppingListResponse() {
        return comboExtraToppingListResponse;
    }
    @SuppressLint("CheckResult")
    public void getComboFood(Context context,String branchId, String apiKey, String langCode, String restorentId,String categoryId, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getComboFood(branchId,apiKey, langCode, restorentId,categoryId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((comboListResponse) -> onSuccessCardList(comboListResponse, nwCall), throwable -> onError(throwable, nwCall));
    }
    private void onSuccessCardList(RmGetComboListResponse comboListResponse, NetworkOperations nwCall) {
        this.comboListResponse.setValue(comboListResponse);
        nwCall.onComplete();
    }

    public MutableLiveData<RmGetComboListResponse> getGetComboListResponse() {
        return comboListResponse;
    }


    private void onError(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    @SuppressLint("CheckResult")
    public void getFood(Context context,String branchId, String apiKey, String langCode, String foodCategory,String restId, NetworkOperations nwCall) {

        nwCall.onStart(context, "");

        rfInterface.getFood(branchId,apiKey, langCode, foodCategory,restId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((rmFrontBannerResponse) -> onSuccessFood(rmFrontBannerResponse.getMenuCat().get(0).get(0).getFood(), nwCall), throwable -> onErrorFood(throwable, nwCall));

    }

    private void onSuccessFood(List<Food> foods, NetworkOperations nwCall) {
        if (this.selectedFoods.getValue() == null) {
            this.selectedFoods.setValue(new HashSet<>());
        } else {
            for (int i = 0; i < foods.size(); i++) {
                Set<Food> selectedFoods = this.selectedFoods.getValue();
                for (Food selecedFood : selectedFoods) {
                    if (foods.get(i).getItemID().equals(selecedFood.getItemID())) {
                        foods.remove(i);
                        foods.add(i, selecedFood);
                    }
                }
            }
        }
        this.foods.setValue(foods);

        nwCall.onComplete();
    }

    private void onErrorFood(Throwable throwable, NetworkOperations nwCall) {
        //todo
        nwCall.onComplete();
    }

    public MutableLiveData<List<Food>> getFoods() {
        return foods;
    }

    public MutableLiveData<Set<Food>> getSelectedFoods() {
        return selectedFoods;
    }

    public void setSelectedFoods(Set<Food> foods) {
        SharedPrefrenceObj.setSharedValue(getApplication(), Constant.SharedPreference.SELECTED_FOOD, new Gson().toJson(foods));
        this.selectedFoods.setValue(foods);
    }

    public MutableLiveData<Food> getCurrentSelectedFood() {
        return currentSelectedFood;
    }

    public void setCurrentSelectedFood(Food currentSelectedFood) {
        this.currentSelectedFood.setValue(currentSelectedFood);
    }

}
