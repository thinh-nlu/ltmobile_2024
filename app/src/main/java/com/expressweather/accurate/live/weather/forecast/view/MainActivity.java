package com.expressweather.accurate.live.weather.forecast.view;

import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.getPref;
import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.isNetworkAvailable;
import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.setPref;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivityMainBinding;
import com.expressweather.accurate.live.weather.forecast.dialog.RateDialog;
import com.expressweather.accurate.live.weather.forecast.dialog.ThankYouDialog;
import com.expressweather.accurate.live.weather.forecast.helper.ItemClickListener;
import com.expressweather.accurate.live.weather.forecast.helper.WeatherState;
import com.expressweather.accurate.live.weather.forecast.model.DrawerItemModel;
import com.expressweather.accurate.live.weather.forecast.model.WeatherSearchModel;
import com.expressweather.accurate.live.weather.forecast.model.network_model.AreaName;
import com.expressweather.accurate.live.weather.forecast.model.network_model.CurrentCondition;
import com.expressweather.accurate.live.weather.forecast.model.network_model.NearestArea;
import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;
import com.expressweather.accurate.live.weather.forecast.model.network_model.Weather;
import com.expressweather.accurate.live.weather.forecast.networking.WeatherApi;
import com.expressweather.accurate.live.weather.forecast.networking.WeatherNetworkService;
import com.expressweather.accurate.live.weather.forecast.services.WeatherNotifyService;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.Network;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;
import com.expressweather.accurate.live.weather.forecast.utils.eventbus.UpdateEvent;
import com.expressweather.accurate.live.weather.forecast.view.adapter.DrawerItemCustomAdapter;
import com.expressweather.accurate.live.weather.forecast.view.config.ConfigActivity;
import com.expressweather.accurate.live.weather.forecast.view.home.WeatherPagerAdapter;
import com.expressweather.accurate.live.weather.forecast.view.language.LanguageActivity;
import com.expressweather.accurate.live.weather.forecast.view.search.SearchActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.AppOpenManager;
import com.nlbn.ads.util.ConsentHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements ItemClickListener {
    private RateDialog rateDialog;
    private ThankYouDialog thankYouDialog;
    private WeatherApi weatherApi;
    private String locationFirst;
    private String locationSecond;
    private RootModel rootModel;
    private RootModel hisRootFirst;
    private RootModel hisRootSecond;
    private final ArrayList<RootModel> listWeatherModel = new ArrayList<>();
    private WeatherPagerAdapter weatherAdapter;
    private WeatherSearchModel weatherCurrent;
    private int countPage = 1; // always have current weather
    private int countPageLoaded = 0;
    public static boolean updateConfig;
    private LocationRequest locationRequest;
    private  LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected ActivityMainBinding setViewBinding() {
        return ActivityMainBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        try {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setInterval(2000);
        } catch (Exception e){}

        initDrawer();
        SharePrefUtils.increaseCountFirstLang(this);
        //check gps
        if (checkTurnOnLocation()) {
            initData();
        }
        ConsentHelper consentHelper = ConsentHelper.getInstance(this);
        if (!consentHelper.canLoadAndShowAds()) {
            consentHelper.reset();
        }
        consentHelper.obtainConsentAndShow(this, ()->{
            loadBanner();
            EventBus.getDefault().postSticky(new UpdateEvent());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //enable ads resume
        AppOpenManager.getInstance().enableAppResumeWithActivity(MainActivity.class);
        if (!checkTurnOnLocation()) {
            openPopUpLocationSetting(this);
        }

        if (updateConfig) {
            initGeneralItem();
            updateConfig = false;
        }
    }

    private void loadBanner() {
        if (isNetworkAvailable(this)) {
            binding.rlBanner.setVisibility(View.VISIBLE);
            Admob.getInstance().loadCollapsibleBanner(MainActivity.this, getString(R.string.banner_collapse), "bottom");
        } else {
            binding.rlBanner.setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    private void initData() {
        binding.layoutLoading.setVisibility(View.VISIBLE);

        weatherApi = WeatherNetworkService.getClient();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                // Logic to handle location object
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        Log.d("locationDebug", "location is :" + addresses.get(0).getAdminArea());
                        getDataFromApi(addresses.get(0).getAdminArea());
                        Intent intent = new Intent(this, WeatherNotifyService.class);
                        SharePrefUtils.putString(SharePrefUtils.LOCATION_FOR_SERVICE, addresses.get(0).getAdminArea());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        }
                    }
                } catch (IOException e) {}
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {

                    }
                    if (addresses != null && addresses.size() > 0) {
                        getDataFromApi(addresses.get(0).getAdminArea());
                    }
                }
                if(locationCallback != null && fusedLocationClient != null){
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        //get weather data history
        locationFirst = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_FIRST, "");
        locationSecond = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_SECOND, "");

        if (!locationFirst.isEmpty()) {
            getDataFromApi(locationFirst);
            countPage = 2;
        }

        if (!locationSecond.isEmpty()) {
            getDataFromApi(locationSecond);
            countPage = 3;
        }

        if (locationSecond.isEmpty()) {
            binding.imgPoint3.setVisibility(View.GONE);
        }

        if (locationFirst.isEmpty()) {
            binding.imgPoint2.setVisibility(View.GONE);
        }
    }

    private void initDrawer() {
        DrawerItemModel[] drawerItemSetting = new DrawerItemModel[1];
        drawerItemSetting[0] = new DrawerItemModel(R.drawable.ic_config, getString(R.string.config));

        DrawerItemCustomAdapter settingDrawerAdapter = new DrawerItemCustomAdapter(this, R.layout.layout_drawer_setting_item, drawerItemSetting, this::selectItemSetting);
        binding.drawer.lvSetting.setAdapter(settingDrawerAdapter);

        initGeneralItem();
    }

    @Override
    protected void viewListener() {
        binding.drawer.imgClose.setOnClickListener(view -> binding.drawerLayout.closeDrawer(binding.clDrawer));

        binding.imgOpenDrawer.setOnClickListener(view -> openDrawer());

        binding.imgSearch.setOnClickListener(view -> {
            if(weatherCurrent != null) {
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra(SharePrefUtils.WEATHER_CURRENT_INFO, weatherCurrent);
                intent.putExtra(Constants.WEATHER_CURRENT, rootModel);
                startActivity(intent);
            }
        });
    }

    public void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    private void selectItemSetting(int position) {
        Intent intent = new Intent();
        if (position == 0) {
            intent = new Intent(this, ConfigActivity.class);
        }

        startActivity(intent);
        binding.drawerLayout.closeDrawer(binding.clDrawer);
    }

    private void selectItemGeneral(int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, LanguageActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, PolicyActivity.class);
                startActivity(intent);
                break;
            case 2:
                if (SharePrefUtils.getBoolean(SharePrefUtils.RATED, false)) {
                    share();
                } else {
                    rateHandle(false);
                }
                break;
            case 3:
                if (!SharePrefUtils.getBoolean(SharePrefUtils.RATED, false)) {
                    share();
                }
                break;
        }

        binding.drawerLayout.closeDrawer(binding.clDrawer);
    }

    private void rateHandle(boolean isOnBackPress) {
        rateDialog = new RateDialog(this);
        thankYouDialog = new ThankYouDialog(this);
        rateDialog.addOnRateListener(new RateDialog.OnRateDialogPress() {
            @Override
            public void send() {
                rateDialog.dismiss();
                thankYouDialog.show();
                SharePrefUtils.putBoolean(SharePrefUtils.RATED, true);

                //set drawer layout
                initGeneralItem();
            }

            @Override
            public void rating() {
                ReviewManager manager = ReviewManagerFactory.create(MainActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> voidTask = manager.launchReviewFlow(MainActivity.this, reviewInfo);
                        voidTask.addOnCompleteListener(task1 -> {
                            SharePrefUtils.putBoolean(SharePrefUtils.RATED, true);

                            //set drawer layout
                            initGeneralItem();
                        });
                    } else {
                        int count = SharePrefUtils.getInt(SharePrefUtils.COUNT_EXIT, 1);
                        SharePrefUtils.putInt(SharePrefUtils.COUNT_EXIT, count + 1);
                    }
                });

                rateDialog.dismiss();
                thankYouDialog.show();
            }

            @Override
            public void later() {
                rateDialog.dismiss();
                if (isOnBackPress) {
                    int count = SharePrefUtils.getInt(SharePrefUtils.COUNT_EXIT, 1);
                    SharePrefUtils.putInt(SharePrefUtils.COUNT_EXIT, count + 1);
                    finish();
                }
            }
        });
        rateDialog.show();

        thankYouDialog.binding.cvOk.setOnClickListener(v -> {
            thankYouDialog.dismiss();
            if (isOnBackPress) {
                finish();
            }
        });
    }

    public void initGeneralItem() {
        DrawerItemModel[] drawerItemGeneral;
        if (SharePrefUtils.getBoolean(SharePrefUtils.RATED, false)) {
            drawerItemGeneral = new DrawerItemModel[3];
            drawerItemGeneral[0] = new DrawerItemModel(R.drawable.ic_language, getString(R.string.languages));
            drawerItemGeneral[1] = new DrawerItemModel(R.drawable.ic_policy, getString(R.string.policy));
            drawerItemGeneral[2] = new DrawerItemModel(R.drawable.ic_share, getString(R.string.share));
        } else {
            drawerItemGeneral = new DrawerItemModel[4];
            drawerItemGeneral[0] = new DrawerItemModel(R.drawable.ic_language, getString(R.string.languages));
            drawerItemGeneral[1] = new DrawerItemModel(R.drawable.ic_policy, getString(R.string.policy));
            drawerItemGeneral[2] = new DrawerItemModel(R.drawable.ic_rate, getString(R.string.rate_this_app));
            drawerItemGeneral[3] = new DrawerItemModel(R.drawable.ic_share, getString(R.string.share));
        }

        DrawerItemCustomAdapter generalDrawerAdapter = new DrawerItemCustomAdapter(this, R.layout.layout_drawer_general_item, drawerItemGeneral, this);
        binding.drawer.lvGeneral.setAdapter(generalDrawerAdapter);
    }

    private void share() {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intentShare.putExtra(Intent.EXTRA_TEXT,  getString(R.string.download_app) +" :" + "https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intentShare, getString(R.string.share_with)));

        AppOpenManager.getInstance().disableAppResumeWithActivity(MainActivity.class);
    }

    private void getDataFromApi(String location) {
        Call<RootModel> weatherCall = weatherApi.getWeatherCurrent(location);
        weatherCall.enqueue(new Callback<RootModel>() {
            @Override
            public void onResponse(@NonNull Call<RootModel> call, @NonNull Response<RootModel> response) {
                if (response.body() != null) {
                    pushDataIntoView(location , response.body());
                    setPref(MainActivity.this, Constants.GET_API_WEATHER_FIRST, true);
                } else {
                    RootModel rootModelFAKE = new RootModel();
                    rootModelFAKE.current_condition = getListCurrentConditionFAKE();
                    rootModelFAKE.nearest_area = getListNearAreaFAKE();
                    rootModelFAKE.weather = getListWeatherFAKE();
                    pushDataIntoView(location , rootModelFAKE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootModel> call, @NonNull Throwable t) {
                RootModel rootModelFAKE = new RootModel();
                rootModelFAKE.current_condition = getListCurrentConditionFAKE();
                rootModelFAKE.nearest_area = getListNearAreaFAKE();
                rootModelFAKE.weather = getListWeatherFAKE();
                pushDataIntoView(location , rootModelFAKE);
            }
        });
    }

    private void pushDataIntoView(String location, RootModel response) {
        if (response != null) {
            if (location != null && location.equals(locationFirst)) {
                hisRootFirst = response;
            } else if (location != null && location.equals(locationSecond)) {
                hisRootSecond = response;
            } else {
                rootModel = response;

                //set data weather search current
                // in order to show default item in search screen
                CurrentCondition currentCondition =  rootModel.current_condition.get(0);
                List<NearestArea> nearestAreaList = rootModel.nearest_area;
                List<AreaName> areaNameList = nearestAreaList.get(0).areaName;
                String weatherString = currentCondition.weatherDesc.get(0).value;
                WeatherState.getImageWeather(weatherString);

                weatherCurrent = new WeatherSearchModel(WeatherState.gifWeather, areaNameList.get(0).value,
                        currentCondition.temp_C, currentCondition.temp_F, weatherString, WeatherState.getImageWeather(weatherString));
            }

            setPref(MainActivity.this, Constants.CURRENT_CONDITION_REAL_LOCAL, new Gson().toJson(response.current_condition));
            setPref(MainActivity.this, Constants.NEAR_AREA_REAL_LOCAL, new Gson().toJson(response.nearest_area));
            setPref(MainActivity.this, Constants.WEATHER_REAL_LOCAL, new Gson().toJson(response.weather));

            countPageLoaded++;
            if (countPageLoaded == countPage) {
                listWeatherModel.add(0, rootModel);
                if (!locationFirst.isEmpty()) {
                    listWeatherModel.add(1, hisRootFirst);
                }

                if (!locationSecond.isEmpty()) {
                    listWeatherModel.add(2, hisRootSecond);
                }

                weatherAdapter = new WeatherPagerAdapter(MainActivity.this, listWeatherModel);
                binding.vpWeather.setAdapter(weatherAdapter);
                handleWeatherViewPager();
                binding.layoutLoading.setVisibility(View.GONE);
            }
        } else {
            binding.layoutLoading.setVisibility(View.GONE);
        }
    }

    private ArrayList<CurrentCondition> getListCurrentConditionFAKE() {
        ArrayList<CurrentCondition> currentConditionsFAKE = new ArrayList();
        String currentLocationRealLocal = getPref(MainActivity.this, Constants.CURRENT_CONDITION_REAL_LOCAL, Constants.CURRENT_CONDITION_FAKE).toString();
        try {
            Type typeOfObjectsList = new TypeToken<ArrayList<CurrentCondition>>() {}.getType();
            if (!getPref(MainActivity.this, Constants.GET_API_WEATHER_FIRST, false)) {
                currentConditionsFAKE = new Gson().fromJson(Constants.CURRENT_CONDITION_FAKE, typeOfObjectsList);
            } else {
                currentConditionsFAKE = new Gson().fromJson(currentLocationRealLocal, typeOfObjectsList);
            }
            return currentConditionsFAKE;

        } catch (Exception e) {
            return currentConditionsFAKE;
        }

    }
    private ArrayList<NearestArea> getListNearAreaFAKE() {
        ArrayList<NearestArea> nearAreaFAKE = new ArrayList();
        String nearAreaRealLocal = getPref(MainActivity.this, Constants.NEAR_AREA_REAL_LOCAL, Constants.NEAR_AREA_FAKE).toString();
        try {
            Type typeOfObjectsList = new TypeToken<List<NearestArea>>() {}.getType();
            if (!getPref(MainActivity.this, Constants.GET_API_WEATHER_FIRST, false)) {
                nearAreaFAKE = new Gson().fromJson(Constants.NEAR_AREA_FAKE, typeOfObjectsList);
            } else {
                nearAreaFAKE = new Gson().fromJson(nearAreaRealLocal, typeOfObjectsList);
            }
            return nearAreaFAKE;

        } catch (Exception e) {
            return nearAreaFAKE;
        }

    }
    private ArrayList<Weather> getListWeatherFAKE() {
        ArrayList<Weather> weathersFAKE = new ArrayList();
        String weatherRealLocal = getPref(MainActivity.this, Constants.WEATHER_REAL_LOCAL, Constants.WEATHER_FAKE).toString();
        try {
            Type typeOfObjectsList = new TypeToken<List<Weather>>() {}.getType();
            if (!getPref(MainActivity.this, Constants.GET_API_WEATHER_FIRST, false)) {
                weathersFAKE = new Gson().fromJson(Constants.WEATHER_FAKE, typeOfObjectsList);
            } else {
                weathersFAKE = new Gson().fromJson(weatherRealLocal, typeOfObjectsList);
            }
            return weathersFAKE;

        } catch (Exception e) {
            return weathersFAKE;
        }

    }

    public void hideShowTopButton(boolean show) {
        binding.imgOpenDrawer.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        binding.imgSearch.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        binding.llPointHistory.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void handleWeatherViewPager() {
        binding.vpWeather.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                changePointHistory();
            }
        });
    }

    private void changePointHistory() {
        switch (binding.vpWeather.getCurrentItem()) {
            case 0:
                binding.imgPoint1.setImageResource(R.drawable.ic_pointer_current_selected);
                binding.imgPoint2.setImageResource(R.drawable.ic_dot_home);
                binding.imgPoint3.setImageResource(R.drawable.ic_dot_home);
                break;
            case 1:
                binding.imgPoint1.setImageResource(R.drawable.ic_pointer_current);
                binding.imgPoint2.setImageResource(R.drawable.ic_dot_home_selected);
                binding.imgPoint3.setImageResource(R.drawable.ic_dot_home);
                break;
            case 2:
                binding.imgPoint1.setImageResource(R.drawable.ic_pointer_current);
                binding.imgPoint2.setImageResource(R.drawable.ic_dot_home);
                binding.imgPoint3.setImageResource(R.drawable.ic_dot_home_selected);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int count = SharePrefUtils.getInt(SharePrefUtils.COUNT_EXIT, 1);
        if (!SharePrefUtils.getBoolean(SharePrefUtils.RATED, false)) {
            if (count == 1 || count == 3 || count == 5 || count == 7) {
                rateHandle(true);
            } else {
                SharePrefUtils.putInt(SharePrefUtils.COUNT_EXIT, count + 1);
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1555) {
            if (resultCode == Activity.RESULT_OK) {
                initData();
                Toast.makeText(MainActivity.this, getString(R.string.gps_is_turn_on), Toast.LENGTH_SHORT).show();
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, getString(R.string.gps_is_required_to_use_this_app), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rateDialog != null && rateDialog.isShowing()) {
            rateDialog.dismiss();
        }

        if (thankYouDialog != null && thankYouDialog.isShowing()) {
            thankYouDialog.dismiss();
        }

        try {
            if(locationCallback != null){
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        } catch (Exception e) {}
    }

    @Override
    public void onClick(int position) {
        selectItemGeneral(position);
    }
}