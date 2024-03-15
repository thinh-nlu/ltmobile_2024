package com.expressweather.accurate.live.weather.forecast.view.language;

import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.isNetworkAvailable;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivityLanguageBinding;
import com.expressweather.accurate.live.weather.forecast.locale.SystemUtils;
import com.expressweather.accurate.live.weather.forecast.model.LanguageModel;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.Network;
import com.expressweather.accurate.live.weather.forecast.view.MainActivity;
import com.expressweather.accurate.live.weather.forecast.view.intro.IntroActivity;
import com.expressweather.accurate.live.weather.forecast.view.language.adapter.LanguageStartAdapter;
import com.expressweather.accurate.live.weather.forecast.view.ten_days_forecast.TenDaysActivity;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.nlbn.ads.callback.NativeCallback;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.ConsentHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {
    List<LanguageModel> listLanguage;
    String codeLang;
    private boolean languageStart = false;

    @Override
    protected ActivityLanguageBinding setViewBinding() {
        return ActivityLanguageBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        initData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageAdapter = new LanguageStartAdapter(listLanguage, code -> codeLang = code, this);

        // set checked default lang local
        String codeLangDefault = Locale.getDefault().getLanguage();
        String[] langDefault = {"fr", "pt", "es", "hi"};
        if (!Arrays.asList(langDefault).contains(codeLangDefault)) codeLang = "en";

        languageAdapter.setCheck(codeLang);
        binding.rvLanguage.setLayoutManager(linearLayoutManager);
        binding.rvLanguage.setAdapter(languageAdapter);

        getIntentData();

        if (languageStart) {
            binding.icBack.setVisibility(View.GONE);
            if (ConsentHelper.getInstance(LanguageActivity.this).canRequestAds()) {
                loadNativeLanguageStart();
            }
        } else {
            if (ConsentHelper.getInstance(LanguageActivity.this).canRequestAds()) {
                loadNativeLanguageSetting();
            }
        }
    }

    @Override
    protected void viewListener() {
        binding.icBack.setOnClickListener(view -> onBackPressed()
        );

        binding.imgChooseLanguage.setOnClickListener(view -> {
            SystemUtils.saveLocale(getBaseContext(), codeLang);
            startActivity(new Intent(LanguageActivity.this, languageStart ? IntroActivity.class : MainActivity.class));
            finishAffinity();
        });
    }

    private void getIntentData() {
        if (getIntent().hasExtra(Constants.LANGUAGE_START)) {
            languageStart = getIntent().getBooleanExtra(Constants.LANGUAGE_START, false);
        }
    }

    private void initData() {
        codeLang = Locale.getDefault().getLanguage();
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel("English", "en"));
        listLanguage.add(new LanguageModel("French", "fr"));
        listLanguage.add(new LanguageModel("Portuguese", "pt"));
        listLanguage.add(new LanguageModel("Spanish", "es"));
        listLanguage.add(new LanguageModel("Hindi", "hi"));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

    private void loadNativeLanguageStart() {
        if (isNetworkAvailable(this)) {
            try {
                Admob.getInstance().loadNativeAd(this, getString(R.string.native_language), new NativeCallback(){
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        super.onNativeAdLoaded(nativeAd);
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(LanguageActivity.this).inflate(R.layout.native_ads_below_button, null);
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
            } catch (Exception exception){
                binding.nativeAds.removeAllViews();
            }
        } else {
            binding.nativeAds.removeAllViews();
        }
    }

    private void loadNativeLanguageSetting() {
        if (isNetworkAvailable(this)) {
            if (ConsentHelper.getInstance(LanguageActivity.this).canRequestAds()) {
                try {
                    Admob.getInstance().loadNativeAd(this, getString(R.string.native_language_setting), new NativeCallback() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            super.onNativeAdLoaded(nativeAd);
                            NativeAdView adView = (NativeAdView) LayoutInflater.from(LanguageActivity.this).inflate(R.layout.native_ads_below_button, null);
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