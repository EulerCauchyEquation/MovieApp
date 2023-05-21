package com.hwonchul.movie.domain.repository

import com.hwonchul.movie.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    /**
     * 영화클립 가져오기
     */
    suspend fun getAllVideosByMovieId(movieId: Int): Flow<List<Video>>

    /**
     * API 에 최신 데이터를 요청
     */
    suspend fun refreshFromRemote(movieId: Int)
}