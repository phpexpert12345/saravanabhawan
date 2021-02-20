package com.harperskebab.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.harperskebab.utils.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RFClient {

    private static Retrofit retrofitInstanceOne = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(HttpLoggingInterceptor.getInterceptor());

        clientBuilder.readTimeout(60, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);

        if (retrofitInstanceOne == null) {
            Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            retrofitInstanceOne = new Retrofit.Builder().client(clientBuilder.build()).baseUrl(Constant.Url.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofitInstanceOne;
    }
}
