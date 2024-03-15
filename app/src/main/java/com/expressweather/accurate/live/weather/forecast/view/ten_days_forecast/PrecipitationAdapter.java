package com.expressweather.accurate.live.weather.forecast.view.ten_days_forecast;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.helper.WeatherState;
import com.expressweather.accurate.live.weather.forecast.model.network_model.Weather;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PrecipitationAdapter extends RecyclerView.Adapter<PrecipitationAdapter.PrecipitationViewHolder> {
    List<Weather> listWeather;
    int maxTempList;
    int minTempList;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
    private final Context context;
    boolean isDegreeC;

    public PrecipitationAdapter(@NonNull List<Weather> listPre, Context context) {
        this.listWeather = listPre;
        this.context = context;
        getRangeTemperature();
        isDegreeC = SharePrefUtils.getBoolean(SharePrefUtils.TEMP_UNIT, true);
    }

    @NonNull
    @Override
    public PrecipitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_precipitation, parent, false);
        return new PrecipitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrecipitationViewHolder holder, int position) {
        Weather weather = listWeather.get(position);

        holder.tvDegreeStart.setText((isDegreeC ? weather.mintempC : weather.mintempF) + "°");
        holder.tvDegreeEnd.setText((isDegreeC ? weather.maxtempC : weather.maxtempF) + "°");
        String date = weather.date;
        Calendar calendar = Calendar.getInstance();

        int indexY = date.indexOf("-");
        int indexM = date.lastIndexOf("-");
        String month = date.substring(indexY + 1, indexM);
        String day = date.substring(indexM + 1);

        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        holder.tvDate.setText(day + "/" + month);
        if (position == 0) {
            holder.tvWeekDay.setText(context.getString(R.string.today));
        } else {
            String weekDay = simpleDateFormat.format(calendar.getTimeInMillis());
            String weekDayFinal = weekDay.substring(0,1).toUpperCase() + weekDay.substring(1);
            holder.tvWeekDay.setText(weekDayFinal);
        }

        if (position == (listWeather.size() - 1)) {
            holder.lineBottom.setVisibility(View.INVISIBLE);
        }

        //get weather at 12:00 am
        holder.imgWeather.setImageResource(WeatherState.getImageWeather(weather.hourly.get(4).weatherDesc.get(0).value));

        ViewTreeObserver vto = holder.barTempBg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.barTempBg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = holder.barTempBg.getMeasuredWidth();
                int rangeTemp = maxTempList - minTempList;
                int unit = width / rangeTemp;

                int minTempWeather = Integer.parseInt(weather.mintempC);
                int maxTempWeather = Integer.parseInt(weather.maxtempC);
                Log.d("setMarginTempProcess", "minTempWeather " + minTempWeather + " - maxTempWeather "
                        + maxTempWeather + " - minTempList " + minTempList + " - maxTempList" + maxTempList + " - width " + width);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.barTemp.getLayoutParams();
                params.setMargins((minTempWeather - minTempList) * unit, 0, (maxTempList - maxTempWeather) * unit, 0);
                holder.barTemp.setLayoutParams(params);

            }
        });
    }

    private void getRangeTemperature() {
        maxTempList = Integer.parseInt(listWeather.get(0).maxtempC);
        minTempList = Integer.parseInt(listWeather.get(0).mintempC);

        for (int i = 0; i < listWeather.size(); i++) {
            int tempMax = Integer.parseInt(listWeather.get(i).maxtempC);
            int tempMin = Integer.parseInt(listWeather.get(i).mintempC);
            if (maxTempList < tempMax) {
                maxTempList = tempMax;
            }

            if (minTempList > tempMin) {
                minTempList = tempMin;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listWeather.size();
    }

    static class PrecipitationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDegreeStart;
        TextView tvDegreeEnd;
        ImageView imgWeather;
        TextView tvWeekDay;
        TextView tvDate;
        View lineBottom, barTempBg, barTemp;

        public PrecipitationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDegreeStart = itemView.findViewById(R.id.tvDegreeStart);
            tvDegreeEnd = itemView.findViewById(R.id.tvDegreeEnd);
            imgWeather = itemView.findViewById(R.id.imgWeather);
            tvWeekDay = itemView.findViewById(R.id.tvWeekDay);
            tvDate = itemView.findViewById(R.id.tvDate);
            lineBottom = itemView.findViewById(R.id.lineBottom);
            barTemp = itemView.findViewById(R.id.barTemperature);
            barTempBg = itemView.findViewById(R.id.barTemperatureBg);
        }
    }
}
