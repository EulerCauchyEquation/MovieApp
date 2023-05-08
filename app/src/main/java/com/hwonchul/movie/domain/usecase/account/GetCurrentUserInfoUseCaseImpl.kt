package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetCurrentUserInfoUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetCurrentUserInfoUseCase {

    override fun invoke(): Flowable<User> {
        return userRepository.getUserInfo()
    }
}