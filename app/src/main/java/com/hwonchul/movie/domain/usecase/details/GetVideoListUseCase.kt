package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Video
import kotlinx.coroutines.flow.Flow

/**
 * 영화 예고편 가져오기 UseCase
 */
interface GetVideoListUseCase {

    suspend operator fun invoke(movieId: Int): Flow<Result<List<Video>>>
}