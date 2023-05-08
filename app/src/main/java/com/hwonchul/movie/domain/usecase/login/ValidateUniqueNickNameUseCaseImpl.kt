package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ValidateUniqueNickNameUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : ValidateUniqueNickNameUseCase {

    override operator fun invoke(nickName: String): Completable {
        return userRepository.doesNicknameExist(nickName)
    }
}