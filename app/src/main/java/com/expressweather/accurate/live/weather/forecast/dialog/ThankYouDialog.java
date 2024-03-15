package com.expressweather.accurate.live.weather.forecast.dialog;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.expressweather.accurate.live.weather.forecast.base.BaseDialog;
import com.expressweather.accurate.live.weather.forecast.databinding.DialogRateSuccessBinding;

public class ThankYouDialog extends BaseDialog<DialogRateSuccessBinding> {
    public ThankYouDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogRateSuccessBinding setViewBinding(Context context) {
        return DialogRateSuccessBinding.inflate(LayoutInflater.from(context));
    }

    @Override
    protected void initView(Context context) {
        binding.cvOk.setOnClickListener(view -> dismiss());
    }
}
