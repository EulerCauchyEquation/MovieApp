package com.hwonchul.movie.domain.usecase.account

interface WithdrawalUseCase {

    suspend operator fun invoke(): Result<Unit>
}