package com.expressweather.accurate.live.weather.forecast.model;

public class TimeWeatherModel {
    private long time;
    private int degree;

    public TimeWeatherModel(long time, int degree) {
        this.time = time;
        this.degree = degree;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
