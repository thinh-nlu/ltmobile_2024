package com.expressweather.accurate.live.weather.forecast.application;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.adrevenue.AppsFlyerAdRevenue;
import com.appsflyer.adrevenue.BuildConfig;
import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.view.SplashActivity;
import com.nlbn.ads.util.AdsApplication;
import com.nlbn.ads.util.AppOpenManager;
import com.nlbn.ads.util.AppsFlyer;

import java.util.List;

public class MyApplication extends AdsApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        AppsFlyer.getInstance().initAppFlyer(this, getString(R.string.app_flyer), true);
    }

    @Override
    public boolean enableAdsResume() {
        return true;
    }

    @Override
    public List<String> getListTestDeviceId() {
        return null;
    }

    @Override
    public String getResumeAdId() {
        return getString(R.string.appopen_resume);
    }

    @Override
    public Boolean buildDebug() {
        return BuildConfig.DEBUG;
    }
}