package com.hwonchul.movie.domain.usecase.account

import io.reactivex.rxjava3.core.Completable

interface WithdrawalUseCase {

    operator fun invoke(): Completable
}