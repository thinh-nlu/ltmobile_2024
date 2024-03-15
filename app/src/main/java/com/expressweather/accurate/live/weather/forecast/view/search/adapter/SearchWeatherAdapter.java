package com.expressweather.accurate.live.weather.forecast.view.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.model.WeatherSearchModel;
import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;

import java.util.ArrayList;

public class SearchWeatherAdapter extends RecyclerView.Adapter<SearchWeatherAdapter.SearchWeatherViewHolder> {
    private final ArrayList<WeatherSearchModel> weatherList;
    private final ArrayList<RootModel> rootWeatherList;
    private final WeatherItemListener listener;
    boolean isDegreeC;
    public SearchWeatherAdapter(ArrayList<WeatherSearchModel> weatherList, ArrayList<RootModel> rootWeatherList, WeatherItemListener listener) {
        this.weatherList = weatherList;
        this.listener = listener;
        this.rootWeatherList = rootWeatherList;
        isDegreeC = SharePrefUtils.getBoolean(SharePrefUtils.TEMP_UNIT, true);
    }

    @NonNull
    @Override
    public SearchWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_search_weather, parent, false);
        return new SearchWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchWeatherViewHolder holder, int position) {
        WeatherSearchModel weather = weatherList.get(position);

        holder.imgWeatherBg.setImageResource(weather.getWeatherBg());
        holder.imgWeatherState.setImageResource(weather.getImgWeatherState());
        holder.tvLocation.setText(weather.getLocation());
        holder.tvDegree.setText(isDegreeC ? weather.getTempC() + "°" : weather.getTempF() + "°");
        holder.tvWeather.setText(weather.getWeatherState());

        holder.itemView.setOnClickListener(view -> listener.onClick(rootWeatherList.get(position)));
    }

    @Override
    public int getItemCount() {
        return Math.min(weatherList.size(), 5);
    }

    static class SearchWeatherViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgWeatherBg, imgWeatherState;
        private final TextView tvLocation, tvDegree, tvWeather;

        public SearchWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWeatherBg = itemView.findViewById(R.id.imgWeatherBg);
            imgWeatherState = itemView.findViewById(R.id.imgWeather);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDegree = itemView.findViewById(R.id.tvDegree);
            tvWeather = itemView.findViewById(R.id.tvWeather);
        }
    }

    public interface WeatherItemListener {
        void onClick(RootModel rootModel);
    }
}
