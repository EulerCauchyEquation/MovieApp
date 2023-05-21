package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMovieUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : GetMovieUseCase {

    override suspend fun invoke(movieId: Int): Flow<Result<MovieDetail>> {
        return repository.getMovieDetailById(movieId)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}