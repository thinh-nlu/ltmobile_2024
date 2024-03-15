package com.expressweather.accurate.live.weather.forecast.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseDialog;
import com.expressweather.accurate.live.weather.forecast.databinding.DialogRateBinding;

import java.util.ArrayList;
import java.util.List;

public class RateDialog extends BaseDialog<DialogRateBinding> {
    private int countRate;
    private OnRateDialogPress onPress;

    public RateDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogRateBinding setViewBinding(Context context) {
        return DialogRateBinding.inflate(LayoutInflater.from(context));
    }

    @Override
    protected void initView(Context context) {
        binding.rateStar1.setOnClickListener(view -> setStarSelect(1));

        binding.rateStar2.setOnClickListener(view -> setStarSelect(2));

        binding.rateStar3.setOnClickListener(view -> setStarSelect(3));

        binding.rateStar4.setOnClickListener(view -> setStarSelect(4));

        binding.rateStar5.setOnClickListener(view -> setStarSelect(5));

        binding.btnRate.setOnClickListener(view -> {
            if (countRate == 0) {
                Toast.makeText(context, context.getString(R.string.please_select_your_rating), Toast.LENGTH_SHORT).show();
            } else if (countRate < 5) {
                onPress.send();
            } else {
                onPress.rating();
            }
        });

        binding.tvNotnow.setOnClickListener(view -> onPress.later());

        binding.rateStar5.performClick();
    }

    private void setStarSelect(int index) {
        List<ImageView> listStar = new ArrayList<>();
        listStar.add(binding.rateStar1);
        listStar.add(binding.rateStar2);
        listStar.add(binding.rateStar3);
        listStar.add(binding.rateStar4);
        listStar.add(binding.rateStar5);

        for (int i = 0; i < 5; i++) {
            if (i < index) {
                listStar.get(i).setImageResource(R.drawable.ic_star_on);
            } else {
                listStar.get(i).setImageResource(R.drawable.ic_star_off);
            }
        }

        switch (index) {
            case 1:
                binding.imgRate.setImageResource(R.drawable.rate_star_1);
                break;
            case 2:
                binding.imgRate.setImageResource(R.drawable.rate_star_2);
                break;
            case 3:
                binding.imgRate.setImageResource(R.drawable.rate_star_3);
                break;
            case 4:
                binding.imgRate.setImageResource(R.drawable.rate_star_4);
                break;
            case 5:
                binding.imgRate.setImageResource(R.drawable.rate_star_5);
                break;
        }

        countRate = index;
    }

    public void addOnRateListener(OnRateDialogPress onPress) {
        this.onPress = onPress;
    }

    public interface OnRateDialogPress {
        void send();
        void rating();
        void later();
    }

}
