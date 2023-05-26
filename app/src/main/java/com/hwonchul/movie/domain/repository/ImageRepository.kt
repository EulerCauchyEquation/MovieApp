package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    /**
     * 스틸컷 가져오기
     */
    fun getAllPhotosByMovieId(movieId: Int): Flow<List<Image>>

    /**
     * API 에 최신 데이터를 요청
     */
    suspend fun refreshFromRemote(movieId: Int)
}