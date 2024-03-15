package com.expressweather.accurate.live.weather.forecast.model;

import java.io.Serializable;

public class WeatherSearchModel implements Serializable {
    private int weatherBg;
    private String location;
    private String tempC;
    private String tempF;
    private String weatherState;
    private int imgWeatherState;

    public WeatherSearchModel(int weatherBg, String location, String tempC, String tempF, String weatherState, int imgWeatherState) {
        this.weatherBg = weatherBg;
        this.location = location;
        this.tempC = tempC;
        this.weatherState = weatherState;
        this.imgWeatherState = imgWeatherState;
        this.tempF = tempF;
    }

    public int getWeatherBg() {
        return weatherBg;
    }

    public void setWeatherBg(int weatherBg) {
        this.weatherBg = weatherBg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTempC() {
        return tempC;
    }

    public void setTempC(String tempC) {
        this.tempC = tempC;
    }

    public String getTempF() {
        return tempF;
    }

    public void setTempF(String tempF) {
        this.tempF = tempF;
    }

    public String getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(String weatherState) {
        this.weatherState = weatherState;
    }

    public int getImgWeatherState() {
        return imgWeatherState;
    }

    public void setImgWeatherState(int imgWeatherState) {
        this.imgWeatherState = imgWeatherState;
    }
}
