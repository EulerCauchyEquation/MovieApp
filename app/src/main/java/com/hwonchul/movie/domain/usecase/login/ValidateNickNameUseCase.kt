package com.hwonchul.movie.domain.usecase.login

interface ValidateNickNameUseCase {

    operator fun invoke(nickName: String): Result<Unit>
}