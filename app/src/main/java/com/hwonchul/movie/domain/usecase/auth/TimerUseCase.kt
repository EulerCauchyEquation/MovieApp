package com.hwonchul.movie.domain.usecase.auth

import kotlinx.coroutines.flow.Flow

interface TimerUseCase {

    suspend fun start(timeOutMillis: Long): Flow<Result<Long>>

    fun stop()
}