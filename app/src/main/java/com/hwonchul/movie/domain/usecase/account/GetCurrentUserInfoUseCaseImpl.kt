package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUserInfoUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetCurrentUserInfoUseCase {

    override fun invoke(): Flow<Result<User>> =
        userRepository.getUserInfo()
            .map { user -> Result.success(user) }
            .catch { e -> emit(Result.failure(e)) }
}