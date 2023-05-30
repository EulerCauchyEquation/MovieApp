package com.hwonchul.movie.domain.usecase.listing

import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import javax.inject.Inject

class RefreshMovieListUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : RefreshMovieListUseCase {

    override suspend fun invoke(listType: MovieListType): Result<Unit> =
        runCatching { repository.refreshForMovieList(listType) }
}