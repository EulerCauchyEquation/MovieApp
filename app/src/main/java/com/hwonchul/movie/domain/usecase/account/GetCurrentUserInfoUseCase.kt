package com.hwonchul.movie.domain.usecase.account

import com.hwonchul.movie.domain.model.User
import io.reactivex.rxjava3.core.Flowable

interface GetCurrentUserInfoUseCase {

    operator fun invoke(): Flowable<User>
}