package com.expressweather.accurate.live.weather.forecast.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class Network {
//    public boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        android.net.Network nw = connectivityManager.getActiveNetwork();
//        boolean networkConnected;
//
//        if (nw == null) {
//            networkConnected = false;
//        } else {
//            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
//            networkConnected = actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
//                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
//        }
//        return networkConnected;
//    }
}