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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateNickNameUseCase: ValidateNickNameUseCase,
    private val checkDuplicateNickNameUseCase: CheckDuplicateNickNameUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) : BaseViewModel<ProfileData, ProfileState>(
    ProfileData(user = savedStateHandle[KEY_USER] ?: User.EMPTY),
    ProfileState.Idle
) {

    fun updateUser() {
        viewModelScope.launch {
            setState { ProfileState.Loading }
            val user = uiData.value!!.user
            val newNickname = uiData.value!!.newNickname

            if (user.nickname == newNickname) {
                // 기존 닉네임과 동일하면 중복 체크를 skip
                updateUserInternal(user.copy(updatedAt = LocalDateTime.now()))
            } else {
                checkDuplicateNickNameUseCase(newNickname)
                    .onSuccess { doesExist ->
                        if (doesExist) {
                            // 중복된 닉네임 있음
                            setState { ProfileState.Error(R.string.nick_unique_invalid) }
                        } else {
                            // 중복된 닉네임 없음
                            withContext(Dispatchers.IO) {
                                updateUserInternal(
                                    user.copy(
                                        nickname = newNickname,
                                        updatedAt = LocalDateTime.now()
                                    )
                                )
                            }
                        }
                    }
                    .onFailure { setState { ProfileState.Error(R.string.nick_connect_failed) } }
            }
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
                        newNickname = nickName,
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