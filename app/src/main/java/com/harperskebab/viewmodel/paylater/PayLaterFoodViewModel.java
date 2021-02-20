package com.harperskebab.viewmodel.paylater;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.harperskebab.model.Food;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.viewmodel.BaseViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayLaterFoodViewModel extends BaseViewModel {
    private static final String TAG = "FoodViewModel";

    private MutableLiveData<List<Food>> foods = new MutableLiveData<>();

    private MutableLiveData<Set<Food>> selectedFoods = new MutableLiveData<>();

    private MutableLiveData<Food> currentSelectedFood = new MutableLiveData<>();

    public PayLaterFoodViewModel(@NonNull Application application) {
        super(application);
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
        this.selectedFoods.setValue(foods);
    }

    public MutableLiveData<Food> getCurrentSelectedFood() {
        return currentSelectedFood;
    }

    public void setCurrentSelectedFood(Food currentSelectedFood) {
        this.currentSelectedFood.setValue(currentSelectedFood);
    }
}
