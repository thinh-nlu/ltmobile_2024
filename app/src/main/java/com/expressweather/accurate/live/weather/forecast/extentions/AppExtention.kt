package com.expressweather.accurate.live.weather.forecast.extentions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.preference.PreferenceManager

fun setPref(c: Context, pref: String, value: String) {
    val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
    e.putString(pref, value)
    e.apply()

}

fun getPref(c: Context, pref: String, value: String): String? {
    return PreferenceManager.getDefaultSharedPreferences(c).getString(
        pref,
        value
    )
}

fun setPref(c: Context, pref: String, value: Boolean) {
    val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
    e.putBoolean(pref, value)
    e.apply()

}

fun getPref(c: Context, pref: String, value: Boolean): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(
        pref, value
    )
}

fun setPref(c: Context, pref: String, value: Int) {
    val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
    e.putInt(pref, value)
    e.apply()

}

fun getPref(c: Context, pref: String, value: Int): Int {
    return PreferenceManager.getDefaultSharedPreferences(c).getInt(
        pref, value
    )
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}