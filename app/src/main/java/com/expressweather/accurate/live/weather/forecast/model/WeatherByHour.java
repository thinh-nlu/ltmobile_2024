package com.expressweather.accurate.live.weather.forecast.model;

public class WeatherByHour {
    int time;
    String tempC;
    String tempF;
    String weatherDes;

    public WeatherByHour(int time, String tempC, String tempF, String weatherDes) {
        this.time = time;
        this.tempC = tempC;
        this.tempF = tempF;
        this.weatherDes = weatherDes;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getWeatherDes() {
        return weatherDes;
    }

    public void setWeatherDes(String weatherDes) {
        this.weatherDes = weatherDes;
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
}
