package com.expressweather.accurate.live.weather.forecast.view;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.expressweather.accurate.live.weather.forecast.BuildConfig;
import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivityPolicyBinding;
import com.expressweather.accurate.live.weather.forecast.utils.Network;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.AppOpenManager;

public class PolicyActivity extends BaseActivity<ActivityPolicyBinding> {

    @Override
    protected ActivityPolicyBinding setViewBinding() {
        return ActivityPolicyBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        binding.icBack.setOnClickListener(view -> onBackPressed());
        binding.tvPolicy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ntd-technology.web.app/policy.html"));
            startActivity(intent);
            AppOpenManager.getInstance().disableAppResumeWithActivity(PolicyActivity.class);
        });

        binding.tvVersion.setText(getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void viewListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //enable ads resume
        AppOpenManager.getInstance().enableAppResumeWithActivity(PolicyActivity.class);
    }
}