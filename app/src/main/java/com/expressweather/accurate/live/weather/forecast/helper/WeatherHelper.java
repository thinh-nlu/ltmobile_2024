package com.expressweather.accurate.live.weather.forecast.helper;

import android.content.Context;

import com.expressweather.accurate.live.weather.forecast.R;

public class WeatherHelper {

    public static String getUVDescription(Context context, int uvIndex) {
        if (uvIndex <= 2) {
            return context.getString(R.string.low);
        } else if (uvIndex <= 5) {
            return context.getString(R.string.moderate);
        } else if (uvIndex <= 7) {
            return context.getString(R.string.high);
        } else if (uvIndex <= 10) {
            return context.getString(R.string.very_high);
        } else {
            return context.getString(R.string.extreme);
        }
    }

    public static String getFeelLikeDescription(Context context, int tempActual, int tempFeelLike) {
        if ((tempFeelLike - tempActual) > 3) {
            return context.getString(R.string.different_from_temperature);
        } else {
            return context.getString(R.string.similar_to_temperature);
        }
    }
}
