package com.hwonchul.movie.domain.usecase.login

import com.hwonchul.movie.domain.model.User
import io.reactivex.rxjava3.core.Completable

interface RegisterUserUseCase {

    operator fun invoke(user: User): Completable
}