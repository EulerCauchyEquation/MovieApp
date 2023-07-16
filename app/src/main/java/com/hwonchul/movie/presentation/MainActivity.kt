package com.hwonchul.movie.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.hwonchul.movie.R
import com.hwonchul.movie.databinding.ActivityMainBinding
import com.hwonchul.movie.presentation.account.profile.ProfileFragment
import com.hwonchul.movie.util.NetworkStatusHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProfileFragment.GalleryImagePicker {

    private lateinit var binding: ActivityMainBinding

    @JvmField
    @Inject
    var helper: NetworkStatusHelper? = null

    private lateinit var galleryImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setNetworkStatusHelper()
        setOnActivityResult()
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

    private fun setOnActivityResult() {
        galleryImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                // ActivityResultContracts : 요청 결과의 유형
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val fragment = navHostFragment.childFragmentManager.fragments[0]
                if (fragment is ProfileFragment) {
                    fragment.updateProfileImage(uri)
                }
            }
    }

    override fun openGallery() {
        galleryImageLauncher.launch("image/*")
    }
}
