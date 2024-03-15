package com.expressweather.accurate.live.weather.forecast.model.network_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Hourly implements Serializable {
        @SerializedName("DewPointC")
        public String dewPointC;
        @SerializedName("DewPointF")
        public String dewPointF;
        @SerializedName("FeelsLikeC")
        public String feelsLikeC;
        @SerializedName("FeelsLikeF")
        public String feelsLikeF;
        @SerializedName("HeatIndexC")
        public String heatIndexC;
        @SerializedName("HeatIndexF")
        public String heatIndexF;
        @SerializedName("WindChillC")
        public String windChillC;
        @SerializedName("WindChillF")
        public String windChillF;
        @SerializedName("WindGustKmph")
        public String windGustKmph;
        @SerializedName("WindGustMiles")
        public String windGustMiles;
        @SerializedName("chanceoffog")
        public String chanceoffog;
        @SerializedName("chanceoffrost")
        public String chanceoffrost;
        @SerializedName("chanceofhightemp")
        public String chanceofhightemp;
        @SerializedName("chanceofovercast")
        public String chanceofovercast;
        @SerializedName("chanceofrain")
        public String chanceofrain;
        @SerializedName("chanceofremdry")
        public String chanceofremdry;
        @SerializedName("chanceofsnow")
        public String chanceofsnow;
        @SerializedName("chanceofsunshine")
        public String chanceofsunshine;
        @SerializedName("chanceofthunder")
        public String chanceofthunder;
        @SerializedName("chanceofwindy")
        public String chanceofwindy;
        @SerializedName("cloudcover")
        public String cloudcover;
        @SerializedName("humidity")
        public String humidity;
        @SerializedName("precipInches")
        public String precipInches;
        @SerializedName("precipMM")
        public String precipMM;
        @SerializedName("pressure")
        public String pressure;
        @SerializedName("pressureInches")
        public String pressureInches;
        @SerializedName("tempC")
        public String tempC;
        @SerializedName("tempF")
        public String tempF;
        @SerializedName("time")
        public String time;
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
