package com.hwonchul.movie.presentation.viewModel.login

sealed class LoginState {
    object Loading : LoginState()
    object SignUp : LoginState()
    object LoginSuccess : LoginState()
    data class Failure(val message: Int) : LoginState()
}