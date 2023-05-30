package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User

interface UpdateUserUseCase {

    suspend operator fun invoke(user: User): Result<Unit>
}