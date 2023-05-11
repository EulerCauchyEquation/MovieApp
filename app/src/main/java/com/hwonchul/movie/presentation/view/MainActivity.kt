package com.hwonchul.movie.presentation.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.ActivityMainBinding
import com.hwonchul.movie.util.NetworkStatusHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @JvmField
    @Inject
    var helper: NetworkStatusHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setNetworkStatusHelper()
        setStatusBarAndNavigationBar()
    }

    private fun setNetworkStatusHelper() {
        helper?.observe(this) { networkStatus ->
            // 인터넷 연결됨
            if (networkStatus == NetworkStatusHelper.NetworkStatus.AVAILABLE) {
                val greenColor =
                    ContextCompat.getColor(application, android.R.color.holo_green_dark)
                binding.tvNetworkState.setBackgroundColor(greenColor)
                binding.tvNetworkState.setText(R.string.all_network_connected)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.tvNetworkState.visibility = View.GONE
                }, 2000)
            } else if (networkStatus == NetworkStatusHelper.NetworkStatus.UNAVAILABLE) {
                val redColor = ContextCompat.getColor(application, android.R.color.holo_red_dark)
                binding.tvNetworkState.setBackgroundColor(redColor)
                binding.tvNetworkState.setText(R.string.all_network_disconnected)
                binding.tvNetworkState.visibility = View.VISIBLE
            }
        }
    }

    private fun setStatusBarAndNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            val whiteColor = ContextCompat.getColor(application, R.color.WHITE)
            val blackColor = ContextCompat.getColor(application, R.color.BLACK)
            val blueColor = ContextCompat.getColor(application, R.color.blue)
            val blue700Color = ContextCompat.getColor(application, R.color.colorPrimary)

            when (navDestination.id) {
                R.id.poster -> updateSystemBarColor(blackColor, blackColor)
                R.id.login, R.id.phone_auth, R.id.account, R.id.profile_edit, R.id.phone_auth_check, R.id.nick ->
                    updateSystemBarColor(
                        whiteColor,
                        whiteColor,
                        isLightStatusBar = true,
                        isLightNavigationBar = true
                    )

                R.id.splash -> updateSystemBarColor(blueColor, whiteColor)
                R.id.movie_list -> updateSystemBarColor(blue700Color, blue700Color)
                R.id.movie_detail -> updateSystemBarColor(
                    blue700Color,
                    whiteColor,
                    isLightNavigationBar = true
                )
            }
        }
    }

    private fun updateSystemBarColor(
        statusBarColor: Int,
        navigationBarColor: Int,
        isLightStatusBar: Boolean = false,
        isLightNavigationBar: Boolean = false,
    ) {
        window.statusBarColor = statusBarColor
        window.navigationBarColor = navigationBarColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                if (isLightStatusBar) {
                    setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }

                if (isLightNavigationBar) {
                    setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                } else {
                    setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }
            }
        } else @Suppress("DEPRECATION") {
            var uiVisibility = window.decorView.systemUiVisibility
            uiVisibility = if (isLightStatusBar) {
                uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            uiVisibility = if (isLightNavigationBar) {
                uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            window.decorView.systemUiVisibility = uiVisibility
        }
    }


    fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        // 시작위치 Top 으로 변경
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params

        // custom layout 으로 변경
        val layout = snackbar.view as SnackbarLayout
        val snackView = LayoutInflater.from(this).inflate(R.layout.snackbar, null)
        val tvMessage = snackView.findViewById<TextView>(R.id.tv_message)
        tvMessage.text = message
        layout.addView(snackView)
        snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.WHITE))
        snackbar.show()
    }
}