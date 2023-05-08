package com.hwonchul.movie.domain.usecase.account

import io.reactivex.rxjava3.core.Completable

interface RefreshUserInfoUseCase {

    operator fun invoke(phoneNumber: String? = null): Completable
}