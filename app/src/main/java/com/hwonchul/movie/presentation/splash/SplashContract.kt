package com.hwonchul.movie.presentation.splash

import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.User

class SplashContract {

    data class SplashData(
        val user: User = User.EMPTY
    ) : UiData

    sealed class SplashState : UiState {
        object Loading : SplashState()
        object UserAvailable : SplashState()
        object NoUser : SplashState()
        data class Error(val message: Int) : SplashState()
    }
}
