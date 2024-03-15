package com.expressweather.accurate.live.weather.forecast.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivitySplashBinding;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;
import com.expressweather.accurate.live.weather.forecast.view.intro.IntroActivity;
import com.expressweather.accurate.live.weather.forecast.view.language.LanguageActivity;
import com.nlbn.ads.callback.InterCallback;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.ConsentHelper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private boolean openingApp = false;
    private InterCallback interCallback;
    private boolean isGoToNextScreen = false;

    @Override
    protected ActivitySplashBinding setViewBinding() {
        return ActivitySplashBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        binding.icLogo.startAnimation(slideAnimation);

        //ads
        interCallback = new InterCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                openNextScreen();
            }
        };
        autoTransitionAfter(30000L);

        ConsentHelper consentHelper = ConsentHelper.getInstance(this);
        if (!consentHelper.canLoadAndShowAds()) {
            consentHelper.reset();
        }
        consentHelper.obtainConsentAndShow(this, this::loadAdsInter);
    }

    public void loadAdsInter(){
        Admob.getInstance().loadSplashInterAds2(this, getString(R.string.inter_splash), 3000, interCallback);
    }
    @Override
    protected void viewListener() {}

    @Override
    protected void onResume() {
        super.onResume();
        Admob.getInstance().onCheckShowSplashWhenFail(this, interCallback, 1000);
        openingApp = true;
    }

    public void openNextScreen() {
        isGoToNextScreen = true;
        if (openingApp) {
            Log.d("openNextScreen", "called");
            Intent intent = new Intent(this, LanguageActivity.class);
            intent.putExtra(Constants.LANGUAGE_START, true);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        openingApp = false;
        super.onStop();
        Admob.getInstance().dismissLoadingDialog();
    }

    private void autoTransitionAfter(Long second){
        try {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Log.d("autoTransitionAfter", "Function running...");
                if(!isGoToNextScreen){
                    openNextScreen();
                }
            }, second);
        } catch (Exception e){
            Log.d("autoTransitionAfter", e.toString());
        }
    }
}