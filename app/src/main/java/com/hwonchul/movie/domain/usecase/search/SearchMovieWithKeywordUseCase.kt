package com.hwonchul.movie.domain.usecase.search

import androidx.paging.PagingData
import com.hwonchul.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface SearchMovieWithKeywordUseCase {

    operator fun invoke(keyword: String): Flow<PagingData<Movie>>
}