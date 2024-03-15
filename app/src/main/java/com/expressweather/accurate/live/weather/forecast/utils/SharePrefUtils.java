package com.expressweather.accurate.live.weather.forecast.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePrefUtils {

    public static final String COUNT_DENIED_NOTIFICATION_PERMISSION = "CountDeniedNotificationPermission";
    public static final String COUNT_DENIED_LOCATION_PERMISSION = "CountDeniedLocationPermission";
    public static final String HISTORY_SEARCH_LOCATION_FIRST = "HistorySearchLocationFirst";
    public static final String HISTORY_SEARCH_LOCATION_SECOND = "HistorySearchLocationSecond";
    public static final String HISTORY_SEARCH_LOCATION_THIRD = "HistorySearchLocationThird";
    public static final String HISTORY_SEARCH_LOCATION_FOURTH= "HistorySearchLocationFourth";
    public static final String LOCATION_SEARCH = "LocationSearch";
    public static final String WEATHER_CURRENT_INFO = "WeatherCurrentInfo";
    public static final String WEATHER_SEARCH_RESULT = "WeatherSearchResult";
    public static final String WEATHER_LOCATION_SELECTED = "WeatherLocationSelected";
    public static final String LOCATION_FOR_SERVICE = "LocationForService";
    public static final String TEMP_UNIT = "TempUnit";
    public static final String WIND_SPEED_UNIT = "WindSpeedUnit";
    public static final String PRESSURE_UNIT = "PressureUnit";
    public static final String PRECIPITATION_UNIT = "PrecipitationUnit";
    public static final String VISIBILITY_UNIT = "VisibilityUnit";
    public static final String RATED = "Rated";
    public static final String COUNT_EXIT = "CountExit";
    public static final String COUNT_CLICK_TOMORROW = "CountClickTomorrow";
    private static SharedPreferences mSharePref;

    public static void init(Context context) {
        if (mSharePref == null) {
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    private static SharedPreferences.Editor editor() {
        return mSharePref.edit();
    }

    public static int getCountOpenFirstLang(Context context) {
        SharedPreferences pre = context.getSharedPreferences("dataLang", Context.MODE_PRIVATE);
        return pre.getInt("first", 0);
    }

    public static void increaseCountFirstLang(Context context) {
        SharedPreferences pre = context.getSharedPreferences("dataLang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("first", pre.getInt("first", 0) + 1);
        editor.apply();
    }


    public static void putLong(String key, long value) {
        editor().putLong(key, value).apply();
    }

    public static long getLong(String key, long defaultValue) {
        return mSharePref.getLong(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        editor().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return mSharePref.getInt(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        editor().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mSharePref.getBoolean(key, defValue);
    }

    public static void putString(String key, String value) {
        editor().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return mSharePref.getString(key, defValue);
    }
}
