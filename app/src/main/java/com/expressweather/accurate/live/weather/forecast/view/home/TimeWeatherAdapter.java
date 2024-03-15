package com.expressweather.accurate.live.weather.forecast.view.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.helper.WeatherState;
import com.expressweather.accurate.live.weather.forecast.model.WeatherByHour;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;

import java.util.List;

public class TimeWeatherAdapter extends RecyclerView.Adapter<TimeWeatherAdapter.TimeWeatherViewHolder> {
    List<WeatherByHour> listTimeWeather;
    boolean isDegreeC;

    boolean isToday;
    int hourNow = -1;

    public TimeWeatherAdapter(@NonNull List<WeatherByHour> listTimeWeather, boolean isToday) {
        this.listTimeWeather = listTimeWeather;
        this.isToday = isToday;
        isDegreeC = SharePrefUtils.getBoolean(SharePrefUtils.TEMP_UNIT, true);
    }

    @NonNull
    @Override
    public TimeWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_weather_time, parent, false);
        return new TimeWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeWeatherViewHolder holder, int position) {
        WeatherByHour hourlyWeather = listTimeWeather.get(position);

        holder.tvTemp.setText((isDegreeC ? hourlyWeather.getTempC() : hourlyWeather.getTempF()) + "°");

        int time = hourlyWeather.getTime();

        if (isToday) {
            if (hourNow == time) {
                holder.layoutParent.setBackgroundResource(R.drawable.bg_time_weather_selected);
            } else {
                holder.layoutParent.setBackgroundResource(R.drawable.bg_time_weather);
            }
        }

        if (time < 10) {
            holder.tvTime.setText("0" + time + ":00");
        } else {
            holder.tvTime.setText(time + ":00");
        }

        holder.imgWeather.setImageResource(WeatherState.getImageWeather(hourlyWeather.getWeatherDes()));

        String weatherDes = hourlyWeather.getWeatherDes();
        boolean isSunnyWeather = weatherDes.equals("Clear") || weatherDes.equals("Sunny");

        if (isSunnyWeather) {
            if (time >= 18) {
                holder.imgWeather.setImageResource(R.drawable.ic_moon);
            } else {
                holder.imgWeather.setImageResource(R.drawable.ic_sunshine);
            }
        }

    }

    @Override
    public int getItemCount() {
        return listTimeWeather.size();
    }

    public void updateItemSelected(int hourNow) {
        this.hourNow = hourNow;
        notifyDataSetChanged();
    }

    static class TimeWeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvTemp;
        ImageView imgWeather;
        View layoutParent;

        public TimeWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvDegree);
            imgWeather = itemView.findViewById(R.id.imgWeather);
            layoutParent = itemView.findViewById(R.id.layoutParent);
        }
    }
}
