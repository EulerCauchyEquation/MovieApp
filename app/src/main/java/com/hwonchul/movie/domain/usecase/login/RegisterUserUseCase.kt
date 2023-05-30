package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.model.User

interface RegisterUserUseCase {

    suspend operator fun invoke(user: User): Result<Unit>
}