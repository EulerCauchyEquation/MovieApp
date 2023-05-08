package com.hwonchul.movie.presentation.viewModel.account

import com.hwonchul.movie.domain.model.User

sealed class AccountState {
    object Loading : AccountState()
    data class Success(val user: User? = null) : AccountState()
    data class Failure(val message: Int) : AccountState()
}