package com.expressweather.accurate.live.weather.forecast.networking;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherNetworkService {

    private static final String BASE_URL = "https://wttr.in/";

    public static WeatherApi getClient() {

        Retrofit myRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return myRetrofit.create(WeatherApi.class);
    }
}
