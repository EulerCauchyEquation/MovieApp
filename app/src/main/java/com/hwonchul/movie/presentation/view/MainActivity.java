package com.hwonchul.movie.presentation.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.hwonchul.movie.R;
import com.hwonchul.movie.databinding.ActivityMainBinding;
import com.hwonchul.movie.util.NetworkStatusHelper;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Inject
    NetworkStatusHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setNetworkStatusHelper();
        setStatusBarAndNavigationBar();
    }

    private void setNetworkStatusHelper() {
        // 네트워크 상태 감지기
        helper.observe(this, networkStatus -> {
            // 인터넷 연결됨
            if (networkStatus == NetworkStatusHelper.NetworkStatus.AVAILABLE) {
                final int greenColor =
                        ContextCompat.getColor(getApplication(), android.R.color.holo_green_dark);
                binding.tvNetworkState.setBackgroundColor(greenColor);
                binding.tvNetworkState.setText(R.string.all_network_connected);
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                        binding.tvNetworkState.setVisibility(View.GONE), 2000);
            }
            // 인터넷 연결 끊김
            else if (networkStatus == NetworkStatusHelper.NetworkStatus.UNAVAILABLE) {
                final int redColor =
                        ContextCompat.getColor(getApplication(), android.R.color.holo_red_dark);
                binding.tvNetworkState.setBackgroundColor(redColor);
                binding.tvNetworkState.setText(R.string.all_network_disconnected);
                binding.tvNetworkState.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setStatusBarAndNavigationBar() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener((__, navDestination, ___) -> {
            final int whiteColor = ContextCompat.getColor(getApplication(), R.color.WHITE);
            final int blackColor = ContextCompat.getColor(getApplication(), R.color.BLACK);
            final int blue700Color = ContextCompat.getColor(getApplication(), R.color.colorPrimary);

            int uiVisibility = 0;

            // 사진보기 화면
            if (navDestination.getId() == R.id.poster) {
                getWindow().setStatusBarColor(blackColor);
                getWindow().setNavigationBarColor(blackColor);
            }
            // 로그인 화면
            else if (navDestination.getId() == R.id.login
                    || navDestination.getId() == R.id.phone_auth
                    || navDestination.getId() == R.id.account
                    || navDestination.getId() == R.id.profile_edit
                    || navDestination.getId() == R.id.phone_auth_check) {
                getWindow().setStatusBarColor(whiteColor);
                getWindow().setNavigationBarColor(whiteColor);
                uiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            // 다른 화면
            else {
                if (navDestination.getId() == R.id.movie_list) {
                    getWindow().setNavigationBarColor(ContextCompat.getColor(getApplication(), R.color.colorPrimary));
                } else {
                    getWindow().setNavigationBarColor(whiteColor);
                }
                getWindow().setStatusBarColor(blue700Color);
                uiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            getWindow().getDecorView().setSystemUiVisibility(uiVisibility);
        });
    }

    public void showSnackbar(@NonNull String message) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
        // 시작위치 Top 으로 변경
        View view = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        // custom layout 으로 변경
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        View snackView = LayoutInflater.from(this).inflate(R.layout.snackbar, null);
        TextView tvMessage = snackView.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        layout.addView(snackView);

        snackbar.setBackgroundTint(ContextCompat.getColor(getApplication(), R.color.WHITE));
        snackbar.show();
    }
}