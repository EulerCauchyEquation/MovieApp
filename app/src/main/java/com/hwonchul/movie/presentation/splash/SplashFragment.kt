package com.hwonchul.movie.presentation.splash

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseFragment
import com.hwonchul.movie.databinding.FragmentSplashBinding
import com.hwonchul.movie.presentation.splash.SplashContract.SplashState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {
    private val viewModel: SplashViewModel by viewModels()
    override fun getSystemBar() = SystemBar(
        ContextCompat.getColor(requireActivity(), R.color.blue),
        ContextCompat.getColor(requireActivity(), android.R.color.white),
        isLightStatusBar = true,
        isLightNavigationBar = true,
    )

    override fun setObserve() {
        observeState()
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SplashState.Loading -> {}
                is SplashState.UserAvailable -> navController.navigate(SplashFragmentDirections.navigateToMain())
                is SplashState.NoUser -> navigateToLogin()
                is SplashState.Error -> setErrorDialog(getString(state.message))
            }
        }
    }

    private fun navigateToLogin() {
        lifecycleScope.launch {
            delay(DELAY)
            navController.navigate(SplashFragmentDirections.navigateToLogin())
        }
    }

    private fun setErrorDialog(msg: String) {
        AlertDialog.Builder(requireContext())
            .apply {
                setMessage(msg)
                setPositiveButton("확인") { _, _ -> requireActivity().finish() }
            }
            .create()
            .show()
    }

    companion object {
        private const val DELAY = 3000L
    }
}