package com.hwonchul.movie.domain.usecase.listing

import androidx.paging.PagingData
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListAsPagedUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieListAsPagedUseCase {
    override fun invoke(listType: MovieListType): Flow<PagingData<Movie>> {
        return movieRepository.getAllMoviesByListTypeAsPaged(listType)
    }
}