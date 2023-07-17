package com.hwonchul.movie.domain.usecase.search

import androidx.paging.PagingData
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMovieWithKeywordUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : SearchMovieWithKeywordUseCase {
    override fun invoke(keyword: String): Flow<PagingData<Movie>> =
        movieRepository.searchMovieByKeyword(keyword)
}