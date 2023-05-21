package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

/**
 * 영화상세 item 가져오기 Usecase
 */
interface GetMovieUseCase {

    suspend operator fun invoke(movieId: Int): Flow<Result<MovieDetail>>
}