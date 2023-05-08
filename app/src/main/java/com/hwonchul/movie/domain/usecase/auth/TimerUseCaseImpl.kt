package com.hwonchul.movie.domain.usecase.auth

import android.os.CountDownTimer
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class TimerUseCaseImpl @Inject constructor() : TimerUseCase {

    // CountDownTimer 는 main thread 에서만 작동하니 처리 시 주의
    private var timer: CountDownTimer? = null

    override fun start(timeOutMillis: Long): Flowable<Long> {
        return Flowable.create<Long>({ emitter ->
            timer?.cancel()
            timer = object : CountDownTimer(timeOutMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (emitter.isCancelled) {
                        // 구독이 취소됐다면 타이머도 취소
                        timer?.cancel()
                    } else {
                        emitter.onNext(millisUntilFinished)
                    }
                }

                override fun onFinish() {
                    emitter.onComplete()
                }
            }.start()
        }, BackpressureStrategy.BUFFER)
    }

    override fun stop() {
        timer?.cancel()
    }
}