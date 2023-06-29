package com.hwonchul.movie.presentation.account.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.account.UpdateUserUseCase
import com.hwonchul.movie.domain.usecase.login.CheckDuplicateNickNameUseCase
import com.hwonchul.movie.domain.usecase.login.ValidateNickNameUseCase
import com.hwonchul.movie.presentation.account.profile.ProfileContract.ProfileData
import com.hwonchul.movie.presentation.account.profile.ProfileContract.ProfileState
import com.hwonchul.movie.util.TextFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val validateNickNameUseCase: ValidateNickNameUseCase,
    private val checkDuplicateNickNameUseCase: CheckDuplicateNickNameUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) : BaseViewModel<ProfileData, ProfileState>(
    ProfileData(user = savedStateHandle[KEY_USER] ?: User.EMPTY),
    ProfileState.Idle
) {

    private val nickName: String
        get() = uiData.value!!.user.nickname ?: ""

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getUser()
            }
        }
    }

    private suspend fun getUser() {
        setState { ProfileState.Loading }
        getCurrentUserInfoUseCase().collectLatest { result ->
            result
                .onSuccess { user ->
                    setData { copy(user = user) }
                    setState { ProfileState.Idle }
                }
                .onFailure { setState { ProfileState.Error(R.string.user_info_get_failed) } }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            setState { ProfileState.Loading }
            checkDuplicateNickNameUseCase(nickName)
                .onSuccess { doesExist ->
                    if (doesExist) {
                        // 중복된 닉네임 있음
                        setState { ProfileState.Error(R.string.nick_unique_invalid) }
                    } else {
                        // 중복된 닉네임 없음
                        val user = uiData.value!!.user
                        withContext(Dispatchers.IO) {
                            updateUserInternal(user.copy(nickname = nickName))
                        }
                    }
                }
                .onFailure { setState { ProfileState.Error(R.string.nick_connect_failed) } }
        }
    }

    private suspend fun updateUserInternal(user: User) {
        updateUserUseCase(user)
            .onSuccess { setState { ProfileState.EditSuccess } }
            .onFailure { setState { ProfileState.Error(R.string.profile_update_failed) } }
    }

    fun nickNameChanged(nickName: String) {
        validateNickNameUseCase(nickName)
            .onSuccess {
                setData {
                    copy(
                        user = user.copy(nickname = nickName),
                        nickNameFormState = TextFormState(true)
                    )
                }
            }
            .onFailure { setData { copy(nickNameFormState = TextFormState(R.string.nick_invalid)) } }
    }

    fun userImageChanged(imageUrl: String?) {
        setData { copy(user = user.copy(userImage = imageUrl)) }
    }

    companion object {
        private const val KEY_USER = "user"
    }
}