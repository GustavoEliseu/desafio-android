package com.picpay.desafio.android.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun hasNetwork(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}