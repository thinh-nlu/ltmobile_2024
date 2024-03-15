package com.expressweather.accurate.live.weather.forecast.helper;

import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;

import java.util.Calendar;

public class UnitHelper {

    public static String windSpeedUnit = "km/h";
    public static String pressureUnit = "hPa";
    public static String precipitationUnit = "mm";
    public static String visibilityUnit = "km";

    //convert from value km/h
    public static String getWindSpeed(int value) {
        int windSpeed = value;
        int windSpeedUnitIndex = SharePrefUtils.getInt(SharePrefUtils.WIND_SPEED_UNIT, Constants.WIND_SPEED_KM_PER_H);
        switch (windSpeedUnitIndex) {
            case Constants.WIND_SPEED_M_PER_S:
                windSpeed = value * 5 / 18;
                windSpeedUnit = "m/s";
                break;
            case Constants.WIND_SPEED_MPH:
                windSpeed = (int) Math.round(value * 0.621);
                windSpeedUnit = "mph";
                break;
            case Constants.WIND_SPEED_FT_PER_S:
                windSpeed = (int) Math.round(value * 0.911);
                windSpeedUnit = "ft/s";
                break;
            case Constants.WIND_SPEED_KN:
                windSpeed = (int) Math.round(value * 0.54);
                windSpeedUnit = "kn";
                break;
            default:
                windSpeedUnit = "km/h";
        }
        return "" + windSpeed;
    }

    //default value get from api is mBar, 1 mBar = 1 hPa
    public static String getPressure(double value) {
        double pressureValue = value;
        int pressureUnitIndex = SharePrefUtils.getInt(SharePrefUtils.PRESSURE_UNIT, Constants.PRESSURE_HPA);

        switch (pressureUnitIndex) {
            case Constants.PRESSURE_IN_HG:
                pressureValue = (double) Math.round(value * 9.5) / 1000;
                pressureUnit = "inHg";
                break;
            case Constants.PRESSURE_MM_HG:
                pressureValue = (double) Math.round(value * 750) / 1000;
                pressureUnit = "mmHg";
                break;
            case Constants.PRESSURE_HPA:
                pressureValue = value;
                pressureUnit = "hPa";
                break;
        }
        return "" + pressureValue;
    }

    //convert from mm
    public static String getPrecipitation(double value) {
        double precipitationValue = value;
        int precipitationUnitIndex = SharePrefUtils.getInt(SharePrefUtils.PRECIPITATION_UNIT, Constants.PRECIPITATION_MM);

        switch (precipitationUnitIndex) {
            case Constants.PRECIPITATION_MM:
                precipitationValue = value;
                precipitationUnit = "mm";
                break;
            case Constants.PRECIPITATION_INCH:
                precipitationValue = (double) Math.round(value * 3.9) / 100;
                precipitationUnit = "inch";
                break;
            case Constants.PRECIPITATION_L_PER_M2:
                precipitationUnit = "L/m²";
                break;
        }

        return "" + precipitationValue;
    }

    //default value get from api is value in km
    public static String getVisibility(int value) {
        int visibilityValue = value;
        int visibilityUnitIndex = SharePrefUtils.getInt(SharePrefUtils.VISIBILITY_UNIT, Constants.VISIBILITY_KM);

        switch (visibilityUnitIndex) {
            case Constants.VISIBILITY_M:
                visibilityValue = value * 1000;
                visibilityUnit = "m";
                break;
            case Constants.VISIBILITY_FT:
                visibilityValue = value * 3280;
                visibilityUnit = "ft";
                break;
            default:
                visibilityUnit = "km";
        }

        return "" + visibilityValue;
    }
}
