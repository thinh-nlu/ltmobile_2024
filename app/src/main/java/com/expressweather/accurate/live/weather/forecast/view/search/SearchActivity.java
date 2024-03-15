package com.expressweather.accurate.live.weather.forecast.view.search;

import static com.expressweather.accurate.live.weather.forecast.extentions.AppExtentionKt.isNetworkAvailable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.expressweather.accurate.live.weather.forecast.R;
import com.expressweather.accurate.live.weather.forecast.ads_config.AdsConfig;
import com.expressweather.accurate.live.weather.forecast.base.BaseActivity;
import com.expressweather.accurate.live.weather.forecast.databinding.ActivitySearchBinding;
import com.expressweather.accurate.live.weather.forecast.helper.WeatherState;
import com.expressweather.accurate.live.weather.forecast.model.WeatherSearchModel;
import com.expressweather.accurate.live.weather.forecast.model.network_model.AreaName;
import com.expressweather.accurate.live.weather.forecast.model.network_model.CurrentCondition;
import com.expressweather.accurate.live.weather.forecast.model.network_model.NearestArea;
import com.expressweather.accurate.live.weather.forecast.model.network_model.RootModel;
import com.expressweather.accurate.live.weather.forecast.networking.WeatherApi;
import com.expressweather.accurate.live.weather.forecast.networking.WeatherNetworkService;
import com.expressweather.accurate.live.weather.forecast.utils.CommonUtils;
import com.expressweather.accurate.live.weather.forecast.utils.Constants;
import com.expressweather.accurate.live.weather.forecast.utils.SharePrefUtils;
import com.expressweather.accurate.live.weather.forecast.view.search.adapter.SearchWeatherAdapter;
import com.expressweather.accurate.live.weather.forecast.view.ten_days_forecast.TenDaysActivity;
import com.nlbn.ads.callback.InterCallback;
import com.nlbn.ads.util.Admob;
import com.nlbn.ads.util.ConsentHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements SearchWeatherAdapter.WeatherItemListener {
    private WeatherApi weatherApi;
    private SearchWeatherAdapter adapter;
    private final ArrayList<WeatherSearchModel> weatherList = new ArrayList<>();
    private final ArrayList<RootModel> rootWeatherList = new ArrayList<>();
    private int countItemHistory;
    private int countItemLoaded;
    private final ArrayList<String> listLocation = new ArrayList<>();

    @Override
    protected ActivitySearchBinding setViewBinding() {
        return ActivitySearchBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConsentHelper.getInstance(SearchActivity.this).canRequestAds()) {
            if (AdsConfig.getAdsConfig().mInterstitialAdSearch == null) {
                AdsConfig.getAdsConfig().loadAdsInterSearch(this);
            }
        }
    }

    @Override
    protected void initView() {
        weatherApi = WeatherNetworkService.getClient();

        WeatherSearchModel weatherCurrent = (WeatherSearchModel) getIntent().getSerializableExtra(SharePrefUtils.WEATHER_CURRENT_INFO);
        RootModel rootWeatherCurrent = (RootModel) getIntent().getSerializableExtra(Constants.WEATHER_CURRENT);
        rootWeatherList.add(rootWeatherCurrent);
        weatherList.add(weatherCurrent);

        adapter = new SearchWeatherAdapter(weatherList, rootWeatherList, this);
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearch.setAdapter(adapter);

        getDataSearchHistory();
    }

    @Override
    protected void viewListener() {
        binding.viewHideKB.setOnClickListener(v -> {
            binding.viewHideKB.setVisibility(View.GONE);
            CommonUtils.hideSoftKeyboard(SearchActivity.this);
            binding.searchView.clearFocus();
        });

        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> binding.viewHideKB.setVisibility(View.VISIBLE));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.layoutLoading.setVisibility(View.VISIBLE);
                binding.layoutNotFindLocation.setVisibility(View.GONE);
                String searchFilter1 = query.replace(" ", "").toLowerCase();
                String searchFilterFinal = searchFilter1.replace("city", "");

                filterSearch(searchFilterFinal);
                //hide keyboard when submit search
                CommonUtils.hideSoftKeyboard(SearchActivity.this);
                binding.viewHideKB.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    binding.rvSearch.setVisibility(View.VISIBLE);
                    binding.layoutNotFindLocation.setVisibility(View.GONE);
                    Log.d("SearchDebug", "onQueryTextChange: text change");
                }
                return false;
            }
        });

        View closeBtn = binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeBtn.setOnClickListener(v -> {
            //hide keyboard when click close icon
            CommonUtils.hideSoftKeyboard(SearchActivity.this);

            //clean search view
            binding.searchView.setQuery("", false);
            binding.searchView.clearFocus();
            binding.rvSearch.setVisibility(View.VISIBLE);
        });

        binding.imgBack.setOnClickListener(view -> onBackPressed());
    }

    private void getDataSearchHistory() {
        String firstHisLocation = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_FIRST, "");
        String secondHisLocation = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_SECOND, "");
        String thirdHisLocation = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_THIRD, "");
        String fourthHisLocation = SharePrefUtils.getString(SharePrefUtils.HISTORY_SEARCH_LOCATION_FOURTH, "");

        if (!firstHisLocation.isEmpty()) {
            binding.layoutLoading.setVisibility(View.VISIBLE);
            getDataFromApi(firstHisLocation, 1);
            countItemHistory = 1;
            listLocation.add(firstHisLocation);
        }

        if (!secondHisLocation.isEmpty()) {
            binding.layoutLoading.setVisibility(View.VISIBLE);
            getDataFromApi(secondHisLocation, 2);
            countItemHistory = 2;
            listLocation.add(secondHisLocation);
        }

        if (!thirdHisLocation.isEmpty()) {
            binding.layoutLoading.setVisibility(View.VISIBLE);
            getDataFromApi(thirdHisLocation, 3);
            countItemHistory = 3;
            listLocation.add(thirdHisLocation);
        }

        if (!fourthHisLocation.isEmpty()) {
            binding.layoutLoading.setVisibility(View.VISIBLE);
            getDataFromApi(fourthHisLocation, 4);
            countItemHistory = 4;
            listLocation.add(fourthHisLocation);
        }
    }

    private void filterSearch(String searchText) {
        Call<RootModel> weatherCall = weatherApi.getWeatherCurrent(searchText);
        weatherCall.enqueue(new Callback<RootModel>() {
            @Override
            public void onResponse(@NonNull Call<RootModel> call, @NonNull Response<RootModel> response) {
                binding.layoutLoading.setVisibility(View.INVISIBLE);
                binding.rvSearch.setVisibility(View.GONE);
                if (response.code() > 400) {
                    binding.layoutNotFindLocation.setVisibility(View.VISIBLE);
                }
                if (response.body() != null) {
                    goToSearchResult(response.body(), searchText);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootModel> call, @NonNull Throwable t) {
                Toast.makeText(SearchActivity.this, getString(R.string.cannot_connect_to_wifi), Toast.LENGTH_SHORT).show();
                binding.layoutLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void goToSearchResult(RootModel dataWeather, String location) {
        binding.rvSearch.setVisibility(View.VISIBLE);

        //hide keyboard when click close icon
        CommonUtils.hideSoftKeyboard(SearchActivity.this);

        //clean search view
        binding.searchView.setQuery("", false);
        binding.searchView.clearFocus();

        Intent intent = new Intent(this, SearchWeatherResultActivity.class);

        for (String l : listLocation) {
            if (l.equals(location)) {
                intent.putExtra(Constants.LOCATION_ADDED, true);
            }
        }
        intent.putExtra(SharePrefUtils.WEATHER_SEARCH_RESULT, dataWeather);
        intent.putExtra(SharePrefUtils.LOCATION_SEARCH, location);
        startActivity(intent);
    }

    private void getDataFromApi(String location, int index) {
        Call<RootModel> weatherCall = weatherApi.getWeatherCurrent(location);
        weatherCall.enqueue(new Callback<RootModel>() {
            @Override
            public void onResponse(@NonNull Call<RootModel> call, @NonNull Response<RootModel> response) {
                binding.layoutLoading.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    RootModel rootModel = response.body();
                    //get data search weather
                    CurrentCondition currentCondition = rootModel.current_condition.get(0);
                    List<NearestArea> nearestAreaList = rootModel.nearest_area;
                    List<AreaName> areaNameList = nearestAreaList.get(0).areaName;
                    String weatherString = currentCondition.weatherDesc.get(0).value;
                    WeatherState.getImageWeather(weatherString);

                    WeatherSearchModel weatherCurrent = new WeatherSearchModel(WeatherState.gifWeather, areaNameList.get(0).value, currentCondition.temp_C, currentCondition.temp_F, weatherString, WeatherState.getImageWeather(weatherString));

                    weatherList.add(Math.min(index, weatherList.size()), weatherCurrent);
                    rootWeatherList.add(Math.min(index, rootWeatherList.size()), rootModel);

                    countItemLoaded++;
                    if (countItemLoaded == countItemHistory) {
                        adapter = new SearchWeatherAdapter(weatherList, rootWeatherList, SearchActivity.this);
                        binding.rvSearch.setAdapter(adapter);
                        binding.layoutLoading.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootModel> call, @NonNull Throwable t) {
                Toast.makeText(SearchActivity.this, "call api failure", Toast.LENGTH_SHORT).show();
                binding.layoutLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(RootModel dataWeather) {
        if (isNetworkAvailable(this)) {
                Admob.getInstance().showInterAds(this, AdsConfig.getAdsConfig().mInterstitialAdSearch, new InterCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        AdsConfig.getAdsConfig().loadAdsInterSearch(SearchActivity.this);

                        Intent intent = new Intent(SearchActivity.this, SearchWeatherResultActivity.class);

                        intent.putExtra(Constants.LOCATION_ADDED, true);
                        intent.putExtra(SharePrefUtils.WEATHER_SEARCH_RESULT, dataWeather);
                        startActivity(intent);
                    }
                });
        } else {
            Intent intent = new Intent(this, SearchWeatherResultActivity.class);

            intent.putExtra(Constants.LOCATION_ADDED, true);
            intent.putExtra(SharePrefUtils.WEATHER_SEARCH_RESULT, dataWeather);
            startActivity(intent);
        }
    }
}