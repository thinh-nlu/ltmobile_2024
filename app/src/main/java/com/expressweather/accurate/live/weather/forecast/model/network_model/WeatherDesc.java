package com.expressweather.accurate.live.weather.forecast.model.network_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherDesc implements Serializable {
    @SerializedName("value")
    public String value;
}
