package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RefreshUserInfoUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : RefreshUserInfoUseCase {

    override fun invoke(phoneNumber: String?): Completable {
        return userRepository.refreshUserInfo(phoneNumber)
    }
}