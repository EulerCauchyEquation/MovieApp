package com.hwonchul.movie.domain.usecase.auth

import kotlinx.coroutines.flow.Flow

interface TimerUseCase {

    fun start(timeOutMillis: Long): Flow<Result<Long>>

    fun stop()
}