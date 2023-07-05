package com.hwonchul.movie.domain.usecase.listing

import androidx.paging.PagingData
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import kotlinx.coroutines.flow.Flow

interface GetMovieListAsPagedUseCase {

    operator fun invoke(listType: MovieListType): Flow<PagingData<Movie>>
}