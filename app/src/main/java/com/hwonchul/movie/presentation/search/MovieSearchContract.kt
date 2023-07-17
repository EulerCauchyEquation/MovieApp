package com.hwonchul.movie.presentation.search

import androidx.paging.PagingData
import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.Movie

class MovieSearchContract {

    data class MovieSearchData(
        val keyword: String = "",
        val pagedMovieList: PagingData<Movie> = PagingData.empty(),
    ) : UiData

    sealed class MovieSearchState : UiState {
        object Loading : MovieSearchState()
        object Idle : MovieSearchState()
        data class Error(val message: Int) : MovieSearchState()
    }
}