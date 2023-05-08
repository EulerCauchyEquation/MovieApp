package com.hwonchul.movie.domain.usecase.auth

import io.reactivex.rxjava3.core.Flowable

interface TimerUseCase {

    fun start(timeOutMillis: Long): Flowable<Long>

    fun stop()
}