package com.harperskebab;

import com.harperskebab.network.retrofit.RFInterface;
import com.harperskebab.network.retrofit.responsemodels.RmGetPaymentKeyResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.stripe.android.PaymentConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RFInterface rfInterface = Utility.getRetrofitInterface();

        rfInterface.getPaymentKey(Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE).enqueue(new Callback<RmGetPaymentKeyResponse>() {
            @Override
            public void onResponse(Call<RmGetPaymentKeyResponse> call, Response<RmGetPaymentKeyResponse> response) {
                PaymentConfiguration.init(
                        getApplicationContext(),
                        response.body().getStripePublishKey()
                );
            }

            @Override
            public void onFailure(Call<RmGetPaymentKeyResponse> call, Throwable t) {

            }
        });


    }

}
