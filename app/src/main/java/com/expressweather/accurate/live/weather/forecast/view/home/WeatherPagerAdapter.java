package com.expressweather.accurate.live.weather.forecast.view.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;

import java.util.ArrayList;

public class WeatherPagerAdapter extends FragmentStateAdapter {
    ArrayList<RootModel> listRootModel;

    public WeatherPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<RootModel> listWeather) {
        super(fragmentActivity);
        listRootModel = listWeather;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new HomeFragment(listRootModel.get(position));
    }

    @Override
    public int getItemCount() {
        if (listRootModel.isEmpty()) {
            return 1;
        } else {
            return listRootModel.size();
        }
    }
}
