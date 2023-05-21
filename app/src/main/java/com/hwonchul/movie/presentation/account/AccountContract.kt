package com.hwonchul.movie.presentation.account

import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.User

class AccountContract {

    data class AccountData(
        val user: User = User.EMPTY,
    ) : UiData

    sealed class AccountState : UiState {
        object Loading : AccountState()
        object Idle : AccountState()
        object LogoutSuccess : AccountState()
        object WithdrawalSuccess : AccountState()
        data class Error(val message: Int) : AccountState()
    }
}