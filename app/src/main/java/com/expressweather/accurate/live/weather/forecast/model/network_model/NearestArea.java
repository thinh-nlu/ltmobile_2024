package com.expressweather.accurate.live.weather.forecast.model.network_model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NearestArea implements Serializable {
    @SerializedName("areaName")
    public ArrayList<AreaName> areaName;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("population")
    public String population;
}
