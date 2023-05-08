package com.hwonchul.movie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

/**
 * 네트워크 상태 감지기
 * LiveData를 상속하여 observer를 통해 관리한다.
 */
public class NetworkStatusHelper extends LiveData<NetworkStatusHelper.NetworkStatus> {

    private final Set<Network> validNetworks; /* List로 하니 중복이 생겨 Set으로 변경 */
    private final ConnectivityManager connectivityManager;
    private final ConnectivityManager.NetworkCallback networkCallback;
    private final NetworkRequest networkRequest;

    public NetworkStatusHelper(@NonNull Context context) {
        Timber.d("init...");
        validNetworks = new HashSet<>();
        connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                boolean hasInternetCapability = isValidated(network);

                if (hasInternetCapability) {
                    validNetworks.add(network);
                    setStatus();
                    Timber.d("onAvailable : %s", network);
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                validNetworks.remove(network);
                setStatus();

                Timber.d("onLost : %s", network);
            }
        };

        // 콜백할 network request 결정
        networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build();
    }

    private boolean isValidated(@NonNull Network network) {
        NetworkCapabilities networkCapabilities =
                connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    // 네트워크 상태를 세팅
    public void setStatus() {
        if (validNetworks.isEmpty()) {
            postValue(NetworkStatus.UNAVAILABLE);
        } else {
            postValue(NetworkStatus.AVAILABLE);
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
        // 활성화 상태일 때는 register
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);

        // 처음 미연결 상태면 감지하지 못하므로 따로 처리해준다.
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            validNetworks.clear();
            postValue(NetworkStatus.UNAVAILABLE);
        }
        Timber.d("onActive");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        // 비활성화일 때는 unregister
        connectivityManager.unregisterNetworkCallback(networkCallback);
        Timber.d("onInactive");
    }

    public enum NetworkStatus {
        AVAILABLE, UNAVAILABLE
    }
}
