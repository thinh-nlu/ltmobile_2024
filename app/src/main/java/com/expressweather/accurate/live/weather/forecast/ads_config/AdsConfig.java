package com.expressweather.accurate.live.weather.forecast.ads_config;

import android.content.Context;

import com.expressweather.accurate.live.weather.forecast.R;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.nlbn.ads.callback.InterCallback;
import com.nlbn.ads.util.Admob;

public class AdsConfig {

    private static AdsConfig instance = null;

    private AdsConfig() {}

    public static synchronized AdsConfig getAdsConfig() {
        if (instance == null) {
            instance = new AdsConfig();
        }
        return instance;
    }

    public InterstitialAd mInterstitialAdNextDay;

    public void loadAdsInterNextDay(Context context) {
        Admob.getInstance().loadInterAds(context, context.getString(R.string.inter_nextday), new InterCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                mInterstitialAdNextDay = interstitialAd;
            }
        });
    }

    public InterstitialAd mInterstitialAdSearch;

    public void loadAdsInterSearch(Context context) {
        Admob.getInstance().loadInterAds(context, context.getString(R.string.inter_search), new InterCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                mInterstitialAdSearch = interstitialAd;
            }
        });
    }

    public InterstitialAd mInterstitialAdDetailSearch;

    public void loadAdsInterDetail(Context context) {
        Admob.getInstance().loadInterAds(context, context.getString(R.string.inter_detail), new InterCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                mInterstitialAdDetailSearch = interstitialAd;
            }
        });
    }
}