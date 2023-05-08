package com.hwonchul.movie.presentation.viewModel.account.profile

sealed class ProfileState {
    object Loading : ProfileState()
    object Success : ProfileState()
    data class Failure(val message: Int) : ProfileState()
}
