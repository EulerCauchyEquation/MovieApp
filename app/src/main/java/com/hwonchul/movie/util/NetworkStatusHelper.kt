package com.hwonchul.movie.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import timber.log.Timber

/**
 * 네트워크 상태 감지기
 */
class NetworkStatusHelper(context: Context) : LiveData<NetworkStatusHelper.NetworkStatus>() {

    /* List로 하니 중복이 생겨 Set으로 변경 */
    private val validNetworks : MutableSet<Network>
    private val connectivityManager: ConnectivityManager
    private val networkCallback: NetworkCallback
    private val networkRequest: NetworkRequest

    init {
        validNetworks = HashSet()
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val hasInternetCapability = isValidated(network)
                if (hasInternetCapability) {
                    validNetworks.add(network)
                    setStatus()
                    Timber.d("onAvailable : %s", network)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                validNetworks.remove(network)
                setStatus()
                Timber.d("onLost : %s", network)
            }
        }

        // 콜백할 network request 결정
        networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
    }

    private fun isValidated(network: Network): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return (networkCapabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
    }

    // 네트워크 상태를 세팅
    fun setStatus() {
        if (validNetworks.isEmpty()) {
            postValue(NetworkStatus.UNAVAILABLE)
        } else {
            postValue(NetworkStatus.AVAILABLE)
        }
    }

    override fun onActive() {
        super.onActive()
        // 활성화 상태일 때는 register
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // 처음 미연결 상태면 감지하지 못하므로 따로 처리해준다.
        val network = connectivityManager.activeNetwork
        if (network == null) {
            validNetworks.clear()
            postValue(NetworkStatus.UNAVAILABLE)
        }
        Timber.d("onActive")
    }

    override fun onInactive() {
        super.onInactive()
        // 비활성화일 때는 unregister
        connectivityManager.unregisterNetworkCallback(networkCallback)
        Timber.d("onInactive")
    }

    enum class NetworkStatus {
        AVAILABLE, UNAVAILABLE
    }
}