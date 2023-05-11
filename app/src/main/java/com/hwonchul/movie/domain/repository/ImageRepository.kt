package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.Image
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface ImageRepository {
    /**
     * 스틸컷 가져오기
     */
    fun getAllPhotosByMovieId(movieId: Int): Flowable<List<Image>>

    /**
     * API 에 최신 데이터를 요청
     */
    fun refreshFromRemote(movieId: Int): Completable
}