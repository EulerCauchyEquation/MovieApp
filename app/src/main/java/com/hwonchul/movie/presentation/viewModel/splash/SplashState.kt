package com.hwonchul.movie.presentation.viewModel.splash

import com.hwonchul.movie.presentation.viewModel.login.LoginState

sealed class SplashState {
    object Loading : SplashState()
    object Success : SplashState()
    data class Failure(val message: Int) : SplashState()
}
