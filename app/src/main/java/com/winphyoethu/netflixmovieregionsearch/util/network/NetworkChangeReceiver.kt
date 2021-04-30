package com.winphyoethu.netflixmovieregionsearch.util.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.winphyoethu.netflixmovieregionsearch.data.remote.ApiService
import dagger.android.AndroidInjection
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

class NetworkChangeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var globalNetworkStateSubject: PublishSubject<GlobalNetworkState>

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        Log.i("globalnet :: Hcode :: ", globalNetworkStateSubject.hashCode().toString())

        context?.let {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= 24) {
                val activeNetwork = connectivityManager.activeNetwork
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                Log.i("globalnet :: ", "activenetworkinfo111")

                if (networkCapabilities != null) {
                    when {
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            Log.i("globalnet :: ", "WIFI")
                            globalNetworkStateSubject.onNext(Wifi)
                        }
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            Log.i("globalnet :: ", "MOB")
                            globalNetworkStateSubject.onNext(Mobile)
                        }
                        else -> {
                            Log.i("globalnet :: ", "DC")
                            globalNetworkStateSubject.onNext(Disconnected)
                        }
                    }
                } else {
                    Log.i("globalnet :: ", "DC1")
                    globalNetworkStateSubject.onNext(Disconnected)
                }
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                Log.i("globalnet :: ", "activenetworkinfo")

                if (networkInfo != null) {
                    when (networkInfo.type) {
                        ConnectivityManager.TYPE_WIFI -> {
                            Log.i("globalnet :: ", "wifi")
                            globalNetworkStateSubject.onNext(Wifi)
                        }
                        ConnectivityManager.TYPE_MOBILE -> {
                            Log.i("globalnet :: ", "mobile")
                            globalNetworkStateSubject.onNext(Mobile)
                        }
                        else -> {
                            Log.i("globalnet :: ", "DC")
                            globalNetworkStateSubject.onNext(Disconnected)
                        }
                    }
                } else {
                    Log.i("globalnet :: ", "DC1")
                    globalNetworkStateSubject.onNext(Disconnected)
                }
            }
        }

    }

}