package com.hwonchul.movie.domain.usecase.login

import io.reactivex.rxjava3.core.Completable

interface ValidateUniqueNickNameUseCase {

    operator fun invoke(nickName: String): Completable
}