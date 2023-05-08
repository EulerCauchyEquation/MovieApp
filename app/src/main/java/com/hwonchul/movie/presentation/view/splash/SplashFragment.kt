package com.hwonchul.movie.presentation.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hwonchul.movie.R
import com.hwonchul.movie.presentation.viewModel.splash.SplashState
import com.hwonchul.movie.presentation.viewModel.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private lateinit var navController: NavController
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        observeUserInfo()
    }

    private fun observeUserInfo() {
        viewModel.loggedInUser.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SplashState.Success -> navController.navigate(SplashFragmentDirections.navigateToMain())
                is SplashState.Failure -> navigateToLogin()
                else -> {}
            }
        }
    }

    private fun navigateToLogin() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY)
            navController.navigate(SplashFragmentDirections.navigateToLogin())
        }
    }

    companion object {
        private const val DELAY = 3000L
    }
}