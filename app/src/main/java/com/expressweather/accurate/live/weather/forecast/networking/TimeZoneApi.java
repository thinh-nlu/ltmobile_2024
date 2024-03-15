package com.expressweather.accurate.live.weather.forecast.networking;

import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;
import com.expressweather.accurate.live.weather.forecast.model.network_model.TimeZoneModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TimeZoneApi {
    @GET("/v1/coordinates/{latitude},{longitude}")
    Call<TimeZoneModel> getTimeZone(@Path("latitude") String latitude, @Path("longitude") String longitude);
}
