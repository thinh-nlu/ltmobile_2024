package com.expressweather.accurate.live.weather.forecast.model.network_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Weather implements Serializable {
    @SerializedName("astronomy")
    public ArrayList<Astronomy> astronomy;
    @SerializedName("avgtempC")
    public String avgtempC;
    @SerializedName("avgtempF")
    public String avgtempF;
    @SerializedName("date")
    public String date;
    @SerializedName("hourly")
    public ArrayList<Hourly> hourly;
    @SerializedName("maxtempC")
    public String maxtempC;
    @SerializedName("maxtempF")
    public String maxtempF;
    @SerializedName("mintempC")
    public String mintempC;
    @SerializedName("mintempF")
    public String mintempF;
    @SerializedName("sunHour")
    public String sunHour;
    @SerializedName("totalSnow_cm")
    public String totalSnow_cm;
    @SerializedName("uvIndex")
    public String uvIndex;
}
