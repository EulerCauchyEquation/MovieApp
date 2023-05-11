package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RefreshMovieUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : RefreshMovieUseCase {

    override fun invoke(movieId: Int): Completable {
        return repository.refreshForMovie(movieId)
    }
}