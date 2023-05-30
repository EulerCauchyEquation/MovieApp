package com.hwonchul.movie.presentation.splash

import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.account.RefreshUserInfoUseCase
import com.hwonchul.movie.presentation.splash.SplashContract.SplashData
import com.hwonchul.movie.presentation.splash.SplashContract.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val refreshUserInfoUseCase: RefreshUserInfoUseCase,
) : BaseViewModel<SplashData, SplashState>(SplashData(), SplashState.Loading) {

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getCurrentUser()
            }
        }
    }

    private suspend fun getCurrentUser() {
        setState { SplashState.Loading }
        getCurrentUserInfoUseCase().collectLatest { result ->
            result
                .onSuccess { refreshUser() }
                .onFailure { setState { SplashState.NoUser } }
        }
    }

    private suspend fun refreshUser() {
        refreshUserInfoUseCase()
            .onSuccess { setState { SplashState.UserAvailable } }
            .onFailure { setState { SplashState.Error(R.string.user_info_get_failed) } }
    }
}