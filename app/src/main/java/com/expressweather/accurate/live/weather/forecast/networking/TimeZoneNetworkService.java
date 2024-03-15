package com.expressweather.accurate.live.weather.forecast.networking;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimeZoneNetworkService {
    private static final String BASE_URL = "https://api.wheretheiss.at/";

    public static TimeZoneApi getClient() {

        Retrofit myRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return myRetrofit.create(TimeZoneApi.class);
    }
}
