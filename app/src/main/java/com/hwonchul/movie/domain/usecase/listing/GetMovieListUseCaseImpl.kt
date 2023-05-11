package com.hwonchul.movie.domain.usecase.listing

import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetMovieListUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : GetMovieListUseCase {

    override fun invoke(listType: MovieListType): Flowable<List<Movie>> {
        return repository.getAllMoviesByListType(listType)
    }
}