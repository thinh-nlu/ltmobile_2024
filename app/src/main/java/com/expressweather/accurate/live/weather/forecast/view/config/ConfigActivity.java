package com.expressweather.accurate.live.weather.forecast.view.config;

import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.isNetworkAvailable;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivityConfigBinding;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.Network;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;
import com.expressweather.accurate.live.weather.forecast.view.home.HomeFragment;
import com.expressweather.accurate.live.weather.forecast.view.ten_days_forecast.TenDaysActivity;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.nlbn.ads.callback.NativeCallback;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.ConsentHelper;

import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends BaseActivity<ActivityConfigBinding> {


    @Override
    protected ActivityConfigBinding setViewBinding() {
        return ActivityConfigBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        loadNative();
    }

    @Override
    protected void viewListener() {
        handleWindSpeed();
        handleTemperatureItem();
        handlePressure();
        handlePrecipitation();
        handleDisplay();

        binding.imgBack.setOnClickListener(view -> onBackPressed());

        boolean isDegreeC = SharePrefUtils.getBoolean(SharePrefUtils.TEMP_UNIT, true);
        if (isDegreeC) {
            binding.tvDegreeC.performClick();
        } else {
            binding.tvDegreeF.performClick();
        }

        int windSpeedUnit = SharePrefUtils.getInt(SharePrefUtils.WIND_SPEED_UNIT, Constants.WIND_SPEED_KM_PER_H);
        switch (windSpeedUnit) {
            case Constants.WIND_SPEED_KM_PER_H:
                binding.tvKmH.performClick();
                break;
            case Constants.WIND_SPEED_M_PER_S:
                binding.tvMS.performClick();
                break;
            case Constants.WIND_SPEED_KN:
                binding.tvKn.performClick();
                break;
            case Constants.WIND_SPEED_MPH:
                binding.tvMph.performClick();
                break;
            case Constants.WIND_SPEED_FT_PER_S:
                binding.tvFtS.performClick();
                break;
        }

        int pressureUnit = SharePrefUtils.getInt(SharePrefUtils.PRESSURE_UNIT, Constants.PRESSURE_HPA);
        switch (pressureUnit) {
            case Constants.PRESSURE_HPA:
                binding.tvHPa.performClick();
                break;
            case Constants.PRESSURE_MM_HG:
                binding.tvMMHg.performClick();
                break;
            case Constants.PRESSURE_IN_HG:
                binding.tvInHg.performClick();
                break;
        }

        int precipitationUnit = SharePrefUtils.getInt(SharePrefUtils.PRECIPITATION_UNIT, Constants.PRECIPITATION_MM);
        switch (precipitationUnit) {
            case Constants.PRECIPITATION_MM:
                binding.tvMm.performClick();
                break;
            case Constants.PRECIPITATION_INCH:
                binding.tvInch.performClick();
                break;
            case Constants.PRECIPITATION_L_PER_M2:
                binding.tvLM2.performClick();
                break;
        }

        int displayUnit = SharePrefUtils.getInt(SharePrefUtils.VISIBILITY_UNIT, Constants.VISIBILITY_KM);
        switch (displayUnit) {
            case Constants.VISIBILITY_KM:
                binding.tvKm.performClick();
                break;
            case Constants.VISIBILITY_M:
                binding.tvM.performClick();
                break;
            case Constants.VISIBILITY_FT:
                binding.tvFt.performClick();
                break;
        }
    }

    private void handleTemperatureItem() {
        binding.tvDegreeC.setOnClickListener(view -> {
            binding.tvDegreeC.setBackgroundResource(R.drawable.bg_item_config_selected);
            binding.tvDegreeF.setBackground(null);
            SharePrefUtils.putBoolean(SharePrefUtils.TEMP_UNIT, true);
        });

        binding.tvDegreeF.setOnClickListener(view -> {
            binding.tvDegreeF.setBackgroundResource(R.drawable.bg_item_config_selected);
            binding.tvDegreeC.setBackground(null);
            SharePrefUtils.putBoolean(SharePrefUtils.TEMP_UNIT, false);
        });
    }

    private void handleWindSpeed() {
        List<TextView> listItem = new ArrayList<>();
        listItem.add(binding.tvKmH);
        listItem.add(binding.tvMS);
        listItem.add(binding.tvKn);
        listItem.add(binding.tvMph);
        listItem.add(binding.tvFtS);

        binding.tvKmH.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvKmH.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.WIND_SPEED_UNIT, Constants.WIND_SPEED_KM_PER_H);
        });

        binding.tvMS.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvMS.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.WIND_SPEED_UNIT, Constants.WIND_SPEED_M_PER_S);
        });

        binding.tvKn.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvKn.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.WIND_SPEED_UNIT, Constants.WIND_SPEED_KN);
        });

        binding.tvMph.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvMph.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.WIND_SPEED_UNIT, Constants.WIND_SPEED_MPH);
        });

        binding.tvFtS.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvMph.setBackground(null);
            binding.tvFtS.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.WIND_SPEED_UNIT, Constants.WIND_SPEED_FT_PER_S);
        });
    }

    private void handlePressure() {
        List<TextView> listItem = new ArrayList<>();
        listItem.add(binding.tvHPa);
        listItem.add(binding.tvMMHg);
        listItem.add(binding.tvInHg);

        binding.tvHPa.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvHPa.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.PRESSURE_UNIT, Constants.PRESSURE_HPA);
        });

        binding.tvMMHg.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvMMHg.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.PRESSURE_UNIT, Constants.PRESSURE_MM_HG);
        });

        binding.tvInHg.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvMMHg.setBackground(null);
            binding.tvInHg.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.PRESSURE_UNIT, Constants.PRESSURE_IN_HG);
        });
    }

    private void handlePrecipitation() {
        List<TextView> listItem = new ArrayList<>();
        listItem.add(binding.tvMm);
        listItem.add(binding.tvInch);
        listItem.add(binding.tvLM2);

        binding.tvMm.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvMm.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.PRECIPITATION_UNIT, Constants.PRECIPITATION_MM);
        });

        binding.tvInch.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvInch.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.PRECIPITATION_UNIT, Constants.PRECIPITATION_INCH);
        });

        binding.tvLM2.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvInch.setBackground(null);
            binding.tvLM2.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.PRECIPITATION_UNIT, Constants.PRECIPITATION_L_PER_M2);
        });
    }

    private void handleDisplay() {
        List<TextView> listItem = new ArrayList<>();
        listItem.add(binding.tvKm);
        listItem.add(binding.tvM);
        listItem.add(binding.tvFt);

        binding.tvKm.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvKm.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.VISIBILITY_UNIT, Constants.VISIBILITY_KM);
        });

        binding.tvM.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvM.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.VISIBILITY_UNIT, Constants.VISIBILITY_M);
        });

        binding.tvFt.setOnClickListener(view -> {
            setBgItemNonSelect(listItem);
            binding.tvM.setBackground(null);
            binding.tvFt.setBackgroundResource(R.drawable.bg_item_config_selected);
            SharePrefUtils.putInt(SharePrefUtils.VISIBILITY_UNIT, Constants.VISIBILITY_FT);
        });
    }

    private void setBgItemNonSelect(List<TextView> listConfig) {
        for (TextView tv : listConfig) {
            tv.setBackgroundResource(R.drawable.bg_item_config_non_select);
        }

        listConfig.get(listConfig.size() - 1).setBackground(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeFragment.updateData = true;
        String locationFirst = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_FIRST, "");
        String locationSecond = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_SECOND, "");

        if (!locationFirst.isEmpty()) {
            HomeFragment.updateDataFirst = true;
        }

        if (!locationSecond.isEmpty()) {
            HomeFragment.updateDataSecond = true;
        }
    }

    private void loadNative() {
        if (isNetworkAvailable(this)) {
            if (ConsentHelper.getInstance(ConfigActivity.this).canRequestAds()) {

                try {
                    Admob.getInstance().loadNativeAd(this, getString(R.string.native_config), new NativeCallback() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            super.onNativeAdLoaded(nativeAd);
                            NativeAdView adView = (NativeAdView) LayoutInflater.from(ConfigActivity.this).inflate(R.layout.native_ads_below_button, null);
                            binding.nativeAds.removeAllViews();
                            binding.nativeAds.addView(adView);
                            Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
                        }

                        @Override
                        public void onAdFailedToLoad() {
                            super.onAdFailedToLoad();
                            binding.nativeAds.removeAllViews();
                        }
                    });
                } catch (Exception exception) {
                    binding.nativeAds.removeAllViews();
                }
            }
        } else {
            binding.nativeAds.removeAllViews();
        }
    }
}