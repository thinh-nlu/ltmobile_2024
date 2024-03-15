package com.expressweather.accurate.live.weather.forecast.view;

import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.isNetworkAvailable;
import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.setPref;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivityPermissionBinding;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.Network;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;
import com.expressweather.accurate.live.weather.forecast.view.intro.IntroActivity;
import com.expressweather.accurate.live.weather.forecast.view.language.LanguageActivity;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.nlbn.ads.callback.NativeCallback;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.AppOpenManager;
import com.nlbn.ads.util.ConsentHelper;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {
    private final int NOTIFICATION_PERMISSION_REQUEST_CODE = 111;
    private final int DEVICE_LOCATION_REQUEST_CODE = 222;
    private boolean enableNotification;
    private boolean enableDeviceLocation;
    private int countNotificationDenied;
    private int countDeviceLocationDenied;

    @Override
    protected ActivityPermissionBinding setViewBinding() {
        return ActivityPermissionBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        SharePrefUtils.init(getApplicationContext());
        String description;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            description = String.valueOf(getText(R.string.permission_description)) +
                    getText(R.string.notifications) +
                    " " +
                    getText(R.string.and) +
                    " " +
                    getText(R.string.device_location) +
                    "}";

        } else {
            description = String.valueOf(getText(R.string.permission_description)) +
                    getText(R.string.device_location) +
                    "}";
        }
        binding.tvContent.setText(description);

        loadNativeAds();

    }

    @Override
    protected void viewListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            enableNotification =
                    PermissionChecker.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                    ) == PermissionChecker.PERMISSION_GRANTED;

            binding.imgSwitchNotification.setOnClickListener(view -> {
                if (countNotificationDenied > 1 && !enableNotification) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
                    }
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            NOTIFICATION_PERMISSION_REQUEST_CODE);
                }
            });
        }

        enableDeviceLocation = checkLocationPermission();

        binding.imgSwitchDeviceLocation.setOnClickListener(view -> {
            if (countDeviceLocationDenied > 1 && !enableDeviceLocation) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
                }
            } else {
                if (!checkTurnOnLocation()) {
                    openPopUpLocationSetting(this);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            DEVICE_LOCATION_REQUEST_CODE);
                }

            }
        });

        binding.tvGo.setOnClickListener(view -> {
            binding.frLoading.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.frLoading.setVisibility(View.GONE);
                    startActivity(new Intent(PermissionActivity.this, MainActivity.class));
                    setPref(PermissionActivity.this, Constants.PERMISSION, true);
                    finish();
                }
            }, 1000);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //enable ads resume
        AppOpenManager.getInstance().enableAppResumeWithActivity(PermissionActivity.class);
        countNotificationDenied = SharePrefUtils.getInt(SharePrefUtils.COUNT_DENIED_NOTIFICATION_PERMISSION, 0);
        countDeviceLocationDenied = SharePrefUtils.getInt(SharePrefUtils.COUNT_DENIED_LOCATION_PERMISSION, 0);
        setCheckedSwitchPermission();
        enableGo();
    }

    private void setCheckedSwitchPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            enableNotification =
                    PermissionChecker.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                    ) == PermissionChecker.PERMISSION_GRANTED;

            binding.cvNotification.setVisibility(View.VISIBLE);
            binding.imgSwitchNotification.setImageResource(enableNotification ? R.drawable.ic_switch_selected : R.drawable.ic_switch_non_select);
        } else {
            binding.cvNotification.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.cvDeviceLocation.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            binding.cvDeviceLocation.requestLayout();
        }

        enableDeviceLocation = checkLocationPermission();
        binding.imgSwitchDeviceLocation.setImageResource(enableDeviceLocation ? R.drawable.ic_switch_selected : R.drawable.ic_switch_non_select);
    }

    private boolean checkLocationPermission() {
        return PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case NOTIFICATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        enableNotification = true;
                        binding.imgSwitchNotification.setImageResource(R.drawable.ic_switch_selected);
                    } else {
                        enableNotification = false;
                        binding.imgSwitchNotification.setImageResource(R.drawable.ic_switch_non_select);
                        countNotificationDenied++;
                        SharePrefUtils.putInt(SharePrefUtils.COUNT_DENIED_NOTIFICATION_PERMISSION, countNotificationDenied);
                    }
                }
                break;
            case DEVICE_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        enableDeviceLocation = true;
                        binding.imgSwitchDeviceLocation.setImageResource(R.drawable.ic_switch_selected);
                    } else {
                        enableDeviceLocation = false;
                        binding.imgSwitchDeviceLocation.setImageResource(R.drawable.ic_switch_non_select);
                        countDeviceLocationDenied++;
                        SharePrefUtils.putInt(SharePrefUtils.COUNT_DENIED_LOCATION_PERMISSION, countDeviceLocationDenied);
                    }
                }
        }

        enableGo();
    }

    private boolean enableGo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (enableNotification && enableDeviceLocation) {
                binding.tvGo.setEnabled(true);
                binding.tvGo.setTextColor(ContextCompat.getColor(this, R.color.white));
                return true;
            } else {
                binding.tvGo.setEnabled(false);
                binding.tvGo.setTextColor(ContextCompat.getColor(this, R.color.white));
                return false;
            }
        } else {
            if (enableDeviceLocation) {
                binding.tvGo.setEnabled(true);
                binding.tvGo.setTextColor(ContextCompat.getColor(this, R.color.white));
                return true;
            } else {
                binding.tvGo.setEnabled(false);
                binding.tvGo.setTextColor(ContextCompat.getColor(this, R.color.white));
                return false;
            }
        }
    }

    private void loadNativeAds() {
        if (isNetworkAvailable(this)) {
            if (ConsentHelper.getInstance(PermissionActivity.this).canRequestAds()) {
                try {
                    Admob.getInstance().loadNativeAd(this, getString(R.string.native_permission), new NativeCallback() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            super.onNativeAdLoaded(nativeAd);
                            NativeAdView adView = (NativeAdView) LayoutInflater.from(PermissionActivity.this).inflate(R.layout.native_ads_below_button, null);
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