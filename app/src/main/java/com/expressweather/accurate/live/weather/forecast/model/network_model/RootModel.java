package com.expressweather.accurate.live.weather.forecast.model.network_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RootModel implements Serializable {
    @SerializedName("current_condition")
    public ArrayList<CurrentCondition> current_condition;
    @SerializedName("nearest_area")
    public ArrayList<NearestArea> nearest_area;
    @SerializedName("weather")
    public ArrayList<Weather> weather;
}
