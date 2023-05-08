package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User
import io.reactivex.rxjava3.core.Completable

interface UpdateUserUseCase {

    operator fun invoke(user: User): Completable
}