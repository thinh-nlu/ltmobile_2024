package com.expressweather.accurate.live.weather.forecast.view.ten_days_forecast;

import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.isNetworkAvailable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.ads_config.AdsConfig;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivityTenDaysBinding;
import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;
import com.expressweather.accurate.live.weather.forecast.model.network_model.Weather;
import com.expressweather.accurate.live.weather.forecast.utils.Network;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;
import com.expressweather.accurate.live.weather.forecast.view.language.LanguageActivity;
import com.nlbn.ads.banner.BannerPlugin;
import com.nlbn.ads.callback.InterCallback;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.ConsentHelper;

import java.util.ArrayList;

public class TenDaysActivity extends BaseActivity<ActivityTenDaysBinding> {
    @Override
    protected ActivityTenDaysBinding setViewBinding() {
        return ActivityTenDaysBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConsentHelper.getInstance(TenDaysActivity.this).canRequestAds()) {
            if (AdsConfig.getAdsConfig().mInterstitialAdNextDay == null) {
                AdsConfig.getAdsConfig().loadAdsInterNextDay(this);
            }
        }
    }

    @Override
    protected void initView() {
        RootModel dataWeather = (RootModel) getIntent().getSerializableExtra(SharePrefUtils.WEATHER_LOCATION_SELECTED);
        if (dataWeather != null) {
            setDataWeather(dataWeather);
        }
        if (ConsentHelper.getInstance(TenDaysActivity.this).canRequestAds()) {
            loadBanner();
        }
    }

    @Override
    protected void viewListener() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());
    }

    private void setDataWeather(RootModel dataWeather) {
        ArrayList<Weather> weatherList = dataWeather.weather;
        PrecipitationAdapter adapter = new PrecipitationAdapter(weatherList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTenPrecipitation.setLayoutManager(layoutManager);
        binding.rvTenPrecipitation.setAdapter(adapter);
    }

    private void loadBanner() {
        if (isNetworkAvailable(this)) {
            binding.banner.setVisibility(View.VISIBLE);
            BannerPlugin.Config config = new BannerPlugin.Config();
            config.defaultAdUnitId = getString(R.string.banner_all);
            config.defaultBannerType = BannerPlugin.BannerType.Adaptive;
            Admob.getInstance().loadBannerPlugin(this, findViewById(R.id.banner), findViewById(R.id.shimmer), config);
//            Admob.getInstance().loadBanner(this, getString(R.string.banner_all));
        } else {
            binding.banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (isNetworkAvailable(this)) {
                Admob.getInstance().showInterAds(this, AdsConfig.getAdsConfig().mInterstitialAdNextDay, new InterCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        AdsConfig.getAdsConfig().loadAdsInterNextDay(TenDaysActivity.this);
                        TenDaysActivity.super.onBackPressed();
                    }
                });
        } else {
            super.onBackPressed();
        }
    }
}