package com.hwonchul.movie.presentation.splash

sealed class SplashState {
    object Loading : SplashState()
    object Success : SplashState()
    data class Failure(val message: Int) : SplashState()
}
