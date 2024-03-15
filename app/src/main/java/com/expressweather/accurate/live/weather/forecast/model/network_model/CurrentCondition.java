package com.expressweather.accurate.live.weather.forecast.model.network_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrentCondition implements Serializable {
    @SerializedName("FeelsLikeC")
    public String feelsLikeC;
    @SerializedName("FeelsLikeF")
    public String feelsLikeF;
    @SerializedName("cloudcover")
    public String cloudcover;
    @SerializedName("humidity")
    public String humidity;
    @SerializedName("localObsDateTime")
    public String localObsDateTime;
    @SerializedName("observation_time")
    public String observation_time;
    @SerializedName("precipInches")
    public String precipInches;
    @SerializedName("precipMM")
    public String precipMM;
    @SerializedName("pressure")
    public String pressure;
    @SerializedName("pressureInches")
    public String pressureInches;
    @SerializedName("temp_C")
    public String temp_C;
    @SerializedName("temp_F")
    public String temp_F;
    @SerializedName("uvIndex")
    public String uvIndex;
    @SerializedName("visibility")
    public String visibility;
    @SerializedName("visibilityMiles")
    public String visibilityMiles;
    @SerializedName("weatherCode")
    public String weatherCode;
    @SerializedName("weatherDesc")
    public ArrayList<WeatherDesc> weatherDesc;
    @SerializedName("winddir16Point")
    public String winddir16Point;
    @SerializedName("winddirDegree")
    public String winddirDegree;
    @SerializedName("windspeedKmph")
    public String windspeedKmph;
    @SerializedName("windspeedMiles")
    public String windspeedMiles;
}
