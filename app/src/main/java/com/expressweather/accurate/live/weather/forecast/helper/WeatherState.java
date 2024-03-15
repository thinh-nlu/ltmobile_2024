package com.expressweather.accurate.live.weather.forecast.helper;

import com.expressweather.accurate.live.weather.forecast.R;

import java.util.ArrayList;
import java.util.Calendar;

public class WeatherState {
    public static int gifWeather = R.drawable.cloudy;

    public static int getImageWeather(String stateWeather) {
        ArrayList<String> listPartlyCloudyWeather = new ArrayList<>();
        listPartlyCloudyWeather.add("Partly cloudy");

        ArrayList<String> listCloudyWeather = new ArrayList<>();
        listCloudyWeather.add("Cloudy");

        ArrayList<String> listSunShineWeather = new ArrayList<>();
        listSunShineWeather.add("Clear");
        listSunShineWeather.add("Sunny");

        ArrayList<String> listOvercastWeather = new ArrayList<>();
        listOvercastWeather.add("Overcast");
        listOvercastWeather.add("Mist");
        listOvercastWeather.add("Fog");
        listOvercastWeather.add("Freezing fog");

        ArrayList<String> listRainWeather = new ArrayList<>();
        listRainWeather.add("Moderate rain at times");
        listRainWeather.add("Moderate rain");
        listRainWeather.add("Heavy rain at times");
        listRainWeather.add("Heavy rain");
        listRainWeather.add("Shower in vicinity");
        listRainWeather.add("Moderate or heavy freezing rain");
        listRainWeather.add("Moderate or heavy sleet");
        listRainWeather.add("Ice pellets");
        listRainWeather.add("Moderate or heavy rain shower");
        listRainWeather.add("Torrential rain shower");
        listRainWeather.add("Light sleet showers");
        listRainWeather.add("Moderate or heavy sleet showers");
        listRainWeather.add("Moderate or heavy snow showers");

        ArrayList<String> listDrizzleWeather = new ArrayList<>();
        listDrizzleWeather.add("Patchy freezing drizzle possible");
        listDrizzleWeather.add("Patchy light drizzle");
        listDrizzleWeather.add("Light drizzle");
        listDrizzleWeather.add("Freezing drizzle");
        listDrizzleWeather.add("Heavy freezing drizzle");

        ArrayList<String> listLightRainWeather = new ArrayList<>();
        listLightRainWeather.add("Patchy rain possible");
        listLightRainWeather.add("Patchy sleet possible");
        listLightRainWeather.add("Patchy light rain");
        listLightRainWeather.add("Light rain");
        listLightRainWeather.add("Light rain shower");
        listLightRainWeather.add("Light snow showers");
        listLightRainWeather.add("Light sleet");
        listLightRainWeather.add("Light freezing rain");

        ArrayList<String> listSnowWeather = new ArrayList<>();
        listSnowWeather.add("Moderate or heavy snow with thunder");
        listSnowWeather.add("Heavy snow");
        listSnowWeather.add("Patchy heavy snow");
        listSnowWeather.add("Moderate snow");
        listSnowWeather.add("Patchy moderate snow");
        listSnowWeather.add("Blowing snow");

        ArrayList<String> listLightSnowWeather = new ArrayList<>();
        listLightSnowWeather.add("Light snow");
        listLightSnowWeather.add("Patchy light snow");
        listLightSnowWeather.add("Patchy snow possible");
        listLightSnowWeather.add("Patchy light snow with thunder");

        ArrayList<String> listThunderLightningWeather = new ArrayList<>();
        listThunderLightningWeather.add("Thundery outbreaks possible");
        listThunderLightningWeather.add("Patchy light rain with thunder");
        listThunderLightningWeather.add("Moderate or heavy rain with thunder");

        ArrayList<String> listStormWeather = new ArrayList<>();
        listStormWeather.add("Blizzard");

        if (listCloudyWeather.contains(stateWeather)) {
            gifWeather = R.drawable.cloudy;
            return R.drawable.ic_cloudy;
        }

        if (listPartlyCloudyWeather.contains(stateWeather)) {
            gifWeather = R.drawable.partly_cloudy;
            return R.drawable.ic_partly_cloudy;
        }

        if (listDrizzleWeather.contains(stateWeather)) {
            gifWeather = R.drawable.drizzle;
            return R.drawable.ic_drizzle;
        }

        if (listLightRainWeather.contains(stateWeather)) {
            gifWeather = R.drawable.rain;
            return R.drawable.ic_raining;
        }

        if (listSunShineWeather.contains(stateWeather)) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 18) {
                gifWeather = R.drawable.clear_night;
                return R.drawable.ic_moon;

            } else {
                gifWeather = R.drawable.cloudy;
                return R.drawable.ic_sunshine;
            }
        }

        if (listOvercastWeather.contains(stateWeather)) {
            gifWeather = R.drawable.fog;
            return R.drawable.ic_frog;
        }

        if (listRainWeather.contains(stateWeather)) {
            gifWeather = R.drawable.heavy_rain;
            return R.drawable.ic_raining;
        }

        if (listSnowWeather.contains(stateWeather)) {
            gifWeather = R.drawable.snow;
            return R.drawable.ic_heavy_snow;
        }

        if (listLightSnowWeather.contains(stateWeather)) {
            gifWeather = R.drawable.light_snow;
            return R.drawable.ic_snowy;
        }

        if (listThunderLightningWeather.contains(stateWeather)) {
            gifWeather = R.drawable.thunder;
            return R.drawable.ic_thunder;
        }

        if (listStormWeather.contains(stateWeather)) {
            gifWeather = R.drawable.thunder;
            return R.drawable.ic_storm;
        }

        return R.drawable.ic_overcast;
    }
}

//113: Trời trong                            : Clear
//113: Trời nắng                             : Sunny
//116: Có mây                                : Partly cloudy
//119: Nhiều mây                             : Cloudy
//122: Âm u                                  : Overcast
//143: Sương mù                              : Mist
//176: Có mưa rải rác                        : Patchy rain possible
//179: Có tuyết rải rác                      : Patchy snow possible
//182: Có mưa đá rải rác                     : Patchy sleet possible
//185: Có mưa phùn băng rải rác              : Patchy freezing drizzle possible
//200: Có dông                               : Thundery outbreaks possible
//227: Tuyết bay                             : Blowing snow
//230: Bão tuyết                             : Blizzard
//248: Sương mù                              : Fog
//260: Sương mù đóng băng                    : Freezing fog
//263: Mưa phùn nhẹ rải rác                  : Patchy light drizzle
//266: Mưa phùn nhẹ                          : Light drizzle
//281: Mưa phùn băng                         : Freezing drizzle
//284: Mưa phùn băng to                      : Heavy freezing drizzle
//293: Mưa nhẹ rải rác                       : Patchy light rain
//296: Mưa nhỏ                               : Light rain
//299: Thỉnh thoảng có mưa vừa               : Moderate rain at times
//302: Mưa vừa                               : Moderate rain
//305: Thỉnh thoảng có mưa to                : Heavy rain at times
//308: Mưa to                                : Heavy rain
//311: Mưa băng giá nhẹ                      : Light freezing rain
//314: Mưa băng giá vừa đến to               : Moderate or heavy freezing rain
//317: Mưa đá nhỏ                            : Light sleet
//320: Mưa đá vừa đến to                     : Moderate or heavy sleet
//323: Tuyết rơi nhẹ rải rác                 : Patchy light snow
//326: Tuyết rơi nhẹ                         : Light snow
//329: Tuyết rơi vừa rải rác                 : Patchy moderate snow
//332: Tuyết rơi vừa                         : Moderate snow
//335: Tuyết rơi dày rải rác                 : Patchy heavy snow
//338: Tuyết rơi dày                         : Heavy snow
//350: Mưa đá                                : Ice pellets
//353: Mưa rào nhẹ                           : Light rain shower
//356: Mưa rào vừa đến to                    : Moderate or heavy rain shower
//359: Mưa rào xối xả                        : Torrential rain shower
//362: Mưa rào đá nhẹ                        : Light sleet showers
//365: Mưa rào đá vừa đến to                 : Moderate or heavy sleet showers
//368: Mưa rào tuyết nhỏ                     : Light snow showers
//371: Mưa rào tuyết vừa đến to              : Moderate or heavy snow showers
//386: Mưa rào nhẹ có dông rải rác           : Patchy light rain with thunder
//389: Mưa dông vừa đến to                   : Moderate or heavy rain with thunder
//392: Tuyết rơi nhẹ có dông rải rác         : Patchy light snow with thunder
//395: Tuyết rơi vừa đến dày có dông rải rác : Moderate or heavy snow with thunder


