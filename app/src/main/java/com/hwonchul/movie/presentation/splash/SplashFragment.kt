package com.hwonchul.movie.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hwonchul.movie.R
import com.hwonchul.movie.presentation.splash.SplashContract.SplashState
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