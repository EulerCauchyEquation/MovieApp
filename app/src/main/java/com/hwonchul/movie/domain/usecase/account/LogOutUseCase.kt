package com.hwonchul.movie.domain.usecase.account

interface LogOutUseCase {

    suspend operator fun invoke(): Result<Unit>
}