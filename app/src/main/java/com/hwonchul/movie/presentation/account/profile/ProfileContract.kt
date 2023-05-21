package com.hwonchul.movie.presentation.account.profile

import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.util.TextFormState

class ProfileContract {

    data class ProfileData(
        val user: User = User.EMPTY,
        val nickNameFormState: TextFormState = TextFormState(false),
    ) : UiData

    sealed class ProfileState : UiState {
        object Loading : ProfileState()
        object Idle : ProfileState()
        object EditSuccess : ProfileState()
        data class Error(val message: Int) : ProfileState()
    }
}
