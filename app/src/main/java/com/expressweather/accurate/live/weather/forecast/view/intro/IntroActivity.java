package com.expressweather.accurate.live.weather.forecast.view.intro;

import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.getPref;
import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.isNetworkAvailable;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.viewpager2.widget.ViewPager2;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivityIntroBinding;
import com.expressweather.accurate.live.weather.forecast.databinding.DialogTurnOnWifiBinding;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.view.MainActivity;
import com.expressweather.accurate.live.weather.forecast.view.PermissionActivity;
import com.expressweather.accurate.live.weather.forecast.view.intro.adapter.IntroAdapter;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.nlbn.ads.callback.NativeCallback;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.AppOpenManager;
import com.nlbn.ads.util.ConsentHelper;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends BaseActivity<ActivityIntroBinding> {
    private boolean isNextActivity = false;
    private ArrayList<String> introTextContentList;
    private ArrayList<String> introTextTitleList;
    private final ArrayList<String> introAdList = new ArrayList();
    private boolean isLoadingIntro = false;
    private boolean isNextPress = false;

    @Override
    protected ActivityIntroBinding setViewBinding() {
        return ActivityIntroBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        initIntroSlide();
        introAdList.add(getString(R.string.native_intro1));
        introAdList.add(getString(R.string.native_intro2));
        introAdList.add(getString(R.string.native_intro3));
        loadNative(0);
    }

    @Override
    protected void viewListener() {
        binding.btnNext.setOnClickListener(v -> {
            if (!isNextPress) {
                isNextPress = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isNextPress = false;
                    }
                }, 1000);
                if (binding.vpSlideIntro.getCurrentItem() < 2) {
                    binding.vpSlideIntro.setCurrentItem(binding.vpSlideIntro.getCurrentItem() + 1);
                } else {
                    if (!isNetworkAvailable(this)) {
                        showPopupTurnOnWifi();
                    } else {
                        if (!getPref(IntroActivity.this, Constants.PERMISSION, false)) {
                            if (!isNextActivity) {
                                if (!checkPermission()) {
                                    Intent intent = new Intent(IntroActivity.this, PermissionActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    binding.frLoading.setVisibility(View.VISIBLE);
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.frLoading.setVisibility(View.GONE);
                                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 1000);
                                }
                            }
                            isNextActivity = true;
                        } else {
                            if (!checkTurnOnLocation()) {
                                openPopUpLocationSetting(this);
                            } else {
                                if (!isNextActivity) {
                                    if (!checkPermission()) {
                                        Intent intent = new Intent(this, PermissionActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        binding.frLoading.setVisibility(View.VISIBLE);
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.frLoading.setVisibility(View.GONE);
                                                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, 1000);
                                    }
                                }
                                isNextActivity = true;
                            }
                        }
                    }
                }
            }
        });

        binding.vpSlideIntro.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                changeColor();
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) loadNative(0);
                else if (position == 1) loadNative(1);
                else if (position == 2) loadNative(2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                changeColor();
            }
        });
    }

    private void initIntroSlide() {
        List<Integer> introImageList = new ArrayList<>();
        introImageList.add(R.drawable.img_intro_1);
        introImageList.add(R.drawable.img_intro_2);
        introImageList.add(R.drawable.img_intro_3);

        IntroAdapter slideAdapter = new IntroAdapter(introImageList);
        binding.vpSlideIntro.setAdapter(slideAdapter);

        introTextTitleList = new ArrayList<>();
        introTextTitleList.add(getString(R.string.intro_title_1));
        introTextTitleList.add(getString(R.string.intro_title_2));
        introTextTitleList.add(getString(R.string.intro_title_3));

        introTextContentList = new ArrayList<>();
        introTextContentList.add(getString(R.string.intro_content_1));
        introTextContentList.add(getString(R.string.intro_content_2));
        introTextContentList.add(getString(R.string.intro_content_3));
    }

    void changeColor() {
        switch (binding.vpSlideIntro.getCurrentItem()) {
            case 0:
                binding.imgCircle1.setImageResource(R.drawable.ic_dot_selected);
                binding.imgCircle2.setImageResource(R.drawable.ic_dot_not_select);
                binding.imgCircle3.setImageResource(R.drawable.ic_dot_not_select);

                binding.tvTitleIntro.setText(introTextTitleList.get(0));
                binding.tvDescriptionIntro.setText(introTextContentList.get(0));
                binding.btnNext.setText(getString(R.string.next));
                break;
            case 1:
                binding.imgCircle1.setImageResource(R.drawable.ic_dot_not_select);
                binding.imgCircle2.setImageResource(R.drawable.ic_dot_selected);
                binding.imgCircle3.setImageResource(R.drawable.ic_dot_not_select);

                binding.tvTitleIntro.setText(introTextTitleList.get(1));
                binding.tvDescriptionIntro.setText(introTextContentList.get(1));
                binding.btnNext.setText(getString(R.string.next));
                break;
            case 2:
                binding.imgCircle1.setImageResource(R.drawable.ic_dot_not_select);
                binding.imgCircle2.setImageResource(R.drawable.ic_dot_not_select);
                binding.imgCircle3.setImageResource(R.drawable.ic_dot_selected);

                binding.tvTitleIntro.setText(introTextTitleList.get(2));
                binding.tvDescriptionIntro.setText(introTextContentList.get(2));
                binding.btnNext.setText(getString(R.string.get_started));
                break;
        }
    }

    private void loadNative(int position) {
        if (isNetworkAvailable(this)) {
            if (ConsentHelper.getInstance(IntroActivity.this).canRequestAds()) {
                try {
                    if (isLoadingIntro) return;
                    isLoadingIntro = true;
                    Admob.getInstance().loadNativeAd(this, introAdList.get(position), new NativeCallback() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            super.onNativeAdLoaded(nativeAd);
                            isLoadingIntro = false;
                            NativeAdView adView = (NativeAdView) LayoutInflater.from(IntroActivity.this).inflate(R.layout.native_ads_below_button, null);
                            binding.nativeAds.removeAllViews();
                            binding.nativeAds.addView(adView);
                            Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
                        }

                        @Override
                        public void onAdFailedToLoad() {
                            super.onAdFailedToLoad();
                            binding.nativeAds.removeAllViews();
                            isLoadingIntro = false;
                        }
                    });
                } catch (Exception exception) {
                    binding.nativeAds.removeAllViews();
                    isLoadingIntro = false;
                }
            }
        } else {
            binding.nativeAds.removeAllViews();
            isLoadingIntro = false;
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return PermissionChecker.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PermissionChecker.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void showPopupTurnOnWifi() {
        Dialog dialogTurnOnWifi = new Dialog(this);
        hideNavigation(dialogTurnOnWifi);
        DialogTurnOnWifiBinding successBinding = DialogTurnOnWifiBinding.inflate(LayoutInflater.from(this));
        dialogTurnOnWifi.setContentView(successBinding.getRoot());
        Window window = dialogTurnOnWifi.getWindow();
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;

        successBinding.btnOk.setOnClickListener(v -> {
            AppOpenManager.getInstance().disableAppResumeWithActivity(IntroActivity.class);
            dialogTurnOnWifi.dismiss();
            hideNavigation(dialogTurnOnWifi);
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });
        successBinding.btnIgnore.setOnClickListener(v -> {
            dialogTurnOnWifi.dismiss();
            hideNavigation(dialogTurnOnWifi);
        });

        binding.nativeAds.setVisibility(View.GONE);
        dialogTurnOnWifi.setCancelable(false);
        dialogTurnOnWifi.show();
    }

    private void hideNavigation(Dialog dialog) {
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppOpenManager.getInstance().enableAppResumeWithActivity(IntroActivity.class);
        if (!isNetworkAvailable(this)) {
            try {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.layoutDescriptionIntro.getLayoutParams();
                params.reset();
                params.topToBottom = binding.llIntro.getId();
                params.bottomToTop = ConstraintLayout.LayoutParams.UNSET;
                binding.layoutDescriptionIntro.requestLayout();
            } catch (Exception ignore) {
            }
        }
    }
}
