package com.hwonchul.movie.domain.usecase.login

interface CheckDuplicateNickNameUseCase {

    suspend operator fun invoke(nickName: String): Result<Boolean>
}