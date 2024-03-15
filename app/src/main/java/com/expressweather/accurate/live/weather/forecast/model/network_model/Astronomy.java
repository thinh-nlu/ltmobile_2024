package com.expressweather.accurate.live.weather.forecast.model.network_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Astronomy implements Serializable {
    @SerializedName("moon_illumination")
    public String moon_illumination;
    @SerializedName("moon_phase")
    public String moon_phase;
    @SerializedName("moonrise")
    public String moonrise;
    @SerializedName("moonset")
    public String moonset;
    @SerializedName("sunrise")
    public String sunrise;
    @SerializedName("sunset")
    public String sunset;
}
