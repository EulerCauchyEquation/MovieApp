package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.repository.MovieRepository
import javax.inject.Inject

class RefreshMovieUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : RefreshMovieUseCase {

    override suspend fun invoke(movieId: Int): Result<Unit> =
        runCatching { repository.refreshForMovie(movieId) }
}