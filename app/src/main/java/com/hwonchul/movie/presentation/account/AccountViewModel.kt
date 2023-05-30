package com.hwonchul.movie.presentation.account

import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.account.LogOutUseCase
import com.hwonchul.movie.domain.usecase.account.WithdrawalUseCase
import com.hwonchul.movie.presentation.account.AccountContract.AccountData
import com.hwonchul.movie.presentation.account.AccountContract.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
) : BaseViewModel<AccountData, AccountState>(AccountData(), AccountState.Idle) {

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getCurrentUser()
            }
        }
    }

    private suspend fun getCurrentUser() {
        setState { AccountState.Loading }
        getCurrentUserInfoUseCase().collectLatest { result ->
            result
                .onSuccess { user ->
                    setData { copy(user = user) }
                    setState { AccountState.Idle }
                }
                .onFailure { setState { AccountState.Error(R.string.user_info_get_failed) } }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            setState { AccountState.Loading }
            withContext(Dispatchers.IO) {
                logOutUseCase()
                    .onSuccess { setState { AccountState.LogoutSuccess } }
                    .onFailure { setState { AccountState.Error(R.string.logout_failure) } }
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            setState { AccountState.Loading }
            withContext(Dispatchers.IO) {
                withdrawalUseCase()
                    .onSuccess { setState { AccountState.WithdrawalSuccess } }
                    .onFailure { setState { AccountState.Error(R.string.user_withdrawal_failed) } }
            }
        }
    }
}