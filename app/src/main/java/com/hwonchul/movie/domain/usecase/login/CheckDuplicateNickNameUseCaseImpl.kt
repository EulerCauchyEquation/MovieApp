package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.repository.UserRepository
import javax.inject.Inject

class CheckDuplicateNickNameUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : CheckDuplicateNickNameUseCase {

    override suspend operator fun invoke(nickName: String): Result<Boolean> =
        runCatching { userRepository.doesNicknameExist(nickName) }
}