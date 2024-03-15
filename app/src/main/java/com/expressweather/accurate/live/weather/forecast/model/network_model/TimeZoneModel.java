package com.expressweather.accurate.live.weather.forecast.model.network_model;

import com.google.gson.annotations.SerializedName;

public class TimeZoneModel {
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("timezone_id")
    public String timezone_id;
    @SerializedName("offset")
    public int offset;
    @SerializedName("country_code")
    public String country_code;
    @SerializedName("map_url")
    public String map_url;
}
