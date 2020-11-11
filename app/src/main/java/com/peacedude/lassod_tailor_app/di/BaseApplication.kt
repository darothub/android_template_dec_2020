package com.peacedude.lassod_tailor_app.di


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.peacedude.lassod_tailor_app.helpers.i
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {
    val netWorkLiveData = MutableLiveData<Boolean>()
    override fun onCreate() {
        super.onCreate()
        networkMonitor()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerAppComponent.builder().application(this).build()
    }

    protected fun networkMonitor(): MutableLiveData<Boolean> {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                //take action when network connection is gained
                i("Application", "onAvailable")
                netWorkLiveData.postValue(true)
            }

            override fun onLost(network: Network) {
                //take action when network connection is lost
                i("Application", "onLost")
                netWorkLiveData.postValue(false)
            }

        }

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                it.registerDefaultNetworkCallback(networkCallback)
            } else {
                val request: NetworkRequest = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                it.registerNetworkCallback(request, networkCallback)
            }
        }
        return netWorkLiveData
    }
}