package com.example.netcloudmusic.core.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

actual object NetworkUtil {
    private var applicationContext: Context? = null

    actual fun initialize(context: Any) {
        applicationContext = (context as Context).applicationContext
    }

    actual fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}
