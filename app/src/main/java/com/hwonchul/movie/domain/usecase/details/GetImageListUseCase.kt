package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Image
import kotlinx.coroutines.flow.Flow

/**
 * 영화 스틸컷 가져오기 Usecase
 */
interface GetImageListUseCase {

    operator fun invoke(movieId: Int): Flow<Result<List<Image>>>
}