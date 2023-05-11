package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetMovieUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : GetMovieUseCase {

    override fun invoke(movieId: Int): Flowable<MovieDetail> {
        return repository.getMovieDetailById(movieId)
    }
}