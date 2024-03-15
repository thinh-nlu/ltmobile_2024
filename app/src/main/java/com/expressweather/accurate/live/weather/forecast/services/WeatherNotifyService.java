package com.expressweather.accurate.live.weather.forecast.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.helper.WeatherState;
import com.expressweather.accurate.live.weather.forecast.model.network_model.AreaName;
import com.expressweather.accurate.live.weather.forecast.model.network_model.CurrentCondition;
import com.expressweather.accurate.live.weather.forecast.model.network_model.NearestArea;
import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;
import com.expressweather.accurate.live.weather.forecast.networking.WeatherApi;
import com.expressweather.accurate.live.weather.forecast.networking.WeatherNetworkService;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;
import com.expressweather.accurate.live.weather.forecast.view.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherNotifyService extends Service {
    String weatherChannelId = "weatherChannelId";
    private WeatherApi weatherApi;
    private Context context;
    private String location = "My Location";
    boolean isDegreeC;
    private String temperature = "00";
    private String weatherString = "Updating...";

    private int NOTIFY_ID = 78;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createWeatherNotificationChannel(this);
        weatherApi = WeatherNetworkService.getClient();
        context = this;
        startForeground(NOTIFY_ID, buildNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharePrefUtils.init(context);
        String location = SharePrefUtils.getString(SharePrefUtils.LOCATION_FOR_SERVICE, "");
        isDegreeC = SharePrefUtils.getBoolean(SharePrefUtils.TEMP_UNIT, true);

        getDataWeather(location);

        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent iEnd = new Intent(getApplicationContext(), WeatherNotifyService.class);
        PendingIntent piEnd = PendingIntent.getService(getApplicationContext(), 0, iEnd, PendingIntent.FLAG_IMMUTABLE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3600 * 1000, piEnd);
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("MissingPermission")
    private void getDataWeather(String locationGetWeather) {
        Call<RootModel> weatherCall = weatherApi.getWeatherCurrent(locationGetWeather);
        weatherCall.enqueue(new Callback<RootModel>() {
            @Override
            public void onResponse(@NonNull Call<RootModel> call, @NonNull Response<RootModel> response) {
                Log.d("weather_res==", "print" + response.code());
                if (response.body() != null) {
                    //data for today
                    CurrentCondition currentCondition = response.body().current_condition.get(0);
                    List<NearestArea> nearestAreaList = response.body().nearest_area;
                    List<AreaName> areaNameList = nearestAreaList.get(0).areaName;

                    weatherString = currentCondition.weatherDesc.get(0).value;
                    location = areaNameList.get(0).value + ":";
                    temperature = isDegreeC ? currentCondition.temp_C + "°" : currentCondition.temp_F + "°";

                    startForeground(NOTIFY_ID, buildNotification());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void createWeatherNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WeatherNotify";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(weatherChannelId, name, importance);
            manager.createNotificationChannel(mChannel);
        }
    }

    @SuppressLint("RemoteViewLayout")
    private Notification buildNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.OPEN_FROM_NOTIFICATION, true);
        PendingIntent clickIntent = PendingIntent.getActivity(
                this, 1,
                intent,
                PendingIntent.FLAG_IMMUTABLE);

        RemoteViews notifyLayout = new RemoteViews(getPackageName(), R.layout.notify_small);
        notifyLayout.setTextViewText(R.id.tvLocation, location);
        notifyLayout.setTextViewText(R.id.tvTemperature, temperature);
        notifyLayout.setTextViewText(R.id.tvDes, weatherString);
        notifyLayout.setImageViewResource(R.id.imgWeather, WeatherState.getImageWeather(weatherString));

        RemoteViews notifyLayoutExpended = new RemoteViews(getPackageName(), R.layout.notify_large);
        notifyLayoutExpended.setTextViewText(R.id.tvLocation, location);
        notifyLayoutExpended.setTextViewText(R.id.tvTemperature, temperature);
        notifyLayoutExpended.setTextViewText(R.id.tvDes, weatherString);
        notifyLayoutExpended.setImageViewResource(R.id.imgWeather, WeatherState.getImageWeather(weatherString));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, weatherChannelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notifyLayout)
                .setCustomBigContentView(notifyLayoutExpended)
                .setContentIntent(clickIntent)
                .setAutoCancel(false)
                .setOngoing(true);

        builder.setColor(Color.parseColor("#343333"));
        builder.setColorized(true);

        return builder.build();
    }
}
