package com.hwonchul.movie.domain.usecase.auth

import android.os.CountDownTimer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class TimerUseCaseImpl @Inject constructor() : TimerUseCase {

    // CountDownTimer 는 main thread 에서만 작동하니 처리 시 주의
    private var timer: CountDownTimer? = null

    override fun start(timeOutMillis: Long): Flow<Result<Long>> =
        callbackFlow {
            timer?.cancel()  /* 남은 시간 초기화 */
            try {
                timer = object : CountDownTimer(timeOutMillis, TIME) {
                    override fun onTick(millisUntilFinished: Long) {
                        trySend(Result.success(millisUntilFinished))
                    }

                    override fun onFinish() {
                        close()
                    }
                }.start()

                awaitClose { timer?.cancel() }
            } catch (e : Exception) {
                trySend(Result.failure(e))
                close(e)
            }
        }

    override fun stop() {
        timer?.cancel()
    }

    companion object {
        private const val TIME = 1000L
    }
}