package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetCurrentUserInfoUseCase {

    operator fun invoke(): Flow<Result<User>>
}