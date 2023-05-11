package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.Video
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface VideoRepository {
    /**
     * 영화클립 가져오기
     */
    fun getAllVideosByMovieId(movieId: Int): Flowable<List<Video>>

    /**
     * API 에 최신 데이터를 요청
     */
    fun refreshFromRemote(movieId: Int): Completable
}