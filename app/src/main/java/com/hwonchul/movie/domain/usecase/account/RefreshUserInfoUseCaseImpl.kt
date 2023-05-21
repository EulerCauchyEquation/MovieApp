package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class RefreshUserInfoUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : RefreshUserInfoUseCase {

    override suspend fun invoke(phoneNumber: String?): Result<Unit> =
        runCatching { userRepository.refreshUserInfo(phoneNumber) }
}