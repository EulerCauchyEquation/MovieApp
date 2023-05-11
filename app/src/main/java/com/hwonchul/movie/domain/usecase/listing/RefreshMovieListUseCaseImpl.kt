package com.hwonchul.movie.domain.usecase.listing

import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RefreshMovieListUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : RefreshMovieListUseCase {

    override fun invoke(listType: MovieListType): Completable {
        return repository.refreshForMovieList(listType)
    }
}