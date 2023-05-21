package com.hwonchul.movie.domain.usecase.account

interface RefreshUserInfoUseCase {

    suspend operator fun invoke(phoneNumber: String? = null): Result<Unit>
}