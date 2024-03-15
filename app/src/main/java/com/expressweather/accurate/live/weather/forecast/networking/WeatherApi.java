package com.expressweather.accurate.live.weather.forecast.networking;


import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WeatherApi {
    @GET("/{location}?format=j1")
    Call<RootModel> getWeatherCurrent(@Path("location") String location);
}
